package com.agroknow.socnav.domain;

import com.agroknow.socnav.api.TaggingApiController;
import com.agroknow.socnav.reference.SharingLevelType;
import flexjson.JSONSerializer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.solr.RooSolrSearchable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.scheduling.annotation.Async;

@RooEquals
@RooJavaBean
@RooToString
@RooJson
@RooSolrSearchable
@RooJpaActiveRecord(table = "taggings", finders = { "findTaggingsByUserAndItem", "findTaggingsByUser", "findTaggingsByItem", "findTaggingsByTags" })
public class Tagging extends Event {
	
	private static final Logger log = LoggerFactory.getLogger(Tagging.class);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "tagging", orphanRemoval = true)
    private Set<Tag> tags = new HashSet<Tag>();

    private void prepareTags() {
        for (Tag current_tag : tags) {
            current_tag.setTagging(this);
        }
    }

    @PrePersist
    private void beforeSave() {
        this.beforeSaveEvent();
        this.prepareTags();
    }

    @PreUpdate
    private void beforeUpdate() {
    	log.debug("preUpdate");
        this.prepareTags();
    }

    @Async
    public static void indexTaggings(Collection<com.agroknow.socnav.domain.Tagging> taggings) {
        List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
        for (Tagging tagging : taggings) {
            SolrInputDocument sid = new SolrInputDocument();
            sid.addField("id", "tagging_" + tagging.getId());
            sid.addField("tagging.updated_at_dt", tagging.getUpdated_at());
            sid.addField("tagging.sharing_level_t", tagging.getSharing_level());
            sid.addField("tagging.session_id_s", tagging.getSession_id());
            sid.addField("tagging.domain_s", tagging.getDomain());
            sid.addField("tagging.ip_address_s", tagging.getIp_address());
            sid.addField("tagging.id_l", tagging.getId());
            sid.addField("tagging.user_id_l", tagging.getUser().getId());
            sid.addField("tagging.user_remote_id_l", tagging.getUser().getRemote_id());
            sid.addField("item.id_l", tagging.getItem().getId());
            sid.addField("item.resource_uri_s", tagging.getItem().getResource_uri());
            sid.addField("item.resource_id_l", tagging.getItem().getResource_id());
            sid.addField("item.metadata_uri_s", tagging.getItem().getMetadata_uri());
            for (Tag tag : tagging.tags) {
                sid.addField("tagging.tag_" + tag.getLang() + "_str", tag.getValue());
                sid.addField("tagging.tag_" + tag.getLang() + "_txt", tag.getValue());
                sid.addField("tagging.tag_str", tag.getValue());
                sid.addField("tagging.tag_txt", tag.getValue());
            }
            documents.add(sid);
        }
        try {
            SolrServer solrServer = solrServer();
            solrServer.add(documents);
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        return new JSONSerializer().include("user", "item", "tags").exclude("tags.id","tags.version","*.class").serialize(this);
    }

    public static String toJsonArray(Collection<com.agroknow.socnav.domain.Tagging> collection) {
        return new JSONSerializer().include("user", "item", "tags").exclude("tags.id","tags.version","*.class").serialize(collection);
    }

    public void setTags(Set<com.agroknow.socnav.domain.Tag> tags) {
        this.tags = tags;
    }
    
    public static List<Tagging> findTaggingEntriesByUserAndItem(User user, Item item, int firstResult, int maxResults) {
        return findTaggingsByUserAndItem(user, item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Tagging> findTaggingEntriesByUser(User user, int firstResult, int maxResults) {
        return findTaggingsByUser(user).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Tagging> findTaggingEntriesByItem(Item item, int firstResult, int maxResults) {
        return findTaggingsByItem(item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Tagging> findTaggingEntriesByTags(List<Tag> tagList, int firstResult, int maxResults) {
    	Set<Tag> tags = new HashSet<Tag>();
    	Iterator<Tag> iter = tagList.iterator();
    	while (iter.hasNext()){
    		Tag t = iter.next();
    		log.debug(t.getValue());
    		log.debug("id "+t.getId());
    		log.debug("tagging "+t.getTagging().getId());
    		tags.add(t);
    	}
    	log.debug("tags size: "+tags.size());
    	List<Tagging> foos = findTaggingsByTags(tags).getResultList();
    	log.debug("foss size: "+ foos.size());
        return findTaggingsByTags(tags).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}

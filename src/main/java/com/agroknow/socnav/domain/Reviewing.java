package com.agroknow.socnav.domain;

import com.agroknow.socnav.reference.LanguageType;
import com.agroknow.socnav.reference.SharingLevelType;
import flexjson.JSONSerializer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
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
@RooJpaActiveRecord(table = "reviewings", finders = { "findReviewingsByUserAndItem", "findReviewingsByUser", "findReviewingsByItem" })
public class Reviewing extends Event {

    @NotNull
    //@Size(max = 255)
    private String review;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LanguageType lang;

    //@PreUpdate
    @PrePersist
    private void beforeSave() {
    	this.beforeSaveEvent();
        if (this.lang == null) {
            this.lang = LanguageType.en;
        }
    }

    @Async
    public static void indexReviewings(Collection<com.agroknow.socnav.domain.Reviewing> reviewings) {
        List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
        for (Reviewing reviewing : reviewings) {
            SolrInputDocument sid = new SolrInputDocument();
            sid.addField("id", "reviewing_" + reviewing.getId());
            sid.addField("reviewing.updated_at_dt", reviewing.getUpdated_at());
            sid.addField("reviewing.sharing_level_t", reviewing.getSharing_level());
            sid.addField("reviewing.session_id_s", reviewing.getSession_id());
            sid.addField("reviewing.domain_s", reviewing.getDomain());
            sid.addField("reviewing.ip_address_s", reviewing.getIp_address());
            sid.addField("reviewing.id_l", reviewing.getId());
            sid.addField("reviewing.review_t", reviewing.getReview());
            sid.addField("reviewing.lang_s", reviewing.getLang());
            sid.addField("reviewing.user_id_l", reviewing.getUser().getId());
            sid.addField("reviewing.user_remote_id_l", reviewing.getUser().getRemote_id());
            sid.addField("reviewing.item_id_l", reviewing.getItem().getId());
            sid.addField("reviewing.item_resource_uri_s", reviewing.getItem().getResource_uri());
            sid.addField("reviewing.item_resource_id_l", reviewing.getItem().getResource_id());
            sid.addField("reviewing.item_metadata_uri_s", reviewing.getItem().getMetadata_uri());
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
        return new JSONSerializer().include("user", "item").exclude("*.class").serialize(this);
    }

    public static String toJsonArray(Collection<com.agroknow.socnav.domain.Reviewing> collection) {
        return new JSONSerializer().include("user", "item").exclude("*.class").serialize(collection);
    }
    
    public static List<Reviewing> findReviewingEntriesByUserAndItem(User user, Item item, int firstResult, int maxResults) {
        return findReviewingsByUserAndItem(user, item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Reviewing> findReviewingEntriesByUser(User user, int firstResult, int maxResults) {
        return findReviewingsByUser(user).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Reviewing> findReviewingEntriesByItem(Item item, int firstResult, int maxResults) {
        return findReviewingsByItem(item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}

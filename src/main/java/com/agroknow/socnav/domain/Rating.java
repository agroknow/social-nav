package com.agroknow.socnav.domain;

import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;

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
@RooJpaActiveRecord(table = "ratings", finders = { "findRatingsByUser", "findRatingsByItem", "findRatingsByUserAndItem" })
public class Rating extends Event {
	
	private static final Logger log = LoggerFactory.getLogger(Rating.class);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "rating", orphanRemoval=true)
    private Set<Preference> preferences = new HashSet<Preference>();

    private float preference_avg;

    private int preference_dimensions;

    public void addPreferenceMetadata() {
    	log.debug("Setting pref metadata");
        int size = 0;
        float avg = 0;
        for (Preference current_pref : preferences) {
            avg += current_pref.getValue();
            size++;
            current_pref.setRating(this);
        }
        this.preference_avg = (avg / size);
        this.preference_dimensions = size;
    }

    public void setPreference_avg(float preference_avg) {
    }

    public void setPreference_dimensions(int preference_dimensions) {
    }

//    @PreUpdate
    @PrePersist
    private void beforeSave() {
    	log.debug("Before save rating");
    	this.beforeSaveEvent();
        this.addPreferenceMetadata();
    }
    
    @PreUpdate
    private void beforeUpdate() {
    	log.debug("preUpdate");
    	this.addPreferenceMetadata();
    }

    @Async
    public static void indexRatings(Collection<com.agroknow.socnav.domain.Rating> ratings) {
        List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
        for (Rating rating : ratings) {
            SolrInputDocument sid = new SolrInputDocument();
            sid.addField("id", "rating_" + rating.getId());
            sid.addField("rating.updated_at_dt", rating.getUpdated_at());
            sid.addField("rating.sharing_level_s", rating.getSharing_level());
            sid.addField("rating.session_id_s", rating.getSession_id());
            sid.addField("rating.domain_s", rating.getDomain());
            sid.addField("rating.ip_address_s", rating.getIp_address());
            sid.addField("rating.id_l", rating.getId());
            sid.addField("rating.user_id_l", rating.getUser().getId());
            sid.addField("rating.user_remote_id_l", rating.getUser().getRemote_id());
            sid.addField("rating.item_id_l", rating.getItem().getId());
            sid.addField("rating.item_resource_uri_s", rating.getItem().getResource_uri());
            sid.addField("rating.preference_avg_f", rating.getPreference_avg());
            sid.addField("rating.preference_dimensions_i", rating.getPreference_dimensions());
            for (Preference pref : rating.getPreferences()) {
                sid.addField("rating.preference_dim_" + pref.getDimension() + "_f", pref.getValue());
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
        return new JSONSerializer().include("user", "item", "preferences").exclude("*.class").serialize(this);
    }
    
    public static String toJsonArray(Collection<Rating> collection) {
        return new JSONSerializer().include("user", "item", "preferences").exclude("*.class").serialize(collection);
    }
    
    public static List<Rating> findRatingEntriesByUserAndItem(User user, Item item, int firstResult, int maxResults) {
        return findRatingsByUserAndItem(user, item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Rating> findRatingEntriesByUser(User user, int firstResult, int maxResults) {
        return findRatingsByUser(user).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Rating> findRatingEntriesByItem(Item item, int firstResult, int maxResults) {
        return findRatingsByItem(item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static List<Item> findPopularItems(int maxResults) {
        EntityManager em = Rating.entityManager();
        TypedQuery<Rating> q = em.createQuery("SELECT o FROM Rating AS o GROUP BY o.item HAVING SUM(o.preference_avg)>10", Rating.class);
        List<Rating> popularList = q.setFirstResult(0).setMaxResults(maxResults).getResultList();
        List<Item> popularItems = new ArrayList<Item>();
        for (Rating pop : popularList){
        	popularItems.add(pop.getItem());
        }
        return popularItems;
    }

}

package com.agroknow.socnav.domain;

import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooEquals
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(table = "users", finders = { "findUsersByRemote_idEquals" })
public class User {
	
	private static final Logger log = LoggerFactory.getLogger(User.class);

    private String metadata_uri;

    @NotNull
    @Column(unique = true)
    private Long remote_id;

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "user",targetEntity=Rating.class)
    private Set<Rating> ratings = new HashSet<Rating>();

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "user",targetEntity=Tagging.class)
    private Set<Tagging> taggings = new HashSet<Tagging>();

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "user",targetEntity=Reviewing.class)
    private Set<Reviewing> reviewings = new HashSet<Reviewing>();
    
    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, targetEntity=Recommendation.class)
    private Set<Recommendation> recommendations = new HashSet<Recommendation>();
    
    private transient Set<Tag> tags = new HashSet<Tag>();

    public Set<Tag> getTags() {
        if (this.tags.isEmpty()) {
            this.tags = getTagsAsFacets();
        }
        return this.tags;
    }
    
    public void setTags() {
    	this.tags = getTagsAsFacets();
    }
    
    private Set<Tag> getTagsAsFacets() {
    	Set<Tag> tagsFromSolr = new HashSet<Tag>();
    	
    	String q =  "tagging.user_id_l:"+this.getId();
    	String facetField = "tagging.tag_str";
    	SolrQuery query = new SolrQuery(q).
    					setFacet(true).
    					setFacetMinCount(1).
    					setFacetLimit(8).
    					addFacetField(facetField);
    	
    	FacetField facets = Tagging.search(query).getFacetField(facetField);
    	
    	if (facets.getValues()==null){
    		return tagsFromSolr;
    	}
    	
    	for (Count facet_count : facets.getValues()){
    		Tag tag = new Tag();
    		tag.setValue(facet_count.getName());
    		tag.setFrequency(facet_count.getCount());
    		
    		tagsFromSolr.add(tag);
    	}
		return tagsFromSolr;
    }

    /*
     * Get unique user.
     * The scope of this function is to check if a NEW user already exists based on remote_id
     */
    public static User getUniqueUser(User examined_user) {
    	log.info("Get unique user");
        List<User> uniqueUsers = User.findUsersByRemote_idEquals(examined_user.getRemote_id()).getResultList();
        if (uniqueUsers.isEmpty()) {
            return examined_user;
        } else {
            return uniqueUsers.get(0);
        }
    }

    public String toJson(Boolean withEvents, Boolean withTags) {
    	if (withEvents && withTags){
    		return new JSONSerializer().include("taggings","ratings","reviewings","tags").exclude("tags.id","tags.version","tags.tagging","*.class").serialize(this);
    	}
    	
    	if (withEvents){
    		return new JSONSerializer().include("taggings","ratings","reviewings").exclude("*.class").serialize(this);
    	}
    	
    	if (withTags){
    		return new JSONSerializer().include("tags").exclude("tags.id","tags.version","tags.tagging","*.class").serialize(this);
    	}
    	
    	return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static String toJsonArray(Collection<User> collection, Boolean with_events) {
        if (with_events){
        	return new JSONSerializer().include("taggings","ratings","reviewings").exclude("*.class").serialize(collection);
        }else {
        	return new JSONSerializer().exclude("*.class").serialize(collection);
        }
    }


}
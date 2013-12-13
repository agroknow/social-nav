package com.agroknow.socnav.domain;

import com.agroknow.socnav.reference.LanguageType;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooEquals
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(table = "tags", finders = { "findTagsByTagging", "findTagsByValueLike" })
public class Tag {

    @NotNull
    //@Size(max = 25)
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LanguageType lang;

    @ManyToOne
    private Tagging tagging;

    private transient Long frequency;

    public Long getFrequency() {
        return this.frequency;
    }

    public void setFrequency(Long freq) {
        this.frequency = freq;
    }

    @PreUpdate
    @PrePersist
    private void beforeSave() {
        if (this.lang == null) {
            this.lang = LanguageType.en;
        }
    }
    
    public static List<Tag> fetchTagsAsFacets (int max, LanguageType lang) {
    	return fetchTagsAsFacets (max, lang, null);
    }
    
    public static List<Tag> fetchTagsAsFacets (int max, LanguageType lang, Long itemId) {
    	List<Tag> tagsFromSolr = new ArrayList<Tag>();
    	if (max<0) max=5;
    	
    	String q =  "tagging.tag_"+lang+"_str:*";

    	String facetField = "tagging.tag_"+lang+"_str";
    	SolrQuery query = new SolrQuery(q).
    					setFacet(true).
    					setFacetMinCount(1).
    					setFacetLimit(max).
    					addFacetField(facetField);
    	
    	if (itemId !=null) {
    		String fq = "item.id_l:"+itemId;
    		query.addFilterQuery(fq);
    	}
    	
    	FacetField facets = Tagging.search(query).getFacetField(facetField);
    	
    	if (facets.getValues()==null){
    		return tagsFromSolr;
    	}
    	
    	for (Count facet_count : facets.getValues()){
    		Tag tag = new Tag();
    		tag.setValue(facet_count.getName());
    		tag.setLang(lang);
    		tag.setFrequency(facet_count.getCount());
    		/*Tag tagFromDb = Tag.findTagsByValueLike(facet_count.getName()).getResultList().get(0);
    		tag.setLang(tagFromDb.getLang());*/
    		tagsFromSolr.add(tag);
    	}
		return tagsFromSolr;
    	
    }

    public static String toJsonArray(Collection<Tag> collection) {
        return new JSONSerializer().include("value","lang","frequency").exclude("*").serialize(collection);
    }

    public String toJson() {
        return new JSONSerializer().include("value","lang","frequency").exclude("*").serialize(this);
    }
}

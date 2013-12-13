package com.agroknow.socnav.domain;

import com.agroknow.socnav.reference.LanguageType;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.Field;
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
@RooJpaActiveRecord(table = "items", finders = { "findItemsByResource_uriEquals", "findItemsByTaggings", "findItemsByResource_idEquals" })
public class Item {

    private static final Logger log = LoggerFactory.getLogger(Item.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Field("item.id_l")
    private Long id;

    @Field("item.metadata_uri_s")
    private String metadata_uri;

    @Field("item.resource_uri_s")
    @NotNull
    @Column(unique = true)
    private String resource_uri;

    @Field("item.resource_id_l")
    private Long resource_id;

    @OneToMany(cascade = { javax.persistence.CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "item")
    private Set<Rating> ratings = new HashSet<Rating>();

    @OneToMany(cascade = { javax.persistence.CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "item")
    private Set<Tagging> taggings = new HashSet<Tagging>();

    @OneToMany(cascade = { javax.persistence.CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "item")
    private Set<Reviewing> reviewings = new HashSet<Reviewing>();
    
    @OneToMany(cascade = { javax.persistence.CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<Recommendation> recommendations = new HashSet<Recommendation>();

    private transient Set<Tag> tags = new HashSet<Tag>();

    public Set<com.agroknow.socnav.domain.Tag> getTags() {
        if (this.tags.isEmpty()) {
            this.tags = fetchTagsAsFacets();
        }
        return this.tags;
    }
    
    //If this is set as set... , then Roo core breaks. Probably Roo bug, more testing and digging is needed.
    // Index: 0, Size: 0
    public void putTags() {
        this.tags = fetchTagsAsFacets();
    }

    private Set<com.agroknow.socnav.domain.Tag> fetchTagsAsFacets() {
        Set<Tag> tagsFromSolr = new HashSet<Tag>();
        String q = "item.id_l:" + this.id;
        String facetField = "tagging.tag_str";
        SolrQuery query = new SolrQuery(q).setFacet(true).setFacetMinCount(1).setFacetLimit(8).addFacetField(facetField);
        FacetField facets = Tagging.search(query).getFacetField(facetField);
        if (facets.getValues() == null) {
            return tagsFromSolr;
        }
        for (Count facet_count : facets.getValues()) {
            Tag tag = new Tag();
            tag.setValue(facet_count.getName());
            tag.setFrequency(facet_count.getCount());
            tagsFromSolr.add(tag);
        }
        return tagsFromSolr;
    }

    public static com.agroknow.socnav.domain.Item getUniqueItem(com.agroknow.socnav.domain.Item examined_item) {
        log.debug("unique item check");
        List<Item> uniqueItems = Item.findItemsByResource_uriEquals(examined_item.getResource_uri()).getResultList();
        if (uniqueItems.isEmpty()) {
            return examined_item;
        } else {
            return uniqueItems.get(0);
        }
    }

    public static List<com.agroknow.socnav.domain.Item> findItemByTag(String value, LanguageType lang) {
        List<Long> no_dup_ids = new ArrayList<Long>();
        List<Item> no_dup_results = new ArrayList<Item>();
        String q = value;
        if (q == null) {
            return no_dup_results;
        }
        if (lang == null) {
            q = "tagging.tag_txt:" + q;
        } else {
            q = "tagging.tag_" + lang + "_txt:" + q;
        }
        SolrQuery query = new SolrQuery(q);
        List<Item> results = Tagging.search(query).getBeans(Item.class);
        Iterator<Item> litr = results.iterator();
        while (litr.hasNext()) {
            Item exam_item = litr.next();
            if (!no_dup_ids.contains(exam_item.getId())) {
                no_dup_ids.add(exam_item.getId());
                no_dup_results.add(exam_item);
            }
        }
        return no_dup_results;
    }

    public static String toJsonArray(Collection<com.agroknow.socnav.domain.Item> collection) {
        return new JSONSerializer().include("tags").exclude("tags.id", "tags.lang", "tags.tagging", "tags.version", "*.class").serialize(collection);
    }

    public String toJson() {
        return new JSONSerializer().include("tags").exclude("tags.id", "tags.lang", "tags.tagging", "tags.version", "*.class").serialize(this);
    }

    public static List<com.agroknow.socnav.domain.Item> findPopularItems(int maxResults) {
        return Rating.findPopularItems(maxResults);
    }
}

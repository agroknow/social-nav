// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tagging;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

privileged aspect Tagging_Roo_SolrSearch {
    
    @Autowired
    transient SolrServer Tagging.solrServer;
    
    public static QueryResponse Tagging.search(String queryString) {
        String searchString = "Tagging_solrsummary_t:" + queryString;
        return search(new SolrQuery(searchString.toLowerCase()));
    }
    
    public static QueryResponse Tagging.search(SolrQuery query) {
        try {
            return solrServer().query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new QueryResponse();
    }
    
    public static void Tagging.indexTagging(Tagging tagging) {
        List<Tagging> taggings = new ArrayList<Tagging>();
        taggings.add(tagging);
        indexTaggings(taggings);
    }
    
    @Async
    public static void Tagging.deleteIndex(Tagging tagging) {
        SolrServer solrServer = solrServer();
        try {
            solrServer.deleteById("tagging_" + tagging.getId());
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @PostUpdate
    @PostPersist
    private void Tagging.postPersistOrUpdate() {
        indexTagging(this);
    }
    
    @PreRemove
    private void Tagging.preRemove() {
        deleteIndex(this);
    }
    
    public static SolrServer Tagging.solrServer() {
        SolrServer _solrServer = new Tagging().solrServer;
        if (_solrServer == null) throw new IllegalStateException("Solr server has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return _solrServer;
    }
    
}

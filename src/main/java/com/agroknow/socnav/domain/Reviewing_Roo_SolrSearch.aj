// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Reviewing;
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

privileged aspect Reviewing_Roo_SolrSearch {
    
    @Autowired
    transient SolrServer Reviewing.solrServer;
    
    public static QueryResponse Reviewing.search(String queryString) {
        String searchString = "Reviewing_solrsummary_t:" + queryString;
        return search(new SolrQuery(searchString.toLowerCase()));
    }
    
    public static QueryResponse Reviewing.search(SolrQuery query) {
        try {
            return solrServer().query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new QueryResponse();
    }
    
    public static void Reviewing.indexReviewing(Reviewing reviewing) {
        List<Reviewing> reviewings = new ArrayList<Reviewing>();
        reviewings.add(reviewing);
        indexReviewings(reviewings);
    }
    
    @Async
    public static void Reviewing.deleteIndex(Reviewing reviewing) {
        SolrServer solrServer = solrServer();
        try {
            solrServer.deleteById("reviewing_" + reviewing.getId());
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @PostUpdate
    @PostPersist
    private void Reviewing.postPersistOrUpdate() {
        indexReviewing(this);
    }
    
    @PreRemove
    private void Reviewing.preRemove() {
        deleteIndex(this);
    }
    
    public static SolrServer Reviewing.solrServer() {
        SolrServer _solrServer = new Reviewing().solrServer;
        if (_solrServer == null) throw new IllegalStateException("Solr server has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return _solrServer;
    }
    
}

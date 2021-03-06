// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Recommendation;
import com.agroknow.socnav.service.RecommendationServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect RecommendationServiceImpl_Roo_Service {
    
    declare @type: RecommendationServiceImpl: @Service;
    
    declare @type: RecommendationServiceImpl: @Transactional;
    
    public long RecommendationServiceImpl.countAllRecommendations() {
        return Recommendation.countRecommendations();
    }
    
    public void RecommendationServiceImpl.deleteRecommendation(Recommendation recommendation) {
        recommendation.remove();
    }
    
    public Recommendation RecommendationServiceImpl.findRecommendation(Long id) {
        return Recommendation.findRecommendation(id);
    }
    
    public List<Recommendation> RecommendationServiceImpl.findAllRecommendations() {
        return Recommendation.findAllRecommendations();
    }
    
    public List<Recommendation> RecommendationServiceImpl.findRecommendationEntries(int firstResult, int maxResults) {
        return Recommendation.findRecommendationEntries(firstResult, maxResults);
    }
    
    public void RecommendationServiceImpl.saveRecommendation(Recommendation recommendation) {
        recommendation.persist();
    }
    
    public Recommendation RecommendationServiceImpl.updateRecommendation(Recommendation recommendation) {
        return recommendation.merge();
    }
    
}

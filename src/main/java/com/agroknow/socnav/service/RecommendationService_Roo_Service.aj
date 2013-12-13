// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Recommendation;
import com.agroknow.socnav.service.RecommendationService;
import java.util.List;

privileged aspect RecommendationService_Roo_Service {
    
    public abstract long RecommendationService.countAllRecommendations();    
    public abstract void RecommendationService.deleteRecommendation(Recommendation recommendation);    
    public abstract Recommendation RecommendationService.findRecommendation(Long id);    
    public abstract List<Recommendation> RecommendationService.findAllRecommendations();    
    public abstract List<Recommendation> RecommendationService.findRecommendationEntries(int firstResult, int maxResults);    
    public abstract void RecommendationService.saveRecommendation(Recommendation recommendation);    
    public abstract Recommendation RecommendationService.updateRecommendation(Recommendation recommendation);    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Reviewing;
import com.agroknow.socnav.service.ReviewingService;
import java.util.List;

privileged aspect ReviewingService_Roo_Service {
    
    public abstract long ReviewingService.countAllReviewings();    
    public abstract void ReviewingService.deleteReviewing(Reviewing reviewing);    
    public abstract Reviewing ReviewingService.findReviewing(Long id);    
    public abstract List<Reviewing> ReviewingService.findAllReviewings();    
    public abstract List<Reviewing> ReviewingService.findReviewingEntries(int firstResult, int maxResults);    
    public abstract void ReviewingService.saveReviewing(Reviewing reviewing);    
    public abstract Reviewing ReviewingService.updateReviewing(Reviewing reviewing);    
}

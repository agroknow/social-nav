// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.Reviewing;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;
import java.util.Set;

privileged aspect User_Roo_JavaBean {
    
    public String User.getMetadata_uri() {
        return this.metadata_uri;
    }
    
    public void User.setMetadata_uri(String metadata_uri) {
        this.metadata_uri = metadata_uri;
    }
    
    public Long User.getRemote_id() {
        return this.remote_id;
    }
    
    public void User.setRemote_id(Long remote_id) {
        this.remote_id = remote_id;
    }
    
    public Set<Rating> User.getRatings() {
        return this.ratings;
    }
    
    public void User.setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }
    
    public Set<Tagging> User.getTaggings() {
        return this.taggings;
    }
    
    public void User.setTaggings(Set<Tagging> taggings) {
        this.taggings = taggings;
    }
    
    public Set<Reviewing> User.getReviewings() {
        return this.reviewings;
    }
    
    public void User.setReviewings(Set<Reviewing> reviewings) {
        this.reviewings = reviewings;
    }
    
}

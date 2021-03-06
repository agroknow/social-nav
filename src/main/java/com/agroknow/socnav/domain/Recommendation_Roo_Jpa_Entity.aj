// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Recommendation;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect Recommendation_Roo_Jpa_Entity {
    
    declare @type: Recommendation: @Entity;
    
    declare @type: Recommendation: @Table(name = "recommendations");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Recommendation.id;
    
    @Version
    @Column(name = "version")
    private Integer Recommendation.version;
    
    public Long Recommendation.getId() {
        return this.id;
    }
    
    public void Recommendation.setId(Long id) {
        this.id = id;
    }
    
    public Integer Recommendation.getVersion() {
        return this.version;
    }
    
    public void Recommendation.setVersion(Integer version) {
        this.version = version;
    }
    
}

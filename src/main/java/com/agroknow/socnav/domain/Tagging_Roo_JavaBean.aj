// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import java.util.Set;

privileged aspect Tagging_Roo_JavaBean {
    
    public Set<Tag> Tagging.getTags() {
        return this.tags;
    }
    
}
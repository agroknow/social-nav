// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Preference;
import com.agroknow.socnav.domain.Rating;
import java.util.Set;

privileged aspect Rating_Roo_JavaBean {
    
    public Set<Preference> Rating.getPreferences() {
        return this.preferences;
    }
    
    public void Rating.setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }
    
    public float Rating.getPreference_avg() {
        return this.preference_avg;
    }
    
    public int Rating.getPreference_dimensions() {
        return this.preference_dimensions;
    }
    
}

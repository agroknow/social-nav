// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.reference.LanguageType;

privileged aspect Tag_Roo_JavaBean {
    
    public String Tag.getValue() {
        return this.value;
    }
    
    public void Tag.setValue(String value) {
        this.value = value;
    }
    
    public LanguageType Tag.getLang() {
        return this.lang;
    }
    
    public void Tag.setLang(LanguageType lang) {
        this.lang = lang;
    }
    
    public Tagging Tag.getTagging() {
        return this.tagging;
    }
    
    public void Tag.setTagging(Tagging tagging) {
        this.tagging = tagging;
    }
    
}

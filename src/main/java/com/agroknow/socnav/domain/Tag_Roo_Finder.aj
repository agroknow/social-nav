// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Tag_Roo_Finder {
    
    public static TypedQuery<Tag> Tag.findTagsByTagging(Tagging tagging) {
        if (tagging == null) throw new IllegalArgumentException("The tagging argument is required");
        EntityManager em = Tag.entityManager();
        TypedQuery<Tag> q = em.createQuery("SELECT o FROM Tag AS o WHERE o.tagging = :tagging", Tag.class);
        q.setParameter("tagging", tagging);
        return q;
    }
    
    public static TypedQuery<Tag> Tag.findTagsByValueLike(String value) {
        if (value == null || value.length() == 0) throw new IllegalArgumentException("The value argument is required");
        value = value.replace('*', '%');
        if (value.charAt(0) != '%') {
            value = "%" + value;
        }
        if (value.charAt(value.length() - 1) != '%') {
            value = value + "%";
        }
        EntityManager em = Tag.entityManager();
        TypedQuery<Tag> q = em.createQuery("SELECT o FROM Tag AS o WHERE LOWER(o.value) LIKE LOWER(:value)", Tag.class);
        q.setParameter("value", value);
        return q;
    }
    
}
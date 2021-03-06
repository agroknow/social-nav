// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tagging;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Item_Roo_Finder {
    
    public static TypedQuery<Item> Item.findItemsByResource_idEquals(Long resource_id) {
        if (resource_id == null) throw new IllegalArgumentException("The resource_id argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.resource_id = :resource_id", Item.class);
        q.setParameter("resource_id", resource_id);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByResource_uriEquals(String resource_uri) {
        if (resource_uri == null || resource_uri.length() == 0) throw new IllegalArgumentException("The resource_uri argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.resource_uri = :resource_uri", Item.class);
        q.setParameter("resource_uri", resource_uri);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByTaggings(Set<Tagging> taggings) {
        if (taggings == null) throw new IllegalArgumentException("The taggings argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE");
        for (int i = 0; i < taggings.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :taggings_item").append(i).append(" MEMBER OF o.taggings");
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        int taggingsIndex = 0;
        for (Tagging _tagging: taggings) {
            q.setParameter("taggings_item" + taggingsIndex++, _tagging);
        }
        return q;
    }
    
}

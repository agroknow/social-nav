// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Recommendation;
import com.agroknow.socnav.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Recommendation_Roo_Finder {
    
    public static TypedQuery<Recommendation> Recommendation.findRecommendationsByItem(Item item) {
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = Recommendation.entityManager();
        TypedQuery<Recommendation> q = em.createQuery("SELECT o FROM Recommendation AS o WHERE o.item = :item", Recommendation.class);
        q.setParameter("item", item);
        return q;
    }
    
    public static TypedQuery<Recommendation> Recommendation.findRecommendationsByUser(User user) {
        if (user == null) throw new IllegalArgumentException("The user argument is required");
        EntityManager em = Recommendation.entityManager();
        TypedQuery<Recommendation> q = em.createQuery("SELECT o FROM Recommendation AS o WHERE o.user = :user", Recommendation.class);
        q.setParameter("user", user);
        return q;
    }
    
    public static TypedQuery<Recommendation> Recommendation.findRecommendationsByUserAndItem(User user, Item item) {
        if (user == null) throw new IllegalArgumentException("The user argument is required");
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = Recommendation.entityManager();
        TypedQuery<Recommendation> q = em.createQuery("SELECT o FROM Recommendation AS o WHERE o.user = :user AND o.item = :item", Recommendation.class);
        q.setParameter("user", user);
        q.setParameter("item", item);
        return q;
    }
    
}

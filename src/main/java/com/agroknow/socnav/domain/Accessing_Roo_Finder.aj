// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Accessing;
import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Accessing_Roo_Finder {
    
    public static TypedQuery<Accessing> Accessing.findAccessingsByItem(Item item) {
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = Accessing.entityManager();
        TypedQuery<Accessing> q = em.createQuery("SELECT o FROM Accessing AS o WHERE o.item = :item", Accessing.class);
        q.setParameter("item", item);
        return q;
    }
    
    public static TypedQuery<Accessing> Accessing.findAccessingsByUser(User user) {
        if (user == null) throw new IllegalArgumentException("The user argument is required");
        EntityManager em = Accessing.entityManager();
        TypedQuery<Accessing> q = em.createQuery("SELECT o FROM Accessing AS o WHERE o.user = :user", Accessing.class);
        q.setParameter("user", user);
        return q;
    }
    
    public static TypedQuery<Accessing> Accessing.findAccessingsByUserAndItem(User user, Item item) {
        if (user == null) throw new IllegalArgumentException("The user argument is required");
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = Accessing.entityManager();
        TypedQuery<Accessing> q = em.createQuery("SELECT o FROM Accessing AS o WHERE o.user = :user AND o.item = :item", Accessing.class);
        q.setParameter("user", user);
        q.setParameter("item", item);
        return q;
    }
    
}
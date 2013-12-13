package com.agroknow.socnav.domain;

import java.util.List;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.agroknow.socnav.reference.LanguageType;

@RooEquals
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(table = "accessings", finders = { "findAccessingsByUser", "findAccessingsByItem", "findAccessingsByUserAndItem" })
public class Accessing extends Event {
	
	//@PreUpdate
    @PrePersist
    private void beforeSave() {
    	this.beforeSaveEvent();
    }
	
	public static List<Accessing> findAccessingEntriesByUserAndItem(User user, Item item, int firstResult, int maxResults) {
        return findAccessingsByUserAndItem(user, item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Accessing> findAccessingEntriesByUser(User user, int firstResult, int maxResults) {
        return findAccessingsByUser(user).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Accessing> findAccessingEntriesByItem(Item item, int firstResult, int maxResults) {
        return findAccessingsByItem(item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}

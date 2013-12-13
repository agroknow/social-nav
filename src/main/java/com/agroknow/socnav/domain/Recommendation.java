package com.agroknow.socnav.domain;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(table = "recommendations", finders = { "findRecommendationsByUser", "findRecommendationsByItem", "findRecommendationsByUserAndItem" })
public class Recommendation {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @NotNull
    private float evaluation;
    
    public static int deleteAll(){
    	return new Recommendation().deleteAllTransactional();
    }
    
    @Transactional
    private int deleteAllTransactional() {
        return entityManager().createQuery("DELETE FROM Recommendation o").executeUpdate();
    }
    
	public static List<Recommendation> findRecommendationEntriesByUserAndItem(User user, Item item, int firstResult, int maxResults) {
        return findRecommendationsByUserAndItem(user, item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Recommendation> findRecommendationEntriesByUser(User user, int firstResult, int maxResults) {
        return findRecommendationsByUser(user).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Recommendation> findRecommendationEntriesByItem(Item item, int firstResult, int maxResults) {
        return findRecommendationsByItem(item).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
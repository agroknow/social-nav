package com.agroknow.socnav.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Preference;
import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.User;


public class RatingServiceImpl implements RatingService {
	
	private static final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

	public Rating updateRating(Rating rating) {
		//Remove old preferences for this rating
		//This cannot be done in preupdate event due to security reasons of hibernate
    	List<Preference> old_prefs = Preference.findPreferencesByRating(rating).getResultList();
    	for (Preference old_pref : old_prefs){
    		old_pref.setRating(null);
    	}
    	rating.setUser(User.getUniqueUser(rating.getUser()));
    	rating.setItem(Item.getUniqueItem(rating.getItem()));
        return rating.merge();
    }
}

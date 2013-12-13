package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Reviewing;
import com.agroknow.socnav.domain.User;


public class ReviewingServiceImpl implements ReviewingService {

	public Reviewing updateReviewing(Reviewing reviewing) {
		reviewing.setUser(User.getUniqueUser(reviewing.getUser()));
		reviewing.setItem(Item.getUniqueItem(reviewing.getItem()));
        return reviewing.merge();
    }
}

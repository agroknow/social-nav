package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Accessing;
import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.User;

public class AccessingServiceImpl implements AccessingService {

	public Accessing updateAccessing(Accessing accessing) {
		accessing.setUser(User.getUniqueUser(accessing.getUser()));
		accessing.setItem(Item.getUniqueItem(accessing.getItem()));
        return accessing.merge();
    }
}

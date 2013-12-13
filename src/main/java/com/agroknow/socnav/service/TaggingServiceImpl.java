package com.agroknow.socnav.service;

import java.util.List;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;


public class TaggingServiceImpl implements TaggingService {

	public Tagging updateTagging(Tagging tagging) {
        
		List<Tag> old_tags = Tag.findTagsByTagging(tagging).getResultList();
		for (Tag old_tag : old_tags){
			old_tag.setTagging(null);
		}
		tagging.setUser(User.getUniqueUser(tagging.getUser()));
		tagging.setItem(Item.getUniqueItem(tagging.getItem()));
		return tagging.merge();
    }
}

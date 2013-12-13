// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.web.TaggingController;
import java.util.Set;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect TaggingController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByItem", "form" }, method = RequestMethod.GET)
    public String TaggingController.findTaggingsByItemForm(Model uiModel) {
        uiModel.addAttribute("items", itemService.findAllItems());
        return "taggings/findTaggingsByItem";
    }
    
    @RequestMapping(params = "find=ByItem", method = RequestMethod.GET)
    public String TaggingController.findTaggingsByItem(@RequestParam("item") Item item, Model uiModel) {
        uiModel.addAttribute("taggings", Tagging.findTaggingsByItem(item).getResultList());
        return "taggings/list";
    }
    
    @RequestMapping(params = { "find=ByTags", "form" }, method = RequestMethod.GET)
    public String TaggingController.findTaggingsByTagsForm(Model uiModel) {
        uiModel.addAttribute("tags", tagService.findAllTags());
        return "taggings/findTaggingsByTags";
    }
    
    @RequestMapping(params = "find=ByTags", method = RequestMethod.GET)
    public String TaggingController.findTaggingsByTags(@RequestParam("tags") Set<Tag> tags, Model uiModel) {
        uiModel.addAttribute("taggings", Tagging.findTaggingsByTags(tags).getResultList());
        return "taggings/list";
    }
    
    @RequestMapping(params = { "find=ByUser", "form" }, method = RequestMethod.GET)
    public String TaggingController.findTaggingsByUserForm(Model uiModel) {
        uiModel.addAttribute("users", userService.findAllUsers());
        return "taggings/findTaggingsByUser";
    }
    
    @RequestMapping(params = "find=ByUser", method = RequestMethod.GET)
    public String TaggingController.findTaggingsByUser(@RequestParam("user") User user, Model uiModel) {
        uiModel.addAttribute("taggings", Tagging.findTaggingsByUser(user).getResultList());
        return "taggings/list";
    }
    
    @RequestMapping(params = { "find=ByUserAndItem", "form" }, method = RequestMethod.GET)
    public String TaggingController.findTaggingsByUserAndItemForm(Model uiModel) {
        uiModel.addAttribute("users", userService.findAllUsers());
        uiModel.addAttribute("items", itemService.findAllItems());
        return "taggings/findTaggingsByUserAndItem";
    }
    
    @RequestMapping(params = "find=ByUserAndItem", method = RequestMethod.GET)
    public String TaggingController.findTaggingsByUserAndItem(@RequestParam("user") User user, @RequestParam("item") Item item, Model uiModel) {
        uiModel.addAttribute("taggings", Tagging.findTaggingsByUserAndItem(user, item).getResultList());
        return "taggings/list";
    }
    
}

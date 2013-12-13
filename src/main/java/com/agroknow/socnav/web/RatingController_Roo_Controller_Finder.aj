// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.web.RatingController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect RatingController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByItem", "form" }, method = RequestMethod.GET)
    public String RatingController.findRatingsByItemForm(Model uiModel) {
        uiModel.addAttribute("items", itemService.findAllItems());
        return "ratings/findRatingsByItem";
    }
    
    @RequestMapping(params = "find=ByItem", method = RequestMethod.GET)
    public String RatingController.findRatingsByItem(@RequestParam("item") Item item, Model uiModel) {
        uiModel.addAttribute("ratings", Rating.findRatingsByItem(item).getResultList());
        return "ratings/list";
    }
    
    @RequestMapping(params = { "find=ByUser", "form" }, method = RequestMethod.GET)
    public String RatingController.findRatingsByUserForm(Model uiModel) {
        uiModel.addAttribute("users", userService.findAllUsers());
        return "ratings/findRatingsByUser";
    }
    
    @RequestMapping(params = "find=ByUser", method = RequestMethod.GET)
    public String RatingController.findRatingsByUser(@RequestParam("user") User user, Model uiModel) {
        uiModel.addAttribute("ratings", Rating.findRatingsByUser(user).getResultList());
        return "ratings/list";
    }
    
    @RequestMapping(params = { "find=ByUserAndItem", "form" }, method = RequestMethod.GET)
    public String RatingController.findRatingsByUserAndItemForm(Model uiModel) {
        uiModel.addAttribute("users", userService.findAllUsers());
        uiModel.addAttribute("items", itemService.findAllItems());
        return "ratings/findRatingsByUserAndItem";
    }
    
    @RequestMapping(params = "find=ByUserAndItem", method = RequestMethod.GET)
    public String RatingController.findRatingsByUserAndItem(@RequestParam("user") User user, @RequestParam("item") Item item, Model uiModel) {
        uiModel.addAttribute("ratings", Rating.findRatingsByUserAndItem(user, item).getResultList());
        return "ratings/list";
    }
    
}

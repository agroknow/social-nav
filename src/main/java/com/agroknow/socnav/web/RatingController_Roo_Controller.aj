// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.reference.SharingLevelType;
import com.agroknow.socnav.service.ItemService;
import com.agroknow.socnav.service.PreferenceService;
import com.agroknow.socnav.service.RatingService;
import com.agroknow.socnav.service.UserService;
import com.agroknow.socnav.web.RatingController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect RatingController_Roo_Controller {
    
    @Autowired
    RatingService RatingController.ratingService;
    
    @Autowired
    ItemService RatingController.itemService;
    
    @Autowired
    PreferenceService RatingController.preferenceService;
    
    @Autowired
    UserService RatingController.userService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String RatingController.create(@Valid Rating rating, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, rating);
            return "ratings/create";
        }
        uiModel.asMap().clear();
        ratingService.saveRating(rating);
        return "redirect:/ratings/" + encodeUrlPathSegment(rating.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String RatingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Rating());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (userService.countAllUsers() == 0) {
            dependencies.add(new String[] { "user", "users" });
        }
        if (itemService.countAllItems() == 0) {
            dependencies.add(new String[] { "item", "items" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "ratings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String RatingController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("rating", ratingService.findRating(id));
        uiModel.addAttribute("itemId", id);
        return "ratings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String RatingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ratings", ratingService.findRatingEntries(firstResult, sizeNo));
            float nrOfPages = (float) ratingService.countAllRatings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ratings", ratingService.findAllRatings());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ratings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String RatingController.update(@Valid Rating rating, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, rating);
            return "ratings/update";
        }
        uiModel.asMap().clear();
        ratingService.updateRating(rating);
        return "redirect:/ratings/" + encodeUrlPathSegment(rating.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String RatingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ratingService.findRating(id));
        return "ratings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String RatingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Rating rating = ratingService.findRating(id);
        ratingService.deleteRating(rating);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ratings";
    }
    
    void RatingController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("rating_updated_at_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void RatingController.populateEditForm(Model uiModel, Rating rating) {
        uiModel.addAttribute("rating", rating);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", itemService.findAllItems());
        uiModel.addAttribute("preferences", preferenceService.findAllPreferences());
        uiModel.addAttribute("users", userService.findAllUsers());
        uiModel.addAttribute("sharingleveltypes", Arrays.asList(SharingLevelType.values()));
    }
    
    String RatingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
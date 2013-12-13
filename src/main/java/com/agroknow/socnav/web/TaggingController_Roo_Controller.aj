// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.reference.SharingLevelType;
import com.agroknow.socnav.service.ItemService;
import com.agroknow.socnav.service.TagService;
import com.agroknow.socnav.service.TaggingService;
import com.agroknow.socnav.service.UserService;
import com.agroknow.socnav.web.TaggingController;
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

privileged aspect TaggingController_Roo_Controller {
    
    @Autowired
    TaggingService TaggingController.taggingService;
    
    @Autowired
    ItemService TaggingController.itemService;
    
    @Autowired
    TagService TaggingController.tagService;
    
    @Autowired
    UserService TaggingController.userService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TaggingController.create(@Valid Tagging tagging, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tagging);
            return "taggings/create";
        }
        uiModel.asMap().clear();
        taggingService.saveTagging(tagging);
        return "redirect:/taggings/" + encodeUrlPathSegment(tagging.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TaggingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Tagging());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (userService.countAllUsers() == 0) {
            dependencies.add(new String[] { "user", "users" });
        }
        if (itemService.countAllItems() == 0) {
            dependencies.add(new String[] { "item", "items" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "taggings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TaggingController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("tagging", taggingService.findTagging(id));
        uiModel.addAttribute("itemId", id);
        return "taggings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TaggingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("taggings", taggingService.findTaggingEntries(firstResult, sizeNo));
            float nrOfPages = (float) taggingService.countAllTaggings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("taggings", taggingService.findAllTaggings());
        }
        addDateTimeFormatPatterns(uiModel);
        return "taggings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TaggingController.update(@Valid Tagging tagging, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tagging);
            return "taggings/update";
        }
        uiModel.asMap().clear();
        taggingService.updateTagging(tagging);
        return "redirect:/taggings/" + encodeUrlPathSegment(tagging.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TaggingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, taggingService.findTagging(id));
        return "taggings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TaggingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Tagging tagging = taggingService.findTagging(id);
        taggingService.deleteTagging(tagging);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/taggings";
    }
    
    void TaggingController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("tagging_updated_at_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void TaggingController.populateEditForm(Model uiModel, Tagging tagging) {
        uiModel.addAttribute("tagging", tagging);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", itemService.findAllItems());
        uiModel.addAttribute("tags", tagService.findAllTags());
        uiModel.addAttribute("users", userService.findAllUsers());
        uiModel.addAttribute("sharingleveltypes", Arrays.asList(SharingLevelType.values()));
    }
    
    String TaggingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
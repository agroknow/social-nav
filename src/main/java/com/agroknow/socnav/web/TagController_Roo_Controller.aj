// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.reference.LanguageType;
import com.agroknow.socnav.service.TagService;
import com.agroknow.socnav.service.TaggingService;
import com.agroknow.socnav.web.TagController;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect TagController_Roo_Controller {
    
    @Autowired
    TagService TagController.tagService;
    
    @Autowired
    TaggingService TagController.taggingService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TagController.create(@Valid Tag tag, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tag);
            return "tags/create";
        }
        uiModel.asMap().clear();
        tagService.saveTag(tag);
        return "redirect:/tags/" + encodeUrlPathSegment(tag.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TagController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Tag());
        return "tags/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TagController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("tag", tagService.findTag(id));
        uiModel.addAttribute("itemId", id);
        return "tags/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TagController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("tags", tagService.findTagEntries(firstResult, sizeNo));
            float nrOfPages = (float) tagService.countAllTags() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("tags", tagService.findAllTags());
        }
        return "tags/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TagController.update(@Valid Tag tag, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tag);
            return "tags/update";
        }
        uiModel.asMap().clear();
        tagService.updateTag(tag);
        return "redirect:/tags/" + encodeUrlPathSegment(tag.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TagController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, tagService.findTag(id));
        return "tags/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TagController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Tag tag = tagService.findTag(id);
        tagService.deleteTag(tag);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/tags";
    }
    
    void TagController.populateEditForm(Model uiModel, Tag tag) {
        uiModel.addAttribute("tag", tag);
        uiModel.addAttribute("taggings", taggingService.findAllTaggings());
        uiModel.addAttribute("languagetypes", Arrays.asList(LanguageType.values()));
    }
    
    String TagController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Accessing;
import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.reference.SharingLevelType;
import com.agroknow.socnav.service.AccessingService;
import com.agroknow.socnav.service.ItemService;
import com.agroknow.socnav.service.UserService;
import com.agroknow.socnav.web.AccessingController;
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

privileged aspect AccessingController_Roo_Controller {
    
    @Autowired
    AccessingService AccessingController.accessingService;
    
    @Autowired
    ItemService AccessingController.itemService;
    
    @Autowired
    UserService AccessingController.userService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String AccessingController.create(@Valid Accessing accessing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, accessing);
            return "accessings/create";
        }
        uiModel.asMap().clear();
        accessingService.saveAccessing(accessing);
        return "redirect:/accessings/" + encodeUrlPathSegment(accessing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String AccessingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Accessing());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (userService.countAllUsers() == 0) {
            dependencies.add(new String[] { "user", "users" });
        }
        if (itemService.countAllItems() == 0) {
            dependencies.add(new String[] { "item", "items" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "accessings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String AccessingController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("accessing", accessingService.findAccessing(id));
        uiModel.addAttribute("itemId", id);
        return "accessings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String AccessingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("accessings", accessingService.findAccessingEntries(firstResult, sizeNo));
            float nrOfPages = (float) accessingService.countAllAccessings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("accessings", accessingService.findAllAccessings());
        }
        addDateTimeFormatPatterns(uiModel);
        return "accessings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String AccessingController.update(@Valid Accessing accessing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, accessing);
            return "accessings/update";
        }
        uiModel.asMap().clear();
        accessingService.updateAccessing(accessing);
        return "redirect:/accessings/" + encodeUrlPathSegment(accessing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String AccessingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, accessingService.findAccessing(id));
        return "accessings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String AccessingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Accessing accessing = accessingService.findAccessing(id);
        accessingService.deleteAccessing(accessing);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/accessings";
    }
    
    void AccessingController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("accessing_updated_at_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void AccessingController.populateEditForm(Model uiModel, Accessing accessing) {
        uiModel.addAttribute("accessing", accessing);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", itemService.findAllItems());
        uiModel.addAttribute("users", userService.findAllUsers());
        uiModel.addAttribute("sharingleveltypes", Arrays.asList(SharingLevelType.values()));
    }
    
    String AccessingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
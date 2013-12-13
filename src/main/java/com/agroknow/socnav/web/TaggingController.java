package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/taggings")
@Controller
@RooWebScaffold(path = "taggings", formBackingObject = Tagging.class)
@RooWebJson(jsonObject = Tagging.class)
@RooWebFinder
public class TaggingController {

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Tagging tagging = Tagging.fromJsonToTagging(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (tagging.getTags().isEmpty()) {
            return new ResponseEntity<String>("Tags cannot be empty", headers, HttpStatus.OK);
        } else {
            taggingService.saveTagging(tagging);
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(params = "item_resource_uri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindTaggingsByItem(@RequestParam("item_resource_uri") String item_resource_uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
            item = Item.findItemsByResource_uriEquals(item_resource_uri).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingsByItem(item).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "user_remote_id", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindTaggingsByUser(@RequestParam("user_remote_id") Long user_remote_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
            user = User.findUsersByRemote_idEquals(user_remote_id).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingsByUser(user).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = { "item_resource_uri", "user_remote_id" }, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindTaggingsByUserAndItem(@RequestParam("user_remote_id") Long user_remote_id, @RequestParam("item_resource_uri") String item_resource_uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        Item item;
        try {
            user = User.findUsersByRemote_idEquals(user_remote_id).getSingleResult();
            item = Item.findItemsByResource_uriEquals(item_resource_uri).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such user or item", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingsByUserAndItem(user, item).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/reindex", produces = "text/html")
    @ResponseBody
    public String jsonIndexTaggings() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        Tagging.indexTaggings(Tagging.findAllTaggings());
        return "OK";
    }
}

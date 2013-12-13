package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Reviewing;
import com.agroknow.socnav.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/reviewings")
@Controller
@RooWebScaffold(path = "reviewings", formBackingObject = Reviewing.class)
@RooWebJson(jsonObject = Reviewing.class)
@RooWebFinder
public class ReviewingController {

    @RequestMapping(params = "item_resource_uri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindReviewingsByItem(@RequestParam("item_resource_uri") String item_resource_uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
            item = Item.findItemsByResource_uriEquals(item_resource_uri).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingsByItem(item).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "user_remote_id", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindReviewingsByUser(@RequestParam("user_remote_id") Long user_remote_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
            user = User.findUsersByRemote_idEquals(user_remote_id).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingsByUser(user).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = { "user_remote_id", "item_resource_uri" }, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindReviewingsByUserAndItem(@RequestParam("user_remote_id") Long user_remote_id, @RequestParam("item_resource_uri") String item_resource_uri) {
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
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingsByUserAndItem(user, item).getResultList()), headers, HttpStatus.OK);
    }
}

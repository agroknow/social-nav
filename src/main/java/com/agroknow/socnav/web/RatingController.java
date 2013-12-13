package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ratings")
@Controller
@RooWebScaffold(path = "ratings", formBackingObject = Rating.class)
@RooWebJson(jsonObject = Rating.class)
@RooWebFinder
public class RatingController {

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Rating rating = Rating.fromJsonToRating(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (rating.getPreferences().isEmpty()) {
            return new ResponseEntity<String>("Preferences cannot be empty", headers, HttpStatus.OK);
        } else {
            ratingService.saveRating(rating);
            return new ResponseEntity<String>("{id:" + rating.getId() + "}", headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(params = "item_resource_uri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindRatingsByItem(@RequestParam("item_resource_uri") String item_resource_uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
            item = Item.findItemsByResource_uriEquals(item_resource_uri).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Rating.toJsonArray(Rating.findRatingsByItem(item).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "user_remote_id", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindRatingsByUser(@RequestParam("user_remote_id") Long user_remote_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
            user = User.findUsersByRemote_idEquals(user_remote_id).getSingleResult();
        } catch (Exception e) {
            return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Rating.toJsonArray(Rating.findRatingsByUser(user).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = { "user_remote_id", "item_resource_uri" }, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindRatingsByUserAndItem(@RequestParam("user_remote_id") Long user_remote_id, @RequestParam("item_resource_uri") String item_resource_uri) {
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
        return new ResponseEntity<String>(Rating.toJsonArray(Rating.findRatingsByUserAndItem(user, item).getResultList()), headers, HttpStatus.OK);
    }
}

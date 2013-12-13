package com.agroknow.socnav.api;

import java.util.List;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Recommendation;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/recommendations")
//@RooWebJson(jsonObject = Recommendation.class)
public class RecommendationApiController {
	
	@Autowired
    RecommendationService recommendationService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Recommendation> result = recommendationService.findRecommendationEntries(start, max);
        return new ResponseEntity<String>(Recommendation.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/deleteAll", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> deleteAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        int result = Recommendation.deleteAll();
        return new ResponseEntity<String>("Result: "+result, headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Recommendation recommendation = recommendationService.findRecommendation(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (recommendation == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(recommendation.toJson(), headers, HttpStatus.OK);
    }
	
	@RequestMapping(params = "itemResourceUri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindRecommendationsByItem(
    		@RequestParam("itemResourceUri") String itemResourceUri,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
			item = Item.findItemsByResource_uriEquals(itemResourceUri).getSingleResult();
		} catch (Exception e) {
	        return new ResponseEntity<String>("{error:'No such item'}", headers, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<String>(Recommendation.toJsonArray(Recommendation.findRecommendationEntriesByItem(item, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "userRemoteId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindRecommendationsByUser(
    		@RequestParam("userRemoteId") Long userRemoteId,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
			user = User.findUsersByRemote_idEquals(userRemoteId).getSingleResult();
		} catch (Exception e) {
	        return new ResponseEntity<String>("{error:'No such user'}", headers, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<String>(Recommendation.toJsonArray(Recommendation.findRecommendationEntriesByUser(user, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = {"userRemoteId", "itemResourceUri"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindRecommendationsByUserAndItem(
    		@RequestParam("userRemoteId") Long userRemoteId, 
    		@RequestParam("itemResourceUri") String itemResourceUri,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        Item item;
        try{
        	user = User.findUsersByRemote_idEquals(userRemoteId).getSingleResult();
        	item = Item.findItemsByResource_uriEquals(itemResourceUri).getSingleResult();
        }catch (Exception e) {
        	return new ResponseEntity<String>("{error:'No such user or item'}", headers, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<String>(Recommendation.toJsonArray(Recommendation.findRecommendationEntriesByUserAndItem(user, item, start, max)), headers, HttpStatus.OK);
    }
}

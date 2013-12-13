package com.agroknow.socnav.api;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Reviewing;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.ReviewingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//@RooWebJson(jsonObject = Reviewing.class)
@Controller
@RequestMapping("/api/reviewings")
public class ReviewingApiController {
	
	@Autowired
    ReviewingService reviewingService;
	
	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Reviewing> result = reviewingService.findReviewingEntries(start, max);
        return new ResponseEntity<String>(Reviewing.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Reviewing reviewing = reviewingService.findReviewing(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (reviewing == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(reviewing.toJson(), headers, HttpStatus.OK);
    }
	
	@RequestMapping(params = "itemResourceUri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindReviewingsByItem(
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
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingEntriesByItem(item, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "userRemoteId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindReviewingsByUser(
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
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingEntriesByUser(user, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = {"userRemoteId", "itemResourceUri"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindReviewingsByUserAndItem(
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
        return new ResponseEntity<String>(Reviewing.toJsonArray(Reviewing.findReviewingEntriesByUserAndItem(user, item, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Reviewing reviewing = Reviewing.fromJsonToReviewing(json);
        reviewingService.saveReviewing(reviewing);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(reviewing.toJson(),headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Reviewing reviewing = Reviewing.fromJsonToReviewing(json);
        if (reviewingService.updateReviewing(reviewing) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Reviewing reviewing = reviewingService.findReviewing(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (reviewing == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        reviewingService.deleteReviewing(reviewing);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	/*@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Reviewing reviewing: Reviewing.fromJsonArrayToReviewings(json)) {
            reviewingService.saveReviewing(reviewing);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }*/

	/*@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Reviewing reviewing: Reviewing.fromJsonArrayToReviewings(json)) {
            if (reviewingService.updateReviewing(reviewing) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}

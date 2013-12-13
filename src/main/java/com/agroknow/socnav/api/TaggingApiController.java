package com.agroknow.socnav.api;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.TaggingService;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RooWebJson(jsonObject = Tagging.class)
@Controller
@RequestMapping("/api/taggings")
public class TaggingApiController {
	
	private static final Logger log = LoggerFactory.getLogger(TaggingApiController.class);

	@Autowired
    TaggingService taggingService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Tagging> result = taggingService.findAllTaggings();
        return new ResponseEntity<String>(Tagging.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Tagging tagging = taggingService.findTagging(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (tagging == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(tagging.toJson(), headers, HttpStatus.OK);
    }
	
	@RequestMapping(params = "tag", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTaggingsByTags(
    		@RequestParam("tag") String tagValue,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        log.debug("tag value: "+ tagValue);
        List<Tag> tagList;
        tagList = Tag.findTagsByValueLike(tagValue).getResultList();
      	if (tagList.isEmpty()){
      		return new ResponseEntity<String>("{\"error\":\"No such tag value\"}", headers, HttpStatus.NOT_FOUND);
      	} else {
      		return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingEntriesByTags(tagList, start, max)), headers, HttpStatus.OK);
      	}
    }
	
	@RequestMapping(params = "itemResourceUri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTaggingsByItem(
    		@RequestParam("itemResourceUri") String itemResourceUri,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
			item = Item.findItemsByResource_uriEquals(itemResourceUri).getSingleResult();
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"error\":\"No such item\"}", headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingEntriesByItem(item, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "userRemoteId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTaggingsByUser(
    		@RequestParam("userRemoteId") Long userRemoteId,
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
			user = User.findUsersByRemote_idEquals(userRemoteId).getSingleResult();
		} catch (Exception e) {
	        return new ResponseEntity<String>("{\"error\":\"No such user\"}", headers, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingEntriesByUser(user, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = {"itemResourceUri", "userRemoteId"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTaggingsByUserAndItem(
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
        	return new ResponseEntity<String>("{\"error\":\"No such user or item\"}", headers, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<String>(Tagging.toJsonArray(Tagging.findTaggingEntriesByUserAndItem(user, item, start, max)), headers, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Tagging tagging = Tagging.fromJsonToTagging(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (tagging.getTags().isEmpty()){
        	return new ResponseEntity<String>("Tags cannot be empty", headers, HttpStatus.OK);
        }else {
        	taggingService.saveTagging(tagging);
        	return new ResponseEntity<String>(tagging.toJson(),headers, HttpStatus.CREATED);
        }
    }
	
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Tagging tagging = Tagging.fromJsonToTagging(json);
        if (taggingService.updateTagging(tagging) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Tagging tagging = taggingService.findTagging(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (tagging == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        taggingService.deleteTagging(tagging);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Tagging tagging: Tagging.fromJsonArrayToTaggings(json)) {
            taggingService.saveTagging(tagging);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Tagging tagging: Tagging.fromJsonArrayToTaggings(json)) {
            if (taggingService.updateTagging(tagging) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}

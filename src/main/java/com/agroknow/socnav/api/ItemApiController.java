package com.agroknow.socnav.api;

import java.util.List;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.reference.LanguageType;
import com.agroknow.socnav.service.ItemService;
import com.agroknow.socnav.service.PreferenceService;

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

//@RooWebJson(jsonObject = Item.class)
@Controller
@RequestMapping("/api/items")
public class ItemApiController {
	
	private static final Logger log = LoggerFactory.getLogger(ItemApiController.class);
	
	@Autowired
    ItemService itemService;
	
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Item> result = itemService.findItemEntries(start, max);
        return new ResponseEntity<String>(Item.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, params = {"resourceUri"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(
    		@RequestParam(value = "resourceUri", required = true) String resourceUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        log.debug("resourceUri: "+resourceUri);
        try {
			item = Item.findItemsByResource_uriEquals(resourceUri).getSingleResult();
			return new ResponseEntity<String>(item.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
		}
    }
    
    @RequestMapping(method = RequestMethod.GET, params = {"resourceId"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJsonByResourceId(
    		@RequestParam(value = "resourceId", required = true) Long resourceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        log.debug("resourceUri: "+resourceId);
        try {
			item = Item.findItemsByResource_idEquals(resourceId).getSingleResult();
			return new ResponseEntity<String>(item.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
		}
    }
    
    @RequestMapping(method = RequestMethod.GET, params = {"tag","lang"}, headers = "Accept=application/json")
    @ResponseBody
	public ResponseEntity<String> jsonFindItemsByTagEquals(
			@RequestParam("tag") String tagValue,
			@RequestParam(value = "lang", defaultValue= "en") LanguageType lang) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Item> result = Item.findItemByTag(tagValue, lang);
        return new ResponseEntity<String>(Item.toJsonArray(result), headers, HttpStatus.OK);
	}
    
    @RequestMapping(method = RequestMethod.GET, params = {"popular"}, headers = "Accept=application/json")
    @ResponseBody
	public ResponseEntity<String> jsonFindPopularItems(
			@RequestParam("popular") Boolean popular,
			@RequestParam(value="max", defaultValue="5") int max) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Item> result = Item.findPopularItems(max);
        return new ResponseEntity<String>(Item.toJsonArray(result), headers, HttpStatus.OK);
	}
    /*@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Item item = itemService.findItem(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (item == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(item.toJson(), headers, HttpStatus.OK);
    }
        
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Item item: Item.fromJsonArrayToItems(json)) {
            itemService.saveItem(item);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Item item = Item.fromJsonToItem(json);
        if (itemService.updateItem(item) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Item item: Item.fromJsonArrayToItems(json)) {
            if (itemService.updateItem(item) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Item item = itemService.findItem(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (item == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itemService.deleteItem(item);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, params = "resource_uri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItemsByResource_uriEquals(@RequestParam("resource_uri") String resource_uri) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Item item;
        try {
        	log.debug("foo");
        	log.debug(resource_uri);
			item = Item.findItemsByResource_uriEquals(resource_uri).getSingleResult();
			return new ResponseEntity<String>(item.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.debug(e.getMessage());
			return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
		}
    }
	
	@RequestMapping(method = RequestMethod.GET, params = {"tag_value", "tag_lang"}, headers = "Accept=application/json")
    @ResponseBody
	public ResponseEntity<String> jsonFindItemsByTagEquals(@RequestParam("tag_value") String tag_value, @RequestParam("tag_lang") LanguageType tag_lang) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Item> result = Item.findItemByTag(tag_value, tag_lang);
        return new ResponseEntity<String>(Item.toJsonArray(result), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Item item = Item.fromJsonToItem(json);
        itemService.saveItem(item);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }*/
}

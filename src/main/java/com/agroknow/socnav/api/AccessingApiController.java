package com.agroknow.socnav.api;

import com.agroknow.socnav.domain.Accessing;
import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.AccessingService;
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

//@RooWebJson(jsonObject = Accessing.class)
@Controller
@RequestMapping("/api/accessings")
public class AccessingApiController {

	@Autowired
    AccessingService accessingService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Accessing> result = accessingService.findAccessingEntries(start, max);
        return new ResponseEntity<String>(Accessing.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Accessing accessing = accessingService.findAccessing(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (accessing == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(accessing.toJson(), headers, HttpStatus.OK);
    }
	
	@RequestMapping(params = "itemResourceUri", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAccessingsByItem(
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
        return new ResponseEntity<String>(Accessing.toJsonArray(Accessing.findAccessingEntriesByItem(item, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "userRemoteId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAccessingsByUser(
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
        return new ResponseEntity<String>(Accessing.toJsonArray(Accessing.findAccessingEntriesByUser(user, start, max)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = {"userRemoteId", "itemResourceUri"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAccessingsByUserAndItem(
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
        return new ResponseEntity<String>(Accessing.toJsonArray(Accessing.findAccessingEntriesByUserAndItem(user, item, start, max)), headers, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Accessing accessing = Accessing.fromJsonToAccessing(json);
        accessingService.saveAccessing(accessing);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Accessing accessing = Accessing.fromJsonToAccessing(json);
        if (accessingService.updateAccessing(accessing) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Accessing accessing = accessingService.findAccessing(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (accessing == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        accessingService.deleteAccessing(accessing);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	/*@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Accessing accessing: Accessing.fromJsonArrayToAccessings(json)) {
            accessingService.saveAccessing(accessing);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Accessing accessing: Accessing.fromJsonArrayToAccessings(json)) {
            if (accessingService.updateAccessing(accessing) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}

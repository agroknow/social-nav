package com.agroknow.socnav.api;

import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = User.class)
@Controller
@RequestMapping("/api/users")
public class UserApiController {

	@Autowired
    UserService userService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="start", defaultValue="0") int start, 
	    	@RequestParam(value="max", defaultValue="5") int max) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<User> result = userService.findUserEntries(start, max);
        return new ResponseEntity<String>(User.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(params = {"remoteId"}, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(
    		@RequestParam(value = "remoteId", required = true) Long remoteId,
//    		@RequestParam(value = "withEvents", defaultValue= "false") Boolean withEvents,
    		@RequestParam(value = "withTags", defaultValue= "false") Boolean withTags) {
//        User user = userService.findUser(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
        	boolean withEvents = false;
			user = User.findUsersByRemote_idEquals(remoteId).getSingleResult();
			return new ResponseEntity<String>(user.toJson(withEvents,withTags), headers, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
		}
    }
	
	/*@RequestMapping(params = "find=ByRemote_idEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindUsersByRemote_idEquals(@RequestParam("remote_id") Long remote_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(User.toJsonArray(User.findUsersByRemote_idEquals(remote_id).getResultList()), headers, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        User user = User.fromJsonToUser(json);
        userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (User user: User.fromJsonArrayToUsers(json)) {
            userService.saveUser(user);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        User user = userService.findUser(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (user == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(user);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        User user = User.fromJsonToUser(json);
        if (userService.updateUser(user) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (User user: User.fromJsonArrayToUsers(json)) {
            if (userService.updateUser(user) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(params = "remote_id", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindUsersByRemote_idEquals(
    		@RequestParam("remote_id") Long remote_id, 
    		@RequestParam(value = "with_events", defaultValue= "false") Boolean with_events) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
			user = User.findUsersByRemote_idEquals(remote_id).getSingleResult();
			return new ResponseEntity<String>(user.toJson(with_events,false), headers, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
		}
    }*/

}

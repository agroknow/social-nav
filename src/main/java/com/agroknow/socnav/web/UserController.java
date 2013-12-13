package com.agroknow.socnav.web;

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

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = User.class)
@RooWebFinder
@RooWebJson(jsonObject = User.class)
public class UserController {

    @RequestMapping(params = "remote_id", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindUsersByRemote_idEquals(@RequestParam("remote_id") Long remote_id, @RequestParam(value = "with_events", defaultValue = "false") Boolean with_events) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user;
        try {
            user = User.findUsersByRemote_idEquals(remote_id).getSingleResult();
            return new ResponseEntity<String>(user.toJson(with_events, false), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<String>("No such user", headers, HttpStatus.NOT_FOUND);
        }
    }
}

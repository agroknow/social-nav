// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.web.ItemController;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect ItemController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItemController.showJson(@PathVariable("id") Long id) {
        Item item = itemService.findItem(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (item == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(item.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItemController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Item> result = itemService.findAllItems();
        return new ResponseEntity<String>(Item.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItemController.createFromJsonArray(@RequestBody String json) {
        for (Item item: Item.fromJsonArrayToItems(json)) {
            itemService.saveItem(item);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItemController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Item item = Item.fromJsonToItem(json);
        if (itemService.updateItem(item) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItemController.updateFromJsonArray(@RequestBody String json) {
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
    public ResponseEntity<String> ItemController.deleteFromJson(@PathVariable("id") Long id) {
        Item item = itemService.findItem(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (item == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itemService.deleteItem(item);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByResource_idEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItemController.jsonFindItemsByResource_idEquals(@RequestParam("resource_id") Long resource_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Item.toJsonArray(Item.findItemsByResource_idEquals(resource_id).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByTaggings", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItemController.jsonFindItemsByTaggings(@RequestParam("taggings") Set<Tagging> taggings) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Item.toJsonArray(Item.findItemsByTaggings(taggings).getResultList()), headers, HttpStatus.OK);
    }
    
}

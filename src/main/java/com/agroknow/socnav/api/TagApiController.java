package com.agroknow.socnav.api;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.Tagging;
import com.agroknow.socnav.reference.LanguageType;
import com.agroknow.socnav.service.TagService;

import java.util.Collection;
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

//@RooWebJson(jsonObject = Tag.class)
@Controller
@RequestMapping("/api/tags")
public class TagApiController {
	
	@Autowired
    TagService tagService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
    		@RequestParam(value="max", defaultValue="5") int max,
    		@RequestParam(value = "lang", required = true) LanguageType lang,
    		@RequestParam(value = "resourceUri", required = false) String resourceUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Tag> result = null;
        if (resourceUri != null){
        	try {
            	Item item = Item.findItemsByResource_uriEquals(resourceUri).getSingleResult();
            	result = Tag.fetchTagsAsFacets(max, lang, item.getId());
            } catch  (Exception e) {
    			return new ResponseEntity<String>("No such item", headers, HttpStatus.NOT_FOUND);
    		}
        } else {
        	result = Tag.fetchTagsAsFacets(max, lang);
        }
		return new ResponseEntity<String>(Tag.toJsonArray(result), headers, HttpStatus.OK);
    }

	/*@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Tag tag = tagService.findTag(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (tag == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(tag.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Tag tag = Tag.fromJsonToTag(json);
        if (tagService.updateTag(tag) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Tag tag: Tag.fromJsonArrayToTags(json)) {
            if (tagService.updateTag(tag) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByTagging", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTagsByTagging(@RequestParam("tagging") Tagging tagging) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Tag.toJsonArray(Tag.findTagsByTagging(tagging).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTagsByValueLike(@RequestParam("value") String value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Tag.toJsonArray(Tag.findTagsByValueLike(value).getResultList()), headers, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Tag tag = Tag.fromJsonToTag(json);
        tagService.saveTag(tag);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Tag tag: Tag.fromJsonArrayToTags(json)) {
            tagService.saveTag(tag);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Tag tag = tagService.findTag(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (tag == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        tagService.deleteTag(tag);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tag;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Tag_Roo_Json {
    
    public static Tag Tag.fromJsonToTag(String json) {
        return new JSONDeserializer<Tag>().use(null, Tag.class).deserialize(json);
    }
    
    public static Collection<Tag> Tag.fromJsonArrayToTags(String json) {
        return new JSONDeserializer<List<Tag>>().use(null, ArrayList.class).use("values", Tag.class).deserialize(json);
    }
    
}
// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Reviewing;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Reviewing_Roo_Json {
    
    public static Reviewing Reviewing.fromJsonToReviewing(String json) {
        return new JSONDeserializer<Reviewing>().use(null, Reviewing.class).deserialize(json);
    }
    
    public static Collection<Reviewing> Reviewing.fromJsonArrayToReviewings(String json) {
        return new JSONDeserializer<List<Reviewing>>().use(null, ArrayList.class).use("values", Reviewing.class).deserialize(json);
    }
    
}
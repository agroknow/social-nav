// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Tag;
import com.agroknow.socnav.domain.TagDataOnDemand;
import com.agroknow.socnav.domain.TaggingDataOnDemand;
import com.agroknow.socnav.reference.LanguageType;
import com.agroknow.socnav.service.TagService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect TagDataOnDemand_Roo_DataOnDemand {
    
    declare @type: TagDataOnDemand: @Component;
    
    private Random TagDataOnDemand.rnd = new SecureRandom();
    
    private List<Tag> TagDataOnDemand.data;
    
    @Autowired
    TaggingDataOnDemand TagDataOnDemand.taggingDataOnDemand;
    
    @Autowired
    TagService TagDataOnDemand.tagService;
    
    public Tag TagDataOnDemand.getNewTransientTag(int index) {
        Tag obj = new Tag();
        setFrequency(obj, index);
        setLang(obj, index);
        setValue(obj, index);
        return obj;
    }
    
    public void TagDataOnDemand.setFrequency(Tag obj, int index) {
        Long frequency = new Integer(index).longValue();
        obj.setFrequency(frequency);
    }
    
    public void TagDataOnDemand.setLang(Tag obj, int index) {
        LanguageType lang = LanguageType.class.getEnumConstants()[0];
        obj.setLang(lang);
    }
    
    public void TagDataOnDemand.setValue(Tag obj, int index) {
        String value = "value_" + index;
        if (value.length() > 25) {
            value = value.substring(0, 25);
        }
        obj.setValue(value);
    }
    
    public Tag TagDataOnDemand.getSpecificTag(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Tag obj = data.get(index);
        Long id = obj.getId();
        return tagService.findTag(id);
    }
    
    public Tag TagDataOnDemand.getRandomTag() {
        init();
        Tag obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return tagService.findTag(id);
    }
    
    public boolean TagDataOnDemand.modifyTag(Tag obj) {
        return false;
    }
    
    public void TagDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = tagService.findTagEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Tag' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Tag>();
        for (int i = 0; i < 10; i++) {
            Tag obj = getNewTransientTag(i);
            try {
                tagService.saveTag(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}

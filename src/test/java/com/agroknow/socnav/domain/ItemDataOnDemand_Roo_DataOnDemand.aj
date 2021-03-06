// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.ItemDataOnDemand;
import com.agroknow.socnav.service.ItemService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect ItemDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ItemDataOnDemand: @Component;
    
    private Random ItemDataOnDemand.rnd = new SecureRandom();
    
    private List<Item> ItemDataOnDemand.data;
    
    @Autowired
    ItemService ItemDataOnDemand.itemService;
    
    public Item ItemDataOnDemand.getNewTransientItem(int index) {
        Item obj = new Item();
        setMetadata_uri(obj, index);
        setResource_id(obj, index);
        setResource_uri(obj, index);
        return obj;
    }
    
    public void ItemDataOnDemand.setMetadata_uri(Item obj, int index) {
        String metadata_uri = "metadata_uri_" + index;
        obj.setMetadata_uri(metadata_uri);
    }
    
    public void ItemDataOnDemand.setResource_id(Item obj, int index) {
        Long resource_id = new Integer(index).longValue();
        obj.setResource_id(resource_id);
    }
    
    public void ItemDataOnDemand.setResource_uri(Item obj, int index) {
        String resource_uri = "resource_uri_" + index;
        obj.setResource_uri(resource_uri);
    }
    
    public Item ItemDataOnDemand.getSpecificItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Item obj = data.get(index);
        Long id = obj.getId();
        return itemService.findItem(id);
    }
    
    public Item ItemDataOnDemand.getRandomItem() {
        init();
        Item obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return itemService.findItem(id);
    }
    
    public boolean ItemDataOnDemand.modifyItem(Item obj) {
        return false;
    }
    
    public void ItemDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = itemService.findItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Item' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Item>();
        for (int i = 0; i < 10; i++) {
            Item obj = getNewTransientItem(i);
            try {
                itemService.saveItem(obj);
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

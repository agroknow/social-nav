// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.service;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.service.ItemServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemServiceImpl_Roo_Service {
    
    declare @type: ItemServiceImpl: @Service;
    
    declare @type: ItemServiceImpl: @Transactional;
    
    public long ItemServiceImpl.countAllItems() {
        return Item.countItems();
    }
    
    public void ItemServiceImpl.deleteItem(Item item) {
        item.remove();
    }
    
    public Item ItemServiceImpl.findItem(Long id) {
        return Item.findItem(id);
    }
    
    public List<Item> ItemServiceImpl.findAllItems() {
        return Item.findAllItems();
    }
    
    public List<Item> ItemServiceImpl.findItemEntries(int firstResult, int maxResults) {
        return Item.findItemEntries(firstResult, maxResults);
    }
    
    public void ItemServiceImpl.saveItem(Item item) {
        item.persist();
    }
    
    public Item ItemServiceImpl.updateItem(Item item) {
        return item.merge();
    }
    
}

package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.DuplicatedFoundItemException;
import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;
import org.springframework.stereotype.Service;

import javax.swing.tree.ExpandVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    Item[] items;

    public ItemService(ItemRepository itemRepository, Item[] items) {
        this.itemRepository = itemRepository;
        this.items = items;
    }

    public List<Item> updateQuality() {
        var itemsList = itemRepository.findAll();
        var items = itemsList.toArray(new Item[itemsList.size()]);

        for (int i = 0; i < items.length; i++) {
            if (!items[i].type.equals(Item.Type.AGED)
                    && !items[i].type.equals(Item.Type.TICKETS)) {
                if (items[i].quality > 0) {
                    if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                        items[i].quality = items[i].quality - 1;
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].type.equals(Item.Type.TICKETS)) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].type.equals(Item.Type.AGED)) {
                    if (!items[i].type.equals(Item.Type.TICKETS)) {
                        if (items[i].quality > 0) {
                            if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
            itemRepository.save(items[i]);
        }
        return Arrays.asList(items);
    }

    public Item createItem(Item item) {

        try{
            List<Item> currentItems = itemRepository.findAll();
            checkDuplicatedItem(item, currentItems);
            return itemRepository.save(item);
        } catch (DuplicatedFoundItemException e){
            throw e;
        }

    }

    public List<Item> createItems(List<Item> items){
        try{
            checkDuplicatedItems(items);
            return itemRepository.saveAll(items);
        } catch (DuplicatedFoundItemException e){
            throw e;
        }
    }

    public void checkDuplicatedItems(List<Item> items){
        List<Item> currentItems = itemRepository.findAll();
        for (Item item: items){
            checkDuplicatedItem(item, currentItems);
            currentItems.add(item);
        }
    }

    public Item updateItem(int id, Item item) {
        try{
            findById(id);
            return itemRepository.save(new Item(id, item.name, item.sellIn, item.quality, item.type));
        }catch(ResourceNotFoundException e) {
            throw e;
        }
    }

    public List<Item> listItems(){
        return itemRepository.findAll();
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(""));
    }

    public void checkDuplicatedItem(Item item, List<Item> currentItems){

        for (Item currentItem:currentItems){
            Boolean duplicatedName = currentItem.name.equals(item.name);
            Boolean duplicatedSellIn = currentItem.sellIn == item.sellIn;
            Boolean duplicatedQuality = currentItem.quality == item.quality;
            Boolean duplicatedType = currentItem.type == item.type;
            if(duplicatedName && duplicatedSellIn && duplicatedQuality && duplicatedType){
                    throw new DuplicatedFoundItemException("");
            }
        }
    }
}
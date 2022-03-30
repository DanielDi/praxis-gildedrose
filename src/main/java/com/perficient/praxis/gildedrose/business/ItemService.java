package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.DuplicatedFoundItemException;
import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;
import org.springframework.stereotype.Service;

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

    // In this service there are only the methods that correspond to the CRUD of an item
    // CRUD:
    // Create (single and batch items)
    // Read (list items)
    // Update
    // Delete (Not implemented)

    public Item createItem(Item item) {

        try {
            List<Item> currentItems = itemRepository.findAll();
            isDuplicatedItem(item, currentItems);
            return itemRepository.save(item);
        } catch (DuplicatedFoundItemException e) {
            throw e;
        }

    }

    public List<Item> createItems(List<Item> items) {
        try {
            checkDuplicatedItems(items);
            return itemRepository.saveAll(items);
        } catch (DuplicatedFoundItemException e) {
            throw e;
        }
    }

    public void checkDuplicatedItems(List<Item> items) {
        List<Item> currentItems = itemRepository.findAll();
        for (Item item : items) {
            isDuplicatedItem(item, currentItems);
            currentItems.add(item);
        }
    }

    public Item updateItem(int id, Item item) {
        try {
            findById(id);
            return itemRepository.save(new Item(id, item.name, item.sellIn, item.quality, item.type));
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("The item with the id " + id + " was not found"));
    }

    public void isDuplicatedItem(Item item, List<Item> currentItems) {

        for (Item currentItem : currentItems) {

            Boolean isDuplicatedName = currentItem.name.equals(item.name);
            Boolean isDuplicatedSellIn = currentItem.sellIn == item.sellIn;
            Boolean isDuplicatedQuality = currentItem.quality == item.quality;
            Boolean isDuplicatedType = currentItem.type == item.type;

            if (isDuplicatedName && isDuplicatedSellIn && isDuplicatedQuality && isDuplicatedType) {
                throw new DuplicatedFoundItemException("An item with the inserted attributes already exists. ");
            }
        }
    }
}
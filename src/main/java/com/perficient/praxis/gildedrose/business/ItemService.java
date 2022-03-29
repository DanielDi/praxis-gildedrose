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

    //TODO
    // Create an abstract item class
    // according type, create a specific class
    // redefine the method UpdateQuality in each specific class
    // this idea is for prevent the Switch

    public List<Item> updateQuality() {
        var itemsList = itemRepository.findAll();
        var items = itemsList.toArray(new Item[itemsList.size()]);

        for (int i = 0; i < items.length; i++) {
            switch (items[i].type) {
                case NORMAL:
                    updateQualityNormalTypeItem(items[i]);
                    break;
                case AGED:
                    updateQualityAgedTypeItem(items[i]);
                    break;
                case TICKETS:
                    updateQualityTicketsTypeItem(items[i]);
                    break;
                case LEGENDARY:
                    updateQualityLegendaryTypeItem(items[i]);
                    break;
                default:
                    throw new ResourceNotFoundException("type: " + items[i].type + " is not found");
            }
            itemRepository.save(items[i]);
        }
        return Arrays.asList(items);
    }

    public Item reduceQuality(Item item) {
        if (item.quality > 0) {
            item.quality -= 1;
        }
        return item;
    }

    public Item reduceSellIn(Item item) {
        item.sellIn -= 1;
        return item;
    }

    public Item increaseQuality(Item item) {
        if (item.quality < 50) {
            item.quality += 1;
        }
        return item;
    }

    public Item updateQualityNormalTypeItem(Item item) {
        reduceQuality(item);
        reduceSellIn(item);

        // reduce the quality one more time when SellIn is 0 or less
        if (item.sellIn < 0) {
            reduceQuality(item);
        }
        return item;
    }

    public Item updateQualityAgedTypeItem(Item item) {
        increaseQuality(item);
        reduceSellIn(item);

        // increase the quality one more time when SellIn is 0 or less
        if (item.sellIn < 0) {
            increaseQuality(item);
        }
        return item;
    }

    public Item updateQualityTicketsTypeItem(Item item) {

        increaseQuality(item);

        if (item.sellIn <= 5) {
            increaseQuality(item);
            increaseQuality(item);
        } else if (item.sellIn <= 10) {
            increaseQuality(item);
        }

        reduceSellIn(item);

        if (item.sellIn < 0) {
            item.quality = 0;
        }

        return item;
    }

    public Item updateQualityLegendaryTypeItem(Item item) {
        return item;
    }

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
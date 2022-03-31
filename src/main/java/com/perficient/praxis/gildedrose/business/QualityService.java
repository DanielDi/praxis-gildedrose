package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class QualityService {

    private final ItemRepository itemRepository;

    Item[] items;

    public Integer minimumQuality = 0;

    public Integer maximumQuality = 50;

    public Integer sellInDatePassed = 0;

    public Integer sellInDateNear = 5;

    public Integer sellInDateVeryNear = 10;

    public QualityService(ItemRepository itemRepository, Item[] items) {
        this.itemRepository = itemRepository;
        this.items = items;
    }

    public List<Item> updateQuality() {
        var itemsList = itemRepository.findAll();
        var items = itemsList.toArray(new Item[itemsList.size()]);

        for (Item item : itemsList) {
            switch (item.type) {
                case NORMAL:
                    updateQualityNormalTypeItem(item);
                    break;
                case AGED:
                    updateQualityAgedTypeItem(item);
                    break;
                case TICKETS:
                    updateQualityTicketsTypeItem(item);
                    break;
                case LEGENDARY:
                    updateQualityLegendaryTypeItem(item);
                    break;
                }
            itemRepository.save(item);
        }
        return Arrays.asList(items);
    }

    public Item reduceQuality(Item item) {
        if (item.quality > minimumQuality) {
            item.quality -= 1;
        }
        return item;
    }

    public Item reduceSellIn(Item item) {
        item.sellIn -= 1;
        return item;
    }

    public Item increaseQuality(Item item) {
        if (item.quality < maximumQuality) {
            item.quality += 1;
        }
        return item;
    }

    public Item updateQualityNormalTypeItem(Item item) {
        item = reduceQuality(item);
        item = reduceSellIn(item);

        // reduce the quality one more time when SellIn is 0 or less
        if (item.sellIn < 0) {
            item = reduceQuality(item);
        }
        return item;
    }

    public Item updateQualityAgedTypeItem(Item item) {
        item = increaseQuality(item);
        item = reduceSellIn(item);

        // increase the quality one more time when SellIn is 0 or less
        if (item.sellIn < sellInDatePassed) {
            item = increaseQuality(item);
        }
        return item;
    }

    public Item updateQualityTicketsTypeItem(Item item) {

        increaseQuality(item);

        // increases the item quality based on SellIn
        if (item.sellIn <= sellInDateNear) {
            increaseQuality(item);
            increaseQuality(item);
        } else if (item.sellIn <= sellInDateVeryNear) {
            increaseQuality(item);
        }

        reduceSellIn(item);

        if (item.sellIn < sellInDatePassed) {
            item.quality = 0;
        }

        return item;
    }

    public Item updateQualityLegendaryTypeItem(Item item) {
        return item;
    }

}

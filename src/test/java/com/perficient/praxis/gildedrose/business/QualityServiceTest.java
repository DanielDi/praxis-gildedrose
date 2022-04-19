package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.DuplicatedFoundItemException;
import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QualityServiceTest {
    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private QualityService qualityService;

//    @Test
    /**
     * GIVEN a valid aged type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 1
     */
//    public void testUpdateQualityOfNullTypeItem() {
//
//        var item = new Item(0, "Cheese", 2, 30, null);
//        when(itemRepository.findAll()).thenReturn(List.of(item));
//
//        assertThrows(ResourceNotFoundException.class, () -> {qualityService.updateQuality();});
//
//    }

    @Test
    /**
     * GIVEN a valid aged type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 1
     */
    public void testUpdateQualityOfAgedTypeItem() {

        var item = new Item(0, "Cheese", 2, 30, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0 , itemsUpdated.get(0).getId());
        assertEquals("Cheese" , itemsUpdated.get(0).name);
        assertEquals(1 , itemsUpdated.get(0).sellIn);
        assertEquals(31 , itemsUpdated.get(0).quality);
        assertEquals(item.type , itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid aged type item when sellIn less than 0 in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 2
     */
    public void testUpdateQualityOfAgedTypeItemWhenSellInLessThanZero() {

        var item = new Item(0, "Wine", -1, 0, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Wine", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(2, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid aged type item when sellIn less than 0 and Quality equal to 50 in the database
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn value,
     * sellIn will be decreased by 1
     */
    public void testUpdateQualityOfAgedTypeItemWhenSellInLessThanZeroAndQualityEqualFifty() {

        var item = new Item(0, "Beer", -1, 50, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Beer", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid normal type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * both will be decreased by 1
     */
    public void testUpdateQualityOfNormalTypeItem() {

        var item = new Item(0, "Oreo", 10, 2, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Oreo", itemsUpdated.get(0).name);
        assertEquals(9, itemsUpdated.get(0).sellIn);
        assertEquals(1, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid normal type item with quality less than zero in the database
     * WHEN updateQuality method is called
     * THEN the service should update only sellIn values will be decreased by 1
     */
    public void testUpdateQualityOfNormalTypeItemWhenQualityEqualZero() {

        var item = new Item(0, "strawberry", 10, 0, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("strawberry", itemsUpdated.get(0).name);
        assertEquals(9, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid normal type item when SellIn equal to 0 in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * SellIn will be decreased by 1 and Quality by 2
     */
    public void testUpdateQualityOfNormalTypeItemWhenSellInEqualZero() {

        var item = new Item(0, "Eggs", 0, 30, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Eggs", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(28, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid legendary type item in the database
     * WHEN updateQuality method is called
     * THEN the service don't should update the quality and sellIn values.
     */
    public void testUpdateQualityOfLegendaryTypeItem() {

        var item = new Item(0, "Butter", 20, 30, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Butter", itemsUpdated.get(0).name);
        assertEquals(20, itemsUpdated.get(0).sellIn);
        assertEquals(30, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid legendary type item when SellIn less than 0 in the database
     * WHEN updateQuality method is called
     * THEN the service don't should update the quality and sellIn values.
     */
    public void testUpdateQualityOfLegendaryTypeItemWhenSellInLessThanZero() {

        var item = new Item(0, "Almonds", -1, 4, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Almonds", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(4, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid legendary type item when Quality equal to 0 in the database
     * WHEN updateQuality method is called
     * THEN the service don't should update the quality and sellIn values
     */
    public void testUpdateQualityOfLegendaryTypeItemWhenQualityEqualZero() {

        var item = new Item(0, "Water", -1, 0, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Water", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid tickets type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 3 because SellIn is less than 5
     */
    public void testUpdateQualityOfTicketsTypeItem() {

        var item = new Item(0,"movie ticket", 7, 47, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("movie ticket", itemsUpdated.get(0).name);
        assertEquals(6, itemsUpdated.get(0).sellIn);
        assertEquals(49, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid tickets type item when sellIn equal to 0 in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be updated to 0
     */
    public void testUpdateQualityOfTicketsTypeItemWhenSellInEqualZero() {

        var item = new Item(0, "Don Tetto concert", 0, 30, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Don Tetto concert", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid tickets type item when high quality in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 1 because can't increments greater than 50
     */
    public void testUpdateQualityOfTicketsTypeItemWhenQualityCloseToFifty() {

        var item = new Item(0,"shakira concert tickets", 2, 49, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("shakira concert tickets", itemsUpdated.get(0).name);
        assertEquals(1, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    /**
     * GIVEN a valid tickets type item when SellIn greater eleven than in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented only by 1
     */
    public void testUpdateQualityOfTicketsTypeItemWhenSellInGreaterThanEleven() {

        var item = new Item(0,"The Beatles concert tickets", 15, 47, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("The Beatles concert tickets", itemsUpdated.get(0).name);
        assertEquals(14, itemsUpdated.get(0).sellIn);
        assertEquals(48, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }
}

package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.DuplicatedFoundItemException;
import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;

import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


    @Test
    /**
     * GIVEN the request to find a item with a id that doesn´t exist in the database
     * WHEN findById method is called
     * THEN must be thrown a exception of item not found
     */
    public void testGetItemByIdWhenItemWasNotFound() {

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.findById(0));
    }

    @Test
    /**
     * GIVEN the request to find a item with a id that exists in the database
     * WHEN findById method is called
     * THEN the service should return the item with that id
     */
    public void testGetItemByIdSuccess() {

        var item = new Item(0, "Oreo", 10, 30, Item.Type.NORMAL);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        Item itemFound = itemService.findById(0);

        assertEquals(item, itemFound);
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

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Oreo", itemsUpdated.get(0).name);
        assertEquals(9, itemsUpdated.get(0).sellIn);
        assertEquals(1, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateQualityOfLessThanZeroQualityItem() {

        var item = new Item(0, "strawberry", 10, 0, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("strawberry", itemsUpdated.get(0).name);
        assertEquals(9, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateQualityOfAgedTypeItem() {

        var item = new Item(0, "Cheese", 2, 30, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Cheese", itemsUpdated.get(0).name);
        assertEquals(1, itemsUpdated.get(0).sellIn);
        assertEquals(31, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfTicketsTypeItem() {

        var item = new Item(0,"movie ticket", 2, 47, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("movie ticket", itemsUpdated.get(0).name);
        assertEquals(1, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfTicketsTypeItemWithQualityGreaterThanFiftyForAdd() {

        var item = new Item(0,"shakira concert tickets", 2, 49, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("shakira concert tickets", itemsUpdated.get(0).name);
        assertEquals(1, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfTicketsTypeItemWithQualityGreaterThanFiftySinceStart() {

        var item = new Item(0,"Saw III movie tickets", 15, 50, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Saw III movie tickets", itemsUpdated.get(0).name);
        assertEquals(14, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfTicketsTypeItemWithSellInGreaterThanEleven() {

        var item = new Item(0,"The Beatles concert tickets", 15, 49, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("The Beatles concert tickets", itemsUpdated.get(0).name);
        assertEquals(14, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfLegendaryTypeItem() {

        var item = new Item(0, "Butter", 20, 30, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Butter", itemsUpdated.get(0).name);
        assertEquals(20, itemsUpdated.get(0).sellIn);
        assertEquals(30, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateQualityOfLegendaryTypeItemWithSellinLessThanZero() {

        var item = new Item(0, "Almonds", -1, 4, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Almonds", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(4, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfAgedTypeItemWithSellinLessThanZero() {

        var item = new Item(0, "Wine", -1, 0, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Wine", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(2, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfAgedTypeItemWithSellinLessThanZeroAndQualityFifty() {

        var item = new Item(0, "Beer", -1, 50, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Beer", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfLegendaryTypeItemWithQualityEqualZero() {

        var item = new Item(0, "Water", -1, 0, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Water", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfNormalTypeItemWithSellinEqualZero() {

        var item = new Item(0, "Eggs", 0, 30, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Eggs", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(28, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
    }

    @Test
    public void testUpdateQualityOfTicketTypeItemWithSellinEqualZero() {

        var item = new Item(0, "Don Tetto concert", 0, 30, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Don Tetto concert", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
    }


    @Test
    /**
     * GIVEN the request to update any attribute of a nonexistent object in the database
     * WHEN updateItem method is called
     * THEN should be thrown an item not found exception
     */
    public void testUpdateItemByIdWhenItemWasNotFound() {

        var item = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        itemRepository.save(item);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.updateItem(6, item));
    }

    @Test
    /**
     * GIVEN the request to update any attribute of a existent object in the database
     * WHEN updateItem method is called
     * THEN the repository should update the changed attributes
     */
    public void testUpdateItemByIdWhenItemWasFound() {

        var item = new Item(5, "Cookie", 10, 30, Item.Type.TICKETS);
        var newItem = new Item(5, "Chocolate cookie", 9, 32, Item.Type.NORMAL);
        when(itemRepository.findById(5)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(newItem);

        Item updatedItem = itemService.updateItem(5,newItem);

        assertEquals(5, updatedItem.getId());
        assertEquals("Chocolate cookie", updatedItem.name);
        assertEquals(9, updatedItem.sellIn);
        assertEquals(32, updatedItem.quality);
        assertEquals(Item.Type.NORMAL,updatedItem.type);
    }

    @Test
    /**
     * GIVEN the request to show all items in the database
     * WHEN listItems method is called
     * THEN the repository should return all the items in the database
     */
    public void testListItems() {

        var item = new Item(5, "Cookie", 10, 30, Item.Type.TICKETS);
        var item2 = new Item(5, "Chocolate cookie", 9, 32, Item.Type.NORMAL);
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> finalList = itemService.listItems();

        assertEquals(items , finalList);
    }

    @Test
    /**
     * GIVEN a new item that doesn't exist in database
     * WHEN createItem method is called
     * THEN the service should save successfully the item in the database
     */
    public void testCreateItemSuccess(){

        var item = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item createdItem = itemService.createItem(item);

        assertEquals(item, createdItem);
    }

//    @Test
//    /**
//     * GIVEN a new item that alredy exist in database
//     * WHEN createItem method is called
//     * THEN the service should throw an exception of duplicated item
//     */
//    public void testCreateDuplicatedItem(){
//
//        var item = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
//        itemRepository.save(item);
//        var itemToInsert = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
//
//        assertThrows(DuplicatedFoundItemException.class, () -> {itemService.createItem(itemToInsert);});
//    }

    @Test
    /**
     * GIVEN a batch items with one or more different attributes
     * WHEN createItems method is called
     * THEN the service should save all the items in the database
     */
    public void testCreateItemsSuccess() throws Exception{

        var item1 = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        var item2 = new Item(6, "Flower", 8, 15, Item.Type.AGED);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        when(itemRepository.saveAll(items)).thenReturn(items);

        try {
            List<Item> createdItems = itemService.createItems(items);
            assertEquals(createdItems, items);
        } catch (DuplicatedFoundItemException e){
            throw e;
        }

    }

    @Test
    /**
     * GIVEN a batch of items where one or more have equal attributes to another item in database
     * WHEN createItems method is called
     * THEN the service throws an exception of duplicated items
     */
    public void testCreateDuplicatedItems(){
        var item1 = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        var item2 = new Item(6, "Cookie", 10, 30, Item.Type.NORMAL);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        assertThrows(DuplicatedFoundItemException.class, () -> {itemService.createItems(items);});
    }

}
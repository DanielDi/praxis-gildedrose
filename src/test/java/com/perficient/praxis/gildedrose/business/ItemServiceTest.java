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
     * GIVEN the request to find an item with an id that exists in the database
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
     * GIVEN the request to find an item with an id that doesn´t exist in the database
     * WHEN findById method is called
     * THEN must be thrown an exception to item not found
     */
    public void testGetItemByIdWhenItemWasNotFound() {

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.findById(0));
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


    @Test
    /**
     * GIVEN a batch items with one or more different attributes
     * WHEN createItems method is called
     * THEN the service should save all the items in the database
     */
    public void testCreateItemsSuccess() {

        var item1 = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        var item2 = new Item(6, "Ticket", 30, 20, Item.Type.LEGENDARY);
        List<Item> itemsBD = new ArrayList<>();
        itemsBD.add(item1);
        itemsBD.add(item2);

        var itemDifferent = new Item(8, "The Batman", 3, 6, Item.Type.TICKETS);
        var itemWith1Difference = new Item(7, "Cookie", 10, 3, Item.Type.NORMAL);
        var itemWith2Difference = new Item(7, "Cookie", 10, 30, Item.Type.AGED);
        var itemWith3Difference = new Item(7, "Cookie", 5, 2, Item.Type.AGED);

        List<Item> itemsToInsert = new ArrayList<>();

        itemsToInsert.add(itemDifferent);
        itemsToInsert.add(itemWith1Difference);
        itemsToInsert.add(itemWith2Difference);
        itemsToInsert.add(itemWith3Difference);

        when(itemRepository.findAll()).thenReturn(itemsBD);
        when(itemRepository.saveAll(itemsToInsert)).thenReturn(itemsToInsert);

        List<Item> createdItems = itemService.createItems(itemsToInsert);
        assertEquals(createdItems, itemsToInsert);
    }

    @Test
    /**
     * GIVEN a new item that already exist in database
     * WHEN createItem method is called
     * THEN the service should throw an exception of duplicated item
     */
    public void testCreateDuplicatedItem(){

        var item1 = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        var item2 = new Item(6, "Ticket", 20, 15, Item.Type.LEGENDARY);
        List<Item> itemsBD = new ArrayList<>();
        itemsBD.add(item1);
        itemsBD.add(item2);

        var itemToInsert = new Item(7, "Cookie", 10, 30, Item.Type.NORMAL);

        when(itemRepository.findAll()).thenReturn(itemsBD);

        assertThrows(DuplicatedFoundItemException.class, () -> {itemService.createItem(itemToInsert);});
    }

    @Test
    /**
     * GIVEN a batch of items where one or more have equal attributes to another item in database
     * WHEN createItems method is called
     * THEN the service throws an exception of duplicated items
     */
    public void testCreateDuplicatedItems(){
        var item1 = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);
        var item2 = new Item(6, "Ticket", 30, 20, Item.Type.LEGENDARY);
        List<Item> itemsBD = new ArrayList<>();
        itemsBD.add(item1);
        itemsBD.add(item2);

        var item3 = new Item(7, "Cookie", 10, 30, Item.Type.NORMAL);
        var item4 = new Item(8, "Cookie", 10, 30, Item.Type.NORMAL);
        List<Item> itemsToInsert = new ArrayList<>();
        itemsToInsert.add(item3);
        itemsToInsert.add(item4);

        when(itemRepository.findAll()).thenReturn(itemsBD);

        assertThrows(DuplicatedFoundItemException.class, () -> {itemService.createItems(itemsToInsert);});
    }

    @Test
    /**
     * GIVEN an item existing int the database
     * WHEN deleteItem method is called
     * THEN the itemService must delete and return the item found by its id
     */
    public void testDeleteExistingItem(){
        var itemToDelete = new Item(5, "Cookie", 10, 30, Item.Type.NORMAL);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(itemToDelete));

        Item deletedItem = itemService.deleteItem(itemToDelete.getId());

        assertEquals(deletedItem, itemToDelete);
    }

    @Test
    /**
     * GIVEN a nonexistent item in the database
     * WHEN deleteItem method is call
     * THEN the itemService must throws a ResourceNotFoundException
     */
    public void testDeleteNonexistentItem(){

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.deleteItem(6));
    }
}
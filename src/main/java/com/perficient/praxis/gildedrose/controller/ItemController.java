package com.perficient.praxis.gildedrose.controller;

import com.perficient.praxis.gildedrose.business.ItemService;
import com.perficient.praxis.gildedrose.business.QualityService;
import com.perficient.praxis.gildedrose.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/items")
public class ItemController {

    private final ItemService itemService;
    private final QualityService qualityService;

    public ItemController(ItemService itemService, QualityService qualityService) {

        this.itemService = itemService;
        this.qualityService = qualityService;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable int id){
        Item deleteItem = itemService.deleteItem(id);
        return new ResponseEntity<>(deleteItem, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Item>> listItems(){
        var items = itemService.listItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable int id){
        var item = itemService.findById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item){
        Item createdItem = itemService.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PostMapping("/createItems")
    public ResponseEntity<List<Item>> createItems(@RequestBody List<Item> items){
        List<Item> createdItems = itemService.createItems(items);
        return new ResponseEntity<>(createdItems, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable int id,
                                           @RequestBody Item item){
        Item createdItem = itemService.updateItem(id, item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PostMapping("/quality")
    public ResponseEntity<List<Item>> updateItemsQuality(){
        var items = qualityService.updateQuality();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}

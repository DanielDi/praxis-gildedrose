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
public class UnitTestFailTest {
    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private UnitTestFail unitTestFail;


     @Test
    
    //  * GIVEN a valid aged type item in the database
    //  * WHEN updateQuality method is called
    //  * THEN the service should update the quality and sellIn values,
    //  * sellIn will be decreased by 1
    //  *   quality will be incremented by 1
     
    public void testUnitTestFail() {

        var item = new Item(0, "Name", 2, 30, Item.Type.AGED);

        assertEquals("Name" , item[0]);
    }
}
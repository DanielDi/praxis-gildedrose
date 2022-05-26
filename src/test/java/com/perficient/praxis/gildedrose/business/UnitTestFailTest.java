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
    private UnitTestFail qualityService;


    @Test
    /**
     * GIVEN a valid aged type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * sellIn will be decreased by 1
     * quality will be incremented by 1
     */
    public void testUpdateQualityOfAgedTypeItem() {

        var item = new Item(0, "Name", 2, 30, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = qualityService.updateQuality();

        assertEquals(0 , itemsUpdated.get(0).getId());
        assertEquals("Distinct Name" , itemsUpdated.get(0).name);
        assertEquals(1 , itemsUpdated.get(0).sellIn);
        assertEquals(31 , itemsUpdated.get(0).quality);
        assertEquals(item.type , itemsUpdated.get(0).type);
    }
}
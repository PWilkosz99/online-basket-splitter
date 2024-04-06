package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {
    private final String absoluteConfigPath = new File("config/config.json").getAbsolutePath();
    private final String absoluteTestCasePath1 = new File("src/test/java/com/ocado/basket/basket-1.json").getAbsolutePath();
    private final String absoluteTestCasePath2 = new File("src/test/java/com/ocado/basket/basket-2.json").getAbsolutePath();

    @Test
    public void testSplitWithValidConfigTestCase1() {
        BasketSplitter splitter = new BasketSplitter(absoluteConfigPath);
        var items = importItemsFromJsonFile(absoluteTestCasePath1);

        Map<String, List<String>> result = splitter.split(items);

        assertEquals(2, result.size());
        assertEquals(Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"), result.get("Courier"));
        assertEquals(List.of("Fond - Chocolate"), result.get("Pick-up point"));
    }

    @Test
    public void testSplitWithValidConfigTestCase2() {
        BasketSplitter splitter = new BasketSplitter(absoluteConfigPath);
        var items = importItemsFromJsonFile(absoluteTestCasePath2);

        Map<String, List<String>> result = splitter.split(items);

        assertEquals(3, result.size());
        assertEquals(Arrays.asList("Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen", "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Apples - Spartan", "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea"
), result.get("Express Collection"));
        assertEquals(Arrays.asList("Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled"), result.get("Same day delivery"));
        assertEquals(List.of("Cake - Miini Cheesecake Cherry"), result.get("Courier"));
    }

    @Test
    void testSplitWithInvalidItem() {
        BasketSplitter splitter = new BasketSplitter(absoluteConfigPath);
        List<String> items = Arrays.asList("apple", "banana");

        assertThrows(IllegalArgumentException.class, () -> splitter.split(items));
    }

    @Test
    void testSplitWithEmptyItemList() {
        BasketSplitter splitter = new BasketSplitter(absoluteConfigPath);
        List<String> items = Arrays.asList();

        Map<String, List<String>> result = splitter.split(items);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSplitWithInvalidConfig() {
        assertThrows(RuntimeException.class, () -> new BasketSplitter("invalid_config.json"));
    }

    public static List<String> importItemsFromJsonFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> items = null;

        try {
            items = mapper.readValue(new File(filePath), new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
}
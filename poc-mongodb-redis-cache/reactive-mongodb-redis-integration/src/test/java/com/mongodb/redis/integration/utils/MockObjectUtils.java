package com.mongodb.redis.integration.utils;

import com.mongodb.redis.integration.document.Item;

import java.util.List;

public class MockObjectUtils {

  public static List<Item> getItemsList() {
    return List.of(
        new Item(null, "Samsung TV", 40000.0),
        new Item(null, "LG TV", 42000.0),
        new Item(null, "Apple Watch", 90000.99),
        getItemById("ABC"));
  }

  public static Item getItemById(String id) {
    return new Item(id, "Bose Headphone", 900.99);
  }
}

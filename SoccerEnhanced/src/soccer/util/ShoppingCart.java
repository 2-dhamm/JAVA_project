package soccer.util;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<String, Integer> items = new HashMap<>();

    public void addItem(String itemName, int quantity) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        items.put(itemName, quantity);
        System.out.println("🛒 Added: " + itemName + " x" + quantity);
    }
}
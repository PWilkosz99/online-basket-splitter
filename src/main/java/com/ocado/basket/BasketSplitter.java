package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class BasketSplitter {
    private final Map<String, List<String>> deliveryGroups;
    public BasketSplitter(String absolutePathToConfigFile) {
        deliveryGroups = parseConfigFile(absolutePathToConfigFile);
    }

    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> result = new HashMap<>();
        Map<String, List<String>> deliveryPossibilities = new HashMap<>();

        // Assign products to possible delivery methods
        for (String item : items) {
            var availableDeliveryMethods = deliveryGroups.get(item);
            if (availableDeliveryMethods == null) {
                throw new IllegalArgumentException("No delivery methods defined for item: " + item);
            }
            for (var deliveryMethod : availableDeliveryMethods) {
                deliveryPossibilities.computeIfAbsent(deliveryMethod, k -> new ArrayList<>()).add(item);
            }
        }

        // Split products into delivery groups
        while (!deliveryPossibilities.isEmpty()) {
            List<Map.Entry<String, List<String>>> sortedDeliveryGroups = new ArrayList<>(deliveryPossibilities.entrySet());
            sortedDeliveryGroups.sort(Comparator.comparingInt(e -> -e.getValue().size()));

            // Select the largest delivery group
            Map.Entry<String, List<String>> largestDeliveryGroup = sortedDeliveryGroups.getFirst();
            String deliveryMethod = largestDeliveryGroup.getKey();
            List<String> deliveredProducts = new ArrayList<>(largestDeliveryGroup.getValue());
            result.put(deliveryMethod, new ArrayList<>(deliveredProducts));

            // Remove delivered products from remaining groups
            for (List<String> itemsByMethod : deliveryPossibilities.values()) {
                itemsByMethod.removeAll(deliveredProducts);
            }

            // Remove empty delivery groups
            deliveryPossibilities.values().removeIf(List::isEmpty);
        }

        return result;
    }

    private Map<String, List<String>> parseConfigFile(String absolutePathToConfigFile) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> configMap = null;

        try {
            JsonNode root = mapper.readTree(new File(absolutePathToConfigFile));
            configMap = mapper.convertValue(root, new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error parsing configuration file", e);
        }

        return configMap;
    }
}
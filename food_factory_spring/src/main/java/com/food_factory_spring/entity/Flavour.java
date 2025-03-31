package com.food_factory_spring.entity;

import lombok.Data;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Flavour {
    private Map<String, List<FlavourIngredients>> flavours = new HashMap<>();

    public void addIngredient(FlavourIngredients ingredient) {
        String flavourName = ingredient.getFlavourName();
        if (!flavours.containsKey(flavourName)) {
            flavours.put(flavourName, new ArrayList<>());
        }
        flavours.get(flavourName).add(ingredient);
    }

    public Map<String, List<FlavourIngredients>> getFlavours() {
        return flavours;
    }

    // 添加排序方法
    public void sortIngredients(String flavourName, Comparator<FlavourIngredients> comparator) {
        List<FlavourIngredients> ingredients = flavours.get(flavourName);
        if (ingredients != null) {
            ingredients.sort(comparator);
        }
    }
    // 新增：对风味名称进行排序
    public Map<String, List<FlavourIngredients>> getSortedFlavours() {
        return flavours.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

}

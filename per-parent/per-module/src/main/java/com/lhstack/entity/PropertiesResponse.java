package com.lhstack.entity;

import java.util.HashMap;
import java.util.Map;

public class PropertiesResponse<K,V> {

    private Map<K,V> map = new HashMap<>();

    public PropertiesResponse<K,V> put(K key, V value) {
        map.put(key,value);
        return this;
    }

    public Map<K, V> getMap() {
        return map;
    }
}

package de.uniwue.dmir.heatmap.util;

import java.util.Map;

public class MapUtil {
	
	public static <TKey, TValue> TValue getAndSet(
			Map<TKey, TValue> map, 
			TKey key,
			TValue defaultValue) {
		
		TValue value = map.get(key);
		if (value == null) {
			value = defaultValue;
			map.put(key, defaultValue);
		}
		
		return value;
	}
	
	public static <TKey, TValue> TValue get(
			Map<TKey, TValue> map, 
			TKey key,
			TValue defaultValue) {
		
		TValue value = map.get(key);
		if (value == null) {
			value = defaultValue;
		}
		
		return value;
	}
}

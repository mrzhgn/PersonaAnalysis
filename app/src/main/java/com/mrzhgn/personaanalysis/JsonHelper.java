package com.mrzhgn.personaanalysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static Map<String, Object> decodeMap(Map<String, Object> encodedMap) {
        try {
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, Object> pair : encodedMap.entrySet()) {
                byte bytes[] = pair.getKey().getBytes("UTF-8");
                String newKey = new String(bytes, "UTF-8").toLowerCase().replaceAll("\\s+", "");
                Object newValue = pair.getValue();
                if (pair.getValue() instanceof String) {
                    bytes = pair.getValue().toString().getBytes("UTF-8");
                    newValue = new String(bytes, "UTF-8");
                }
                if (pair.getValue() instanceof ArrayList) {
                    newValue = decodeList((ArrayList) pair.getValue());
                }
                if (pair.getValue() instanceof HashMap) {
                    newValue = decodeMap((HashMap) pair.getValue());
                }
                result.put(newKey, newValue);
            }
            return result;
        } catch (Exception e) {
            return encodedMap;
        }
    }

    public static List<Object> decodeList(List<Object> encodedList) {
        try {
            List<Object> result = new ArrayList<>();
            for (Object obj : encodedList) {
                Object newObj = obj;
                if (obj instanceof HashMap) {
                    newObj = decodeMap((HashMap) obj);
                }
                if (obj instanceof ArrayList) {
                    newObj = decodeList((ArrayList) obj);
                }
                if (obj instanceof String) {
                    byte[] bytes = ((String) obj).getBytes("UTF-8");
                    newObj = new String(bytes, "UTF-8");
                }
                result.add(newObj);
            }
            return result;
        } catch (Exception e) {
            return encodedList;
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static ArrayList<Object> toList(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}

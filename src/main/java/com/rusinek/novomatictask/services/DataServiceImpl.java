package com.rusinek.novomatictask.services;

import com.rusinek.novomatictask.model.Statistics;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
@Service
public class DataServiceImpl implements DataService {

    @Override
    public List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> summarize(List<String> languages, List<String> size) {
        Map<String, String> map = new LinkedHashMap<>();

        for (int i = 0; i < languages.size(); i++) {
            String temp = languages.get(i);
            if (map.keySet().contains(temp)) {
                Integer value = Integer.valueOf(map.get(temp));
                map.put(temp, String.valueOf(value + Integer.valueOf(size.get(i))));
            } else {
                map.put(languages.get(i), size.get(i));
            }
        }
        return map;
    }
}

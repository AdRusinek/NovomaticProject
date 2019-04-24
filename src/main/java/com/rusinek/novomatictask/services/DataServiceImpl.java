package com.rusinek.novomatictask.services;

import com.rusinek.novomatictask.exceptions.ResourceNotFoundException;
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

import static com.rusinek.novomatictask.model.ConnectionData.*;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
@Service
public class DataServiceImpl implements DataService {

    @Override
    public Statistics authenticate(String user) {

        RestTemplate restTemplate = new RestTemplate();
        String completeUrl = GIT_URL + user + "/repos";

        if (user.equals(GIT_USERNAME)) {
            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(GIT_USERNAME, GIT_PASSWORD));
        }
        try {
            String text = restTemplate.getForObject(completeUrl, String.class);
            int numberOfRepos = restTemplate.getForObject(completeUrl, Object[].class).length;

            return new Statistics(numberOfRepos, summarize(getValuesForGivenKey(text, "language"),
                    getValuesForGivenKey(text, "size")));
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User not found");
        }
    }

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

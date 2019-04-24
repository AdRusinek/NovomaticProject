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

    /**
     * This method is used for fetching data and possibly authenticate with my own GitGub account.
     *
     * @param user This parameter stands for username to use inside url for fetching correct data
     * @return number of repositories, languages and size corresponding for every language
     */
    @Override
    public Statistics authenticate(String user) {

        RestTemplate restTemplate = new RestTemplate();
        String completeUrl = GIT_URL + user + "/repos";

        /* Here I tried to avoid deprecated class BasicAuthorizationInterceptor */

//        HttpHeaders httpHeaders = restTemplate.headForHeaders(completeUrl);
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        String token = Base64Utils.encodeToString(
//                (GIT_USERNAME + ":" + GIT_PASSWORD).getBytes(StandardCharsets.UTF_8));
//        headers.add("Authorization", "Basic " + token);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//        ResponseEntity template = restTemplate.exchange(completeUrl, HttpMethod.POST, entity, String.class);


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

    /**
     * This method collect values corresponded with specific key
     *
     * @param jsonArrayStr This is for making JSONArray from this String parameter
     * @param key          String param for finding correct values
     * @return list with correct values for a given key
     */
    @Override
    public List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }

    /**
     * @param languages takes list of found languages
     * @param sizes     takes list with sizes for specific languages
     * @return a map with parsed languages corresponding with their size
     */
    @Override
    public Map<String, String> summarize(List<String> languages, List<String> sizes) {
        Map<String, String> map = new LinkedHashMap<>();

        for (int i = 0; i < languages.size(); i++) {
            String temp = languages.get(i);
            if (map.keySet().contains(temp)) {
                Integer value = Integer.valueOf(map.get(temp));
                map.put(temp, String.valueOf(value + Integer.valueOf(sizes.get(i))));
            } else {
                map.put(languages.get(i), sizes.get(i));
            }
        }
        return map;
    }
}

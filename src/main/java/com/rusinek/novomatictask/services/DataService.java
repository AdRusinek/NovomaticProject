package com.rusinek.novomatictask.services;

import com.rusinek.novomatictask.model.Statistics;

import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
public interface DataService {

    Statistics authenticate(String user);

    List<String> getValuesForGivenKey(String jsonArrayStr, String key);

    Map<String, String> summarize(List<String> languages, List<String> size);
}

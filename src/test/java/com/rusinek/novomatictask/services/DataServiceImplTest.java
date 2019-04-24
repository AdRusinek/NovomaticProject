package com.rusinek.novomatictask.services;

import com.rusinek.novomatictask.model.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rusinek.novomatictask.model.ConnectionData.GIT_USERNAME;
import static org.junit.Assert.*;

public class DataServiceImplTest {

    public static final String URL = "https://api.github.com/users/" + GIT_USERNAME + "/repos";
    public static final int NUM_OF_REPOS = 12;
    public static final int NUM_OF_LANGUAGES = 3;

    DataServiceImpl dataService;

    @Before
    public void setUp() throws Exception {
        dataService = new DataServiceImpl();
    }


    @Test
    public void authenticate() throws Exception {

        Statistics statistics = dataService.authenticate(GIT_USERNAME);

        assertEquals(NUM_OF_REPOS, statistics.getRepositoriesCount());
        assertEquals(NUM_OF_LANGUAGES, statistics.getLanguages().size());
    }

    @Test
    public void getValuesForGivenKey() {

        RestTemplate restTemplate = new RestTemplate();

        String text = restTemplate.getForObject(URL, String.class);

        List<String> strings = dataService.getValuesForGivenKey(text, "size");

        assertNotNull(strings);
        assertEquals(NUM_OF_REPOS, strings.size());
    }

    @Test
    public void summarize() {

        List<String> names = new ArrayList<>();
        names.add("John Thompson");
        names.add("John Thompson");
        names.add("Barbara Liskov");
        names.add("Barbara Liskov");
        names.add("Ken Kousen");
        names.add("Barbara Liskov");
        names.add("Ken Kousen");
        names.add("Herbert Schildt");
        names.add("Herbert Schildt");

        assertNotNull(names);

        List<String> values = new ArrayList<>();
        values.add("2");
        values.add("2");
        values.add("3");
        values.add("3");
        values.add("4");
        values.add("1");
        values.add("5");
        values.add("1");
        values.add("2");

        assertNotNull(values);

        Map<String, String> map = dataService.summarize(names, values);

        assertEquals("4", map.get("John Thompson"));
        assertEquals("7", map.get("Barbara Liskov"));
        assertEquals("9", map.get("Ken Kousen"));
        assertEquals("3", map.get("Herbert Schildt"));

    }
}
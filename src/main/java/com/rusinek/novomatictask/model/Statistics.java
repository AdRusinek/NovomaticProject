package com.rusinek.novomatictask.model;

import java.util.Map;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
public class Statistics {

    private int repositoriesCount;
    private Map<String, String> languages;

    public Statistics(int repositoriesCount, Map<String, String> languages) {
        this.repositoriesCount = repositoriesCount;
        this.languages = languages;
    }

    public Statistics() {
    }

    public void setRepositoriesCount(int repositoriesCount) {
        this.repositoriesCount = repositoriesCount;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public int getRepositoriesCount() {
        return repositoriesCount;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }
}
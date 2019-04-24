package com.rusinek.novomatictask.model;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
public class ConnectionData {

    public static final String GIT_URL = "https://api.github.com/users/";

    public static final String GIT_USERNAME = System.getenv("GITHUB_USER");

    public static final String GIT_PASSWORD = System.getenv("GITHUB_PASSWORD");

}

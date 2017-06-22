package com.axisrooms.storm.util;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoSingleton extends MongoClient {

    private static MongoSingleton instance = null;

    protected MongoSingleton() throws UnknownHostException,UnsupportedOperationException {

    }

    protected MongoSingleton(String ip, int port) throws UnknownHostException,UnsupportedOperationException {
        super(ip, port);
    }


    public static synchronized MongoSingleton getInstance(String ip, int port) throws UnknownHostException{
        if (instance == null)
            instance =  new MongoSingleton(ip,port);

        return instance;
    }
}
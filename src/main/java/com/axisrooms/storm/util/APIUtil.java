package com.axisrooms.storm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.storm.shade.org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class APIUtil {

    public static int redirectPost(String jsonRequest, String baseURL, String endPoint) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        StringEntity input = new StringEntity(jsonRequest);
        input.setContentType("application/json");

        HttpPost post = new HttpPost(baseURL + endPoint);
        post.setEntity(input);

        HttpResponse response = client.execute(post);

        if(response != null)
            log.info("Response - " + response.toString());

        return response.getStatusLine().getStatusCode();
    }


}

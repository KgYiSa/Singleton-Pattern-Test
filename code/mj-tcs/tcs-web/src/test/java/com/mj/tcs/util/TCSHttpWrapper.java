/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Wang Zhen
 */
public class TcsHttpWrapper {
    private static final MediaType TYPE = MediaType.APPLICATION_JSON;
    private RestTemplate restTemplate = null;

    public <T> HttpEntity<T> createJsonRequest(T content) {
        HttpEntity<T> request = new HttpEntity<>(content, createHeaders());

        return request;
    }

    public HttpEntity<?> createEmptyJsonRequest() {
        HttpEntity<String> request = new HttpEntity<String>(createHeaders());

        return request;
    }

    public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;

        // Set the request factory.
        // IMPORTANT: This section I had to add for POST request. Not needed for GET
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(
                HttpClientUtils.acceptsUntrustedCertsHttpClient()
        ));

        // Add converters
        // Note I use the Jackson Converter, I removed the http form converter
        // because it is not needed when posting String, used for multipart forms.
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        return restTemplate;
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
        headers.add("mediaType", "application/json");
//        List<MediaType> mediaTypes = new ArrayList<>();
//        mediaTypes.add(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.add("Accept", "application/json");
//
//        List<Charset> charsets = new ArrayList<>();
//        charsets.add(Charset.forName("UTF-8"));
//        headers.setAcceptCharset(Collections.singletonList(Charset.forName("UTF-8")));

        return headers;
    }
}

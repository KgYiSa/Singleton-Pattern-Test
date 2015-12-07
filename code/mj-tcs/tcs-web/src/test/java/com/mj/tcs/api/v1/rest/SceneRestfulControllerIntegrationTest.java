package com.mj.tcs.api.v1.rest;

import com.mj.tcs.TcsWebServerApplication;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.util.SceneDtoModelGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by xiaobai on 2015/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TcsWebServerApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class SceneRestfulControllerIntegrationTest {
    private static final String BASE_URI = "http://localhost:8080/tcs-web/api/v1/rest";

    private static final RestTemplate restTemplate = new RestTemplate();

    private static Map<String, Object> sceneDataMapping;

    private SceneDtoModelGenerator generator = null;

    @Before
    public void setUp() throws Exception {
        generator = new SceneDtoModelGenerator();
    }

    @After
    public void tearDown() throws Exception {
//        delete();
    }

//    @Test
    public void testGet() throws Exception {
        HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_JSON);

        ResponseEntity<Collection> response = restTemplate.exchange(
                BASE_URI + "/scenes/",
                HttpMethod.GET, entity, Collection.class
        );

        Collection<SceneDto> sceneDtos = (Collection<SceneDto>) response.getBody();

        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void post() throws IOException {
        SceneDto dto = generator.createSceneDto();
        HttpEntity<SceneDto> request = new HttpEntity<>(dto);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE_URI + "/scenes/",
                HttpMethod.POST, request, Object.class
        );

        assertTrue(response.getStatusCode() == HttpStatus.CREATED);

        Map responseBody = (Map)response.getBody();

        assertEquals(dto.getName(), responseBody.get("name"));
        assertEquals(dto.getPathDtos().size(), ((Collection)responseBody.get("paths")).size());
        assertEquals(dto.getPointDtos().size(), ((Collection) responseBody.get("points")).size());

//        return responseBody;
    }


//    @Test
    public void getOne() {
        // The new one
        long id = (Integer) sceneDataMapping.get("id");
        String name = (String) sceneDataMapping.get("name");

        // Tesing
        HttpEntity<String> newRequest = prepareGet(MediaType.APPLICATION_JSON);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE_URI + "/scenes/" + id,
                HttpMethod.GET, newRequest, Object.class
        );

        assertTrue(response.getStatusCode() == HttpStatus.OK);

        Map newResponseBody = (Map)response.getBody();

        assertEquals(name, (String)newResponseBody.get("name"));
    }

    // need to instantiate resttemplate by a ClientHttpRequestFactory object to support PUT
//    @Test
    public void put() { // update all
        // Create a new one
//        long id = (Integer) sceneDataMapping.get("id");
//        String name = (String) sceneDataMapping.get("name");
//
//        // Tesing
//        String newName = name + "_new";
//        SceneDto dto = generator().createSceneDto();
//        dto.setName(newName);
//        dto.setId(id);
//
//        HttpEntity<SceneDto> newRequest = new HttpEntity<>(dto);
//
//        ResponseEntity<Object> response = restTemplate.exchange(
//                BASE_URI + "/scenes/" + id,
//                HttpMethod.PUT, newRequest, Object.class
//        );
//
//        assertTrue(response.getStatusCode() == HttpStatus.OK);
//
//        Map responseBody = (Map)response.getBody();
//
//        assertEquals(newName, responseBody.get("name"));
    }

    // need to instantiate resttemplate by a ClientHttpRequestFactory object to support PATCH
//    @Test
    public void patch() { // update partial
        // Create a new one
//        long id = (Integer) sceneDataMapping.get("id");
//        String name = (String) sceneDataMapping.get("name");
//
//        // Tesing
//        String newName = name + "_new";
//        Map<String, Object> requestMap = new HashMap<>();
//        requestMap.put("name", newName);
//
//        HttpEntity<Map<String, Object>> newRequest = new HttpEntity<>(requestMap);
//
//        ResponseEntity<Object> response = restTemplate.exchange(
//                BASE_URI + "/scenes/" + id,
//                HttpMethod.PATCH, newRequest, Object.class
//        );
//
//        assertTrue(response.getStatusCode() == HttpStatus.OK);
//
//        Map responseBody = (Map)response.getBody();
//
//        assertEquals(newName, responseBody.get("name"));
    }

    //    @Test // no testing here
    public static void delete() {
        // Create a new one

        long id = (Integer) sceneDataMapping.get("id");
        String name = (String) sceneDataMapping.get("name");

        // testing
        HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_JSON);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE_URI + "/scenes/" + id,
                HttpMethod.DELETE, entity, Object.class
        );

        assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT);
    }

    private static HttpEntity<String> prepareGet(MediaType type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

//    @Test
    public void testGetAllScenes() throws Exception {

    }

//    @Test
    public void testCreateScene() throws Exception {
        SceneDto sceneDto = generator.createSceneDto();

    }

//    @Test
    public void testGetOneScene() throws Exception {

    }
}
package com.mj.tcs.api.v1.rest;

import com.mj.tcs.TCSWebServerApplication;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.config.AppContext;
import com.mj.tcs.config.WebAppConfig;
import com.mj.tcs.config.WebSecurityConfig;
import com.mj.tcs.util.SceneDtoModelGenerator;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by xiaobai on 2015/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {AppContext.class, WebAppConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
// Spock
//@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = TCSWebServerApplication.class)
@WebIntegrationTest(randomPort = false)
public class SceneRestfulControllerIntegrationTest {
    private static final String BASE_URI = "http://localhost:8080/api/v1/rest";

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private static SceneDtoModelGenerator DATA_GENERATOR = null;

    private static Map<String, Object> SCENE_DATA_MAPPING;

    @BeforeClass
    public static void setUp() throws Exception {
        DATA_GENERATOR = new SceneDtoModelGenerator();
        SCENE_DATA_MAPPING = post();
    }

    @AfterClass
    public static void tearDown() throws Exception {
//        delete();
    }

//    @Test
    public static Map post() throws IOException {
        SceneDto dto = DATA_GENERATOR.createSceneDto();
        HttpEntity<SceneDto> request = new HttpEntity<>(dto);

        ResponseEntity<TCSResponseEntity> response = REST_TEMPLATE.exchange(
                BASE_URI + "/scenes/",
                HttpMethod.POST, request, TCSResponseEntity.class
        );

        assertNotNull(response.getBody());
        assertTrue(response.getBody().getStatusCode() == TCSResponseEntity.Status.SUCCESS);
        assertNotNull(response.getBody().getBody());
        return (Map) response.getBody().getBody();
    }

    //    @Test // no testing here
    public static void delete() {
        HttpEntity<String> entity = prepareHttpEntity(null);

        assertNotNull(SCENE_DATA_MAPPING);
        assertNotNull(SCENE_DATA_MAPPING.get("id"));
        Long sceneId = Long.parseLong(SCENE_DATA_MAPPING.get("id").toString());
        assertNotNull(sceneId);

        ResponseEntity<TCSResponseEntity> response = REST_TEMPLATE.exchange(
                BASE_URI + "/scenes/" + sceneId,
                HttpMethod.DELETE, entity, TCSResponseEntity.class
        );

        assertTrue(response.getBody().getStatusCode() == TCSResponseEntity.Status.SUCCESS);

        // Try get once to double check !
        HttpEntity<String> newRequest = prepareHttpEntity(null);

        ResponseEntity<TCSResponseEntity> newResponse = REST_TEMPLATE.exchange(
                BASE_URI + "/scenes/" + sceneId,
                HttpMethod.GET, newRequest, TCSResponseEntity.class
        );

        assertTrue (newResponse.getBody().getStatusCode() != TCSResponseEntity.Status.ERROR);
        assertNull(newResponse.getBody().getBody());
    }

    @Test
    public void testGetProfile() throws Exception {
        HttpEntity<String> entity = prepareHttpEntity(null);

        ResponseEntity<TCSResponseEntity> response = REST_TEMPLATE.exchange(
                BASE_URI + "/scenes/profile",
                HttpMethod.GET, entity, TCSResponseEntity.class
        );

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        assertTrue(response.getBody().getStatusCode() == TCSResponseEntity.Status.SUCCESS);
    }

    @Test
    public void testGet() {
        assertNotNull(SCENE_DATA_MAPPING);
        assertNotNull(SCENE_DATA_MAPPING.get("id"));
        assertNotNull(SCENE_DATA_MAPPING.get("name"));

        Long sceneId = Long.parseLong(SCENE_DATA_MAPPING.get("id").toString());
        assertNotNull(sceneId);

        String name = SCENE_DATA_MAPPING.get("name").toString();

        // Tesing
        HttpEntity<String> newRequest = prepareHttpEntity(null);

        ResponseEntity<TCSResponseEntity> response = REST_TEMPLATE.exchange(
                BASE_URI + "/scenes/" + sceneId,
                HttpMethod.GET, newRequest, TCSResponseEntity.class
        );

        assertNotNull(response.getBody());
        assertTrue(response.getBody().getStatusCode() == TCSResponseEntity.Status.SUCCESS);
        assertNotNull(response.getBody().getBody());
        assertEquals(name, ((Map) response.getBody().getBody()).get("name").toString());
    }

    // need to instantiate resttemplate by a ClientHttpRequestFactory object to support PUT
//    @Test
//    public void testPut() { // update all
//        assertNotNull(SCENE_DATA_MAPPING);
//        assertNotNull(SCENE_DATA_MAPPING.get("id"));
//        assertNotNull(SCENE_DATA_MAPPING.get("name"));
//
//        Long sceneId = Long.parseLong(SCENE_DATA_MAPPING.get("id").toString());
//        assertNotNull(sceneId);
//
//        String name = SCENE_DATA_MAPPING.get("name").toString();
//
//        // Tesing
//        String newName = name + "_new";
//        SceneDto dto = DATA_GENERATOR.createSceneDto();
//        dto.setName(newName);
//        dto.setId(sceneId);
//
//        HttpEntity<SceneDto> request = prepareHttpEntity(dto);
//
//        ResponseEntity<TCSResponseEntity> response = REST_TEMPLATE.exchange(
//                BASE_URI + "/scenes/" + sceneId,
//                HttpMethod.PUT, request, TCSResponseEntity.class
//        );
//
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().getStatusCode() == TCSResponseEntity.Status.SUCCESS);
//
//        assertNotNull(response.getBody().getBody());
//        assertEquals(name, ((Map) response.getBody().getBody()).get("name").toString());
//    }

    // need to instantiate resttemplate by a ClientHttpRequestFactory object to support PATCH
//    @Test
    public void patch() { // update partial
        // Create a new one
//        long id = (Integer) SCENE_DATA_MAPPING.get("id");
//        String name = (String) SCENE_DATA_MAPPING.get("name");
//
//        // Tesing
//        String newName = name + "_new";
//        Map<String, Object> requestMap = new HashMap<>();
//        requestMap.put("name", newName);
//
//        HttpEntity<Map<String, Object>> newRequest = new HttpEntity<>(requestMap);
//
//        ResponseEntity<Object> response = REST_TEMPLATE.exchange(
//                BASE_URI + "/scenes/" + id,
//                HttpMethod.PATCH, newRequest, Object.class
//        );
//
//        assertTrue(response.getActionCode() == HttpStatus.SUCCESS);
//
//        Map responseBody = (Map)response.getBody();
//
//        assertEquals(newName, responseBody.get("name"));
    }

    private static <T> HttpEntity<T> prepareHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<T>(headers);
        if (body != null) {
            entity = new HttpEntity<T>(body, headers);
        }
        return entity;
    }
}
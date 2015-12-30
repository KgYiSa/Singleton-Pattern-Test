package com.mj.tcs.service;

import static org.mockito.Mockito.*;
import com.mj.tcs.api.v1.dto.*;
import org.junit.Test;
import java.util.*;

/**
 * Created by GuoXiaowu on 2015/12/28.
 */
public class Xml2EntityServiceTest {

    @Test
    public void testGetSceneDto() throws Exception {

    }

    @Test
    public void testSetSceneDto() throws Exception {
//        Xml2EntityService xml2EntityService = (Xml2EntityService)aC.getBean("xml2EntityService");
//        xml2EntityService = new Xml2EntityService();
//        Document document = TcsXmlUtils.getDocument("D:\\Demo.xml");
//        Element root = document.getRootElement();
//        String newSceneName = "test_scene_" +
//                new SimpleDateFormat("yy_MM_dd_HH_mm_ss").format(new Date()).toString();
//        System.out.println(newSceneName);
//
//        SceneDto sceneDto = new SceneDto();
//        sceneDto.setAuditorDto(xml2EntityService.createAuditor());
//        sceneDto.setName(newSceneName);
//        xml2EntityService.setSceneDto(sceneDto);
//        List<Element> elements = root.elements();
//        for (Element e : elements) {
//            String type = TcsXmlUtils.getNodeAttrMap(e).get("type");
//            xml2EntityService.Map2Dto(TcsXmlUtils.getPoint(e),type);
//        }
//
//        SceneDto newSceneDto = resolveRelationships(sceneDto);
//        newSceneDto = serviceGateway.createScene(newSceneDto);
    }

//    @Test
//    public void testCreateAuditor() throws Exception {
//
//    }

    @Test
    public void testMap2Dto() throws Exception {

    }

    @Test
    public void testMap2PointDto() throws Exception {

        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("modelXPosition")).thenReturn("0");
        when(mockMap.get("modelYPosition")).thenReturn("0");
        when(mockMap.get("Name")).thenReturn("0");
        when(mockMap.get("POSITION_X")).thenReturn("0");
        when(mockMap.get("POSITION_Y")).thenReturn("0");
        when(mockMap.get("LABEL_OFFSET_X")).thenReturn("0");
        when(mockMap.get("LABEL_OFFSET_Y")).thenReturn("0");
        when(mockMap.get("vehicleOrientationAngle")).thenReturn("0");
        when(mockMap.get("Type")).thenReturn("HALT");

        PointDto pointDto = xml2EntityService.Map2PointDto(mockMap);
    }

    @Test
    public void testMap2PathDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn("0");
        when(mockMap.get("maxVelocity")).thenReturn("0");
        when(mockMap.get("cost")).thenReturn("0");
        when(mockMap.get("startComponent")).thenReturn("0");
        when(mockMap.get("endComponent")).thenReturn("0");
        when(mockMap.get("maxReverseVelocity")).thenReturn("0");
        when(mockMap.get("length")).thenReturn("0");
        when(mockMap.get("locked")).thenReturn("0");

        PathDto pathDto = xml2EntityService.Map2PathDto(mockMap);
    }

    @Test
    public void testMap2LocationLinkDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn("0");
        when(mockMap.get("endComponent")).thenReturn("0");
        when(mockMap.get("startComponent")).thenReturn("0");

        LocationLinkDto locationLinkDto = xml2EntityService.Map2LocationLinkDto(mockMap);
    }

    @Test
    public void testMap2LocationTypeDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn("0");

        LocationTypeDto locationTypeDto = xml2EntityService.Map2LocationTypeDto(mockMap);
    }

    @Test
    public void testMap2LocationDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("modelXPosition")).thenReturn("0");
        when(mockMap.get("modelYPosition")).thenReturn("0");
        when(mockMap.get("Name")).thenReturn("0");
        when(mockMap.get("POSITION_X")).thenReturn("0");
        when(mockMap.get("POSITION_Y")).thenReturn("0");
        when(mockMap.get("LABEL_OFFSET_X")).thenReturn("0");
        when(mockMap.get("LABEL_OFFSET_Y")).thenReturn("0");
        when(mockMap.get("vehicleOrientationAngle")).thenReturn("0");
        when(mockMap.get("Type")).thenReturn("HALT");

        LocationDto locationDto = xml2EntityService.Map2LocationDto(mockMap);
    }

    @Test
    public void testMap2BlockDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn("0");

        BlockDto blockDto = xml2EntityService.Map2BlockDto(mockMap);
    }

    @Test
    public void testMap2VehicleDto() throws Exception {
        Xml2EntityService xml2EntityService = new Xml2EntityService();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn("0");
        when(mockMap.get("EnergyLevel")).thenReturn("0");
        when(mockMap.get("EnergyLevelCritical")).thenReturn("0");
        when(mockMap.get("OrientationAngle")).thenReturn("0");
        when(mockMap.get("EnergyLevelGood")).thenReturn("0");
        when(mockMap.get("Length")).thenReturn("0");

        VehicleDto vehicleDto = xml2EntityService.Map2VehicleDto(mockMap);
    }
}
package com.mj.tcs.util;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.data.model.Point;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XML2EntityUtilsTest {

    private static String tempNull = null;

    private static String tempNormal = "100.0";

    private static String tempEmpty ;



    public PointDto Map2pointDto(String testString){
        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("modelXPosition")).thenReturn(testString);
        when(mockMap.get("modelYPosition")).thenReturn(testString);
        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("POSITION_X")).thenReturn(testString);
        when(mockMap.get("POSITION_Y")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_X")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_Y")).thenReturn(testString);
        when(mockMap.get("vehicleOrientationAngle")).thenReturn(testString);
        when(mockMap.get("Type")).thenReturn("HALT");

        return xml2EntityUtils.Map2PointDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2PointDto() throws Exception {

        String testString = tempNull;
        Map2pointDto(testString);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2PointDto() throws Exception {

        String testString = tempEmpty;
        Map2pointDto(testString);

    }

    @Test
    public void testNormalMap2PointDto() throws Exception {

        String testString = tempNormal;
        Map2pointDto(testString);

    }

    public PathDto Map2PathDto(String testString) throws NoSuchFieldException, IllegalAccessException {
        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        PointDto pointDto = new PointDto();
        pointDto.setName("100.0");

        SceneDto mockedSsceneDto = mock(SceneDto.class);
        when(mockedSsceneDto.getPointDtoByName("100.0")).thenReturn(pointDto);
        Field fieldSceneDto = XML2EntityUtils.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(xml2EntityUtils, mockedSsceneDto);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("maxVelocity")).thenReturn(testString);
        when(mockMap.get("cost")).thenReturn(testString);
        when(mockMap.get("startComponent")).thenReturn(testString);
        when(mockMap.get("endComponent")).thenReturn(testString);
        when(mockMap.get("maxReverseVelocity")).thenReturn(testString);
        when(mockMap.get("length")).thenReturn(testString);
        when(mockMap.get("locked")).thenReturn(testString);

        return xml2EntityUtils.Map2PathDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2PathDto() throws Exception {
        String testString = tempNull;

        Map2PathDto(testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2PathDto() throws Exception {
        String testString = tempEmpty;

        Map2PathDto(testString);
    }

    @Test
    public void testNormalMap2PathDto() throws Exception {
        String testString = tempNormal;

        Map2PathDto(testString);
    }

    public LocationLinkDto Map2LocationLinkDto(String testString) throws Exception {

        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        LocationDto locationDto = new LocationDto();
        locationDto.setName("100.0");

        PointDto pointDto = new PointDto();
        pointDto.setName("100.0");

        SceneDto mockedSsceneDto = mock(SceneDto.class);

        when(mockedSsceneDto.getLocationDtoByName("100.0")).thenReturn(locationDto);
        when(mockedSsceneDto.getPointDtoByName("100.0")).thenReturn(pointDto);
        Field fieldSceneDto = XML2EntityUtils.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(xml2EntityUtils,mockedSsceneDto);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("endComponent")).thenReturn(testString);
        when(mockMap.get("startComponent")).thenReturn(testString);

         return xml2EntityUtils.Map2LocationLinkDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2LocationLinkDto() throws Exception {
        String testString = tempNull;

        Map2LocationLinkDto(testString);
    }

    @Test
    public void testNormalMap2LocationLinkDto() throws Exception {
        String testString = tempNormal;

        Map2LocationLinkDto(testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2LocationLinkDto() throws Exception {
        String testString = tempEmpty;

        Map2LocationLinkDto(testString);
    }

    public LocationTypeDto Map2LocationTypeDto(String testString) throws Exception {
        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);
        when(mockMap.get("Name")).thenReturn(testString);
        return xml2EntityUtils.Map2LocationTypeDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2LocationTypeDto() throws Exception {

        String testString = tempNull;

        Map2LocationTypeDto(testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2LocationTypeDto() throws Exception {

        String testString = tempEmpty;

        Map2LocationTypeDto(testString);
    }

    @Test
    public void testNormalMap2LocationTypeDto() throws Exception {

        String testString = tempNormal;

        Map2LocationTypeDto(testString);
    }


    public LocationDto Map2LocationDto( String testString ) throws Exception {

        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        LocationTypeDto locationTypeDto = new LocationTypeDto();
        locationTypeDto.setName("HALT");

        SceneDto mockedSsceneDto = mock(SceneDto.class);

        when(mockedSsceneDto.getLocationTypeDtoByName("HALT")).thenReturn(locationTypeDto);
        Field fieldSceneDto = XML2EntityUtils.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(xml2EntityUtils,mockedSsceneDto);

        when(mockMap.get("modelXPosition")).thenReturn(testString);
        when(mockMap.get("modelYPosition")).thenReturn(testString);
        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("POSITION_X")).thenReturn(testString);
        when(mockMap.get("POSITION_Y")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_X")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_Y")).thenReturn(testString);
        when(mockMap.get("vehicleOrientationAngle")).thenReturn(testString);
        when(mockMap.get("Type")).thenReturn("HALT");

         return xml2EntityUtils.Map2LocationDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2LocationDto() throws Exception {

        String testString = tempNull;

        Map2LocationDto(testString);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2LocationDto() throws Exception {

        String testString = tempEmpty;

        Map2LocationDto(testString);
    }
    @Test
    public void testNormalMap2LocationDto() throws Exception {

        String testString = tempNormal;

        Map2LocationDto(testString);
    }

    public BlockDto Map2BlockDto(String testString) throws Exception {

        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn(testString);

         return xml2EntityUtils.Map2BlockDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2BlockDto() throws Exception {

        String testString = tempNull;

        Map2BlockDto(testString);
    }

    @Test
    public void tesNormaltMap2BlockDto() throws Exception {

        String testString = tempNormal;

        Map2BlockDto(testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2BlockDto() throws Exception {

        String testString = tempEmpty;

        Map2BlockDto(testString);
    }

    public VehicleDto Map2VehicleDto(String testString) throws Exception {

        XML2EntityUtils xml2EntityUtils = new XML2EntityUtils();
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("EnergyLevel")).thenReturn(testString);
        when(mockMap.get("EnergyLevelCritical")).thenReturn(testString);
        when(mockMap.get("OrientationAngle")).thenReturn(testString);
        when(mockMap.get("EnergyLevelGood")).thenReturn(testString);
        when(mockMap.get("Length")).thenReturn(testString);

         return xml2EntityUtils.Map2VehicleDto(mockMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap2VehicleDto() throws Exception {

        String testString = tempNull;

        Map2VehicleDto(testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap2VehicleDto() throws Exception {

        String testString = tempEmpty;

        Map2VehicleDto(testString);
    }

    @Test
    public void testNormalMap2VehicleDto() throws Exception {

        String testString = tempNormal;

        Map2VehicleDto(testString);
    }
}
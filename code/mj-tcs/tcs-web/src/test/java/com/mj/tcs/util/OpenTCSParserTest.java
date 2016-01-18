package com.mj.tcs.util;

import com.mj.tcs.api.v1.dto.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenTCSParserTest {

    private static final String tempNull = null;

    private static final String tempNormal = "123.4";

    private static String tempEmpty ;



    public PointDto Map2pointDto(String testString){
        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
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
        when(mockMap.get("RFID")).thenReturn(String.valueOf(new Random().nextInt(1000)));

        return openTCSParser.map2PointDto(mockMap);
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
        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);

        PointDto pointDto = new PointDto();
        pointDto.setName(testString);

        SceneDto mockedSceneDto = mock(SceneDto.class);
        when(mockedSceneDto.getPointDtoByName(testString)).thenReturn(pointDto);
        Field fieldSceneDto = OpenTCSParser.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(openTCSParser, mockedSceneDto);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("maxVelocity")).thenReturn(testString);
        when(mockMap.get("cost")).thenReturn(testString);
        when(mockMap.get("startComponent")).thenReturn(testString);
        when(mockMap.get("endComponent")).thenReturn(testString);
        when(mockMap.get("maxReverseVelocity")).thenReturn(testString);
        when(mockMap.get("length")).thenReturn(testString);
        when(mockMap.get("locked")).thenReturn(testString);
        when(mockMap.get("CONTROL_POINTS")).thenReturn("100,200;300,400");

        return openTCSParser.map2PathDto(mockMap);
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

        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);

        LocationDto locationDto = new LocationDto();
        locationDto.setName(testString);

        PointDto pointDto = new PointDto();
        pointDto.setName(testString);

        SceneDto mockedSsceneDto = mock(SceneDto.class);

        when(mockedSsceneDto.getLocationDtoByName(testString)).thenReturn(locationDto);
        when(mockedSsceneDto.getPointDtoByName(testString)).thenReturn(pointDto);
        Field fieldSceneDto = OpenTCSParser.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(openTCSParser,mockedSsceneDto);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("endComponent")).thenReturn(testString);
        when(mockMap.get("startComponent")).thenReturn(testString);

         return openTCSParser.map2LocationLinkDto(mockMap);
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
        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);
        when(mockMap.get("Name")).thenReturn(testString);
        return openTCSParser.map2LocationTypeDto(mockMap);
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

        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);

        LocationTypeDto locationTypeDto = new LocationTypeDto();
        locationTypeDto.setName("HALT");

        SceneDto mockedSsceneDto = mock(SceneDto.class);

        when(mockedSsceneDto.getLocationTypeDtoByName("HALT")).thenReturn(locationTypeDto);
        Field fieldSceneDto = OpenTCSParser.class.getDeclaredField("sceneDto");
        fieldSceneDto.setAccessible(true);
        fieldSceneDto.set(openTCSParser,mockedSsceneDto);

        when(mockMap.get("modelXPosition")).thenReturn(testString);
        when(mockMap.get("modelYPosition")).thenReturn(testString);
        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("POSITION_X")).thenReturn(testString);
        when(mockMap.get("POSITION_Y")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_X")).thenReturn(testString);
        when(mockMap.get("LABEL_OFFSET_Y")).thenReturn(testString);
        when(mockMap.get("vehicleOrientationAngle")).thenReturn(testString);
        when(mockMap.get("Type")).thenReturn("HALT");

         return openTCSParser.map2LocationDto(mockMap);
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

        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("COLOR")).thenReturn(testString);

         return openTCSParser.map2BlockDto(mockMap);
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

        OpenTCSParser openTCSParser = new OpenTCSParser(mock(SceneDto.class));
        Map<String,String> mockMap = mock(Map.class);

        when(mockMap.get("Name")).thenReturn(testString);
        when(mockMap.get("EnergyLevel")).thenReturn(testString);
        when(mockMap.get("EnergyLevelCritical")).thenReturn(testString);
        when(mockMap.get("OrientationAngle")).thenReturn(testString);
        when(mockMap.get("EnergyLevelGood")).thenReturn(testString);
        when(mockMap.get("Length")).thenReturn(testString);

         return openTCSParser.map2VehicleDto(mockMap);
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
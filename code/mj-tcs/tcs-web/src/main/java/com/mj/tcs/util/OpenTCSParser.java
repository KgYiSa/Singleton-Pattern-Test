package com.mj.tcs.util;

import com.mj.tcs.api.dto.*;
import com.mj.tcs.api.dto.base.BaseEntityDto;
import com.mj.tcs.api.dto.base.EntityProperty;
import com.mj.tcs.api.dto.base.TripleDto;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class OpenTCSParser {
    public static final String LEVEL_1_DEVIDER = ";";
    private static final String LEVEL_2_DEVIDER = ",";
    private static final String MAP_KEY_VALUE_DEVIDER = "=";

    private final SceneDto sceneDto;

    public OpenTCSParser(SceneDto sceneDto) {
        this.sceneDto = Objects.requireNonNull(sceneDto);
    }

    public SceneDto getSceneDto() {
        return sceneDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public Object map2Dto(Map<String,String> map, String type){
        switch (type){
            case "point":
                return sceneDto.addPointDto(map2PointDto(map));
            case "path":
                return sceneDto.addPathDto(map2PathDto(map));
            case "locationType":
                return sceneDto.addLocationTypeDto(map2LocationTypeDto(map));
            case "location":
                return sceneDto.addLocationDto(map2LocationDto(map));
            case "link":
                return map2LocationLinkDto(map);
            case "block":
                return sceneDto.addBlockDto(map2BlockDto(map));
            case "staticRoute":
                return sceneDto.addStaticRouteDto(map2StaticRouteDto(map));
            case "vehicle":
                return sceneDto.addVehicleDto(map2VehicleDto(map));
            default:
                return null;
        }
    }

    // TODO:
    public <T extends BaseEntityDto> T map2BaseDto(T dto, Map<String, String> map) {

        return dto;
    }

    /**
     *
     * @param map
     * @return
     */
    public PointDto map2PointDto(Map<String,String> map){
        PointDto pointDto = new PointDto();
        TripleDto triple = new TripleDto();
        triple.setX(getLongFromMap("modelXPosition",map));//((int)Double.parseDouble(map.get("modelXPosition")));
        triple.setY(getLongFromMap("modelYPosition",map));//((int)Double.parseDouble(map.get("modelYPosition")));
        triple.setZ((int)Double.parseDouble("0"));
        pointDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        pointDto.setDisplayPositionX(getLongFromMap("POSITION_X",map));//(Long.parseLong(map.get("POSITION_X")));
        pointDto.setDisplayPositionY(getLongFromMap("POSITION_Y",map));//(Long.parseLong(map.get("POSITION_Y")));
        pointDto.setLabelOffsetX(getLongFromMap("LABEL_OFFSET_X",map));//(Long.parseLong(map.get("LABEL_OFFSET_X").length()<1?"0":map.get("LABEL_OFFSET_X")));
        pointDto.setLabelOffsetY(getLongFromMap("LABEL_OFFSET_Y",map));//(Long.parseLong(map.get("LABEL_OFFSET_Y").length()<1?"0":map.get("LABEL_OFFSET_X")));
        pointDto.setVehicleOrientationAngle(getDoubleFromMap("vehicleOrientationAngle",map));//(Double.parseDouble(map.get("vehicleOrientationAngle").equals("NaN")?"0":map.get("vehicleOrientationAngle")));
        pointDto.setPosition(triple);
        pointDto.setType(getTypeFromMap("Type",map));//(PointDto.Type.valueOf(map.get("Type")+"_POSITION"));

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        // TODO: Add RFID to properties
        properties.put("tcs::rfid", String.valueOf(getLongFromMap("RFID",map)));
        pointDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));

        return pointDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public PathDto map2PathDto(Map<String,String> map){
        PathDto pathDto = new PathDto();
        pathDto.setName(getStringFromMap("Name",map));
        pathDto.setMaxVelocity(getIntegerFromMap("maxVelocity",map));//(Integer.parseInt(map.get("maxVelocity").split("\\.")[0]));
        pathDto.setRoutingCost(getLongFromMap("cost",map));//((long)Double.parseDouble(map.get("cost")));
        PointDto outPoint = getPointDtoFromMap("startComponent",map);
        outPoint.addOutgoingPath(pathDto);
        pathDto.setSourcePointDto(outPoint);//(sceneDto.getPointDtoByName(map.get("startComponent")));
        PointDto inPoint = getPointDtoFromMap("endComponent",map);
        inPoint.addIncomingPath(pathDto);
        pathDto.setDestinationPointDto(inPoint);//(sceneDto.getPointDtoByName(map.get("endComponent")));
        pathDto.setMaxReverseVelocity(getIntegerFromMap("maxReverseVelocity",map));//(Integer.parseInt(map.get("maxReverseVelocity").split("\\.")[0]));
        pathDto.setLength(getLongFromMap("length",map));//((long)Double.parseDouble(map.get("length")));
        pathDto.setLocked(getBooleanFromMap("locked",map));//(map.get("locked").equals("false"));
        pathDto.setControlPoints(getControlPointsFromMap("CONTROL_POINTS",map));

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        pathDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));

        return pathDto;
    }

    private List<TripleDto> getControlPointsFromMap(String control_points, Map<String, String> map) {
        List<TripleDto> tripleDtos = new ArrayList<>();
        try {
            if(map.get(control_points).isEmpty()){
                return tripleDtos;
            }
            String[] points = map.get(control_points).split(LEVEL_1_DEVIDER);
            for (int i = 0; i < points.length; i++) {
                String[] label = points[i].split(LEVEL_2_DEVIDER);
                TripleDto tripleDto = new TripleDto();
                tripleDto.setX(Integer.parseInt(label[0])*50);
                tripleDto.setY(-Integer.parseInt(label[1])*50);
                tripleDto.setZ(0);
                tripleDtos.add(tripleDto);
            }
        }catch (Exception e){
//            System.out.print(e.getMessage());
            e.printStackTrace();
        }
        return tripleDtos;
    }

    /**
     *
     endComponent	Goods in north 01

     Name	Point-0026 --- Goods in north 01
     startComponent	Point-0026

     * @param map
     * @return
     */
    public LocationLinkDto map2LocationLinkDto(Map<String,String> map){
        LocationDto locationDto = getLocationDtoFromMap("endComponent",map);//sceneDto.getLocationDtoByName(map.get("endComponent"));
        LocationLinkDto locationLinkDto = new LocationLinkDto();
        locationLinkDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        locationLinkDto.setPointDto(getPointDtoFromMap("startComponent",map));//(sceneDto.getPointDtoByName(map.get("startComponent")));
        locationLinkDto.setLocationDto(getLocationDtoFromMap("endComponent",map));//(sceneDto.getLocationDtoByName(map.get("endComponent")));
        locationLinkDto.setAllowedOperations(null);
        locationDto.attachLink(locationLinkDto);

        return locationLinkDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public LocationTypeDto map2LocationTypeDto(Map<String,String> map){
        LocationTypeDto locationTypeDto = new LocationTypeDto();
        locationTypeDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        locationTypeDto.setAllowedOperations(null);

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        locationTypeDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));

        return locationTypeDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public LocationDto map2LocationDto(Map<String,String> map){
        LocationDto locationDto = new LocationDto();
        TripleDto triple = new TripleDto();
        triple.setX(getLongFromMap("modelXPosition",map));//((int)Double.parseDouble(map.get("modelXPosition")));
        triple.setY(getLongFromMap("modelXPosition",map));//((int)Double.parseDouble(map.get("modelYPosition")));
        triple.setZ((long)Double.parseDouble("0"));
        locationDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        locationDto.setPosition(triple);
        locationDto.setLabelOffsetX(getLongFromMap("LABEL_OFFSET_X",map));//(Long.parseLong(map.get("LABEL_OFFSET_X")));
        locationDto.setLabelOffsetY(getLongFromMap("LABEL_OFFSET_Y",map));//(Long.parseLong(map.get("LABEL_OFFSET_Y")));
        locationDto.setLocationTypeDto(getLocationTypeDtoFromMap("Type",map));//(sceneDto.getLocationTypeDtoByName(map.get("Type")));//Type
        locationDto.setDisplayPositionX(getLongFromMap("POSITION_X",map));//(Long.parseLong(map.get("POSITION_X")));
        locationDto.setDisplayPositionY(getLongFromMap("POSITION_Y",map));//(Long.parseLong(map.get("POSITION_Y")));

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        locationDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));

        return locationDto;
    }

    public BlockDto map2BlockDto(Map<String,String> map){
        BlockDto blockDto = new BlockDto();
        blockDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        blockDto.setResources(getBlockElementsFromMap("blockElements",map));

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        blockDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));
        // TODO: Not Used currently
        blockDto.addProperty("COLOR",getStringFromMap("COLOR",map));

        return blockDto;
    }

    public StaticRouteDto map2StaticRouteDto(Map<String,String> map){
        StaticRouteDto routeDto = new StaticRouteDto();
        routeDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        routeDto.setHops(getHopsFromMap("staticRouteElements",map));

        final Map<String, String> properties = getMapValueFromMap("Miscellaneous", map);
        routeDto.setProperties(properties.keySet().stream().map(k -> new EntityProperty(k, properties.get(k))).collect(Collectors.toSet()));
        // TODO: Not Used currently
        routeDto.addProperty("COLOR",getStringFromMap("COLOR",map));

        return routeDto;
    }

    private Map<String, String> getMapValueFromMap(String name, Map<String, String> map) {
        Map<String, String> mapValue = new HashMap<>();
        String value = map.get(name);
        if (!Objects.isNull(value)) {
            for (String subValue : value.split(LEVEL_1_DEVIDER)) {
                String[] temp = subValue.split(MAP_KEY_VALUE_DEVIDER);
                if ((temp != null) && (temp.length == 2)) {
                    mapValue.put(temp[0], temp[1]);
                }
            }
        }

        return mapValue;
    }

    private String[] getStringArrayFromMap(String name, Map<String, String> map) {
        String value = map.get(name);
        if (Objects.isNull(value)) {
            return new String[]{};
        }
        return value.split(LEVEL_1_DEVIDER);
    }

    private Set<BaseEntityDto> getBlockElementsFromMap(String name, Map<String, String> map) {
        Set<BaseEntityDto> blockElementDtos = new HashSet<>();
        try {
            String[] elementList = getStringArrayFromMap(name, map);
            for (String elementName : elementList) {
                if(!elementName.isEmpty()) {
                    // TODO: Polish the resource type: Point, Path, Location
                    BaseEntityDto elementDto = sceneDto.getPointDtoByName(elementName);
                    if (elementDto == null) {
                        elementDto = sceneDto.getPathDtoByName(elementName);
                    }
                    if (elementDto == null) {
                        elementDto = sceneDto.getLocationDtoByName(elementName);
                    }
                    if (elementDto == null) {
                        throw new IllegalArgumentException("The elements of the Block [" + elementName
                                + "] is not the three types: Point, Path, Location! ");
                    }
                    blockElementDtos.add(elementDto);
//                    String[] point = element.split(" --- ");
//                    for (String temp : point) {
//                        PointDto pointDto = sceneDto.getPointDtoByName(temp);
//                        baseEntityDtos.add(pointDto);
//                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return blockElementDtos;
    }

    private List<PointDto> getHopsFromMap(String name, Map<String, String> map) {
        List<PointDto> hops = new ArrayList<>();
        try {
            String[] pointList = getStringArrayFromMap(name, map);
            for (String pointName : pointList) {
                if(!pointName.isEmpty()) {
                    PointDto pointDto = Objects.requireNonNull(sceneDto.getPointDtoByName(pointName));
                    hops.add(pointDto);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return hops;
    }

    /**
     *
     * @param map
     * @return
     */
    public VehicleDto map2VehicleDto(Map<String,String> map){
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        vehicleDto.setInitialPoint(null);
        vehicleDto.setEnergyLevel(getDoubleFromMap("EnergyLevel",map));
        vehicleDto.setEnergyLevelCritical(getIntegerFromMap("EnergyLevelCritical",map));//(Integer.parseInt(map.get("EnergyLevelCritical").split("\\.")[0]));
        vehicleDto.setOrientationAngle(getDoubleFromMap("OrientationAngle",map));//(map.get("OrientationAngle").equals("NaN")?0:Double.parseDouble(map.get("OrientationAngle")));
        vehicleDto.setEnergyLevelGood(getIntegerFromMap("EnergyLevelGood",map));//(Integer.parseInt(map.get("EnergyLevelGood").split("\\.")[0]));
        vehicleDto.setLength(getLongFromMap("Length",map));//(Long.parseLong(map.get("Length").split("\\.")[0]));
        return vehicleDto;
    }

    private String getStringFromMap(String key,Map map){
        if(StringUtils.isEmpty(key)){
            throw new IllegalArgumentException("String is Empty :" + key + ",can not get value from XML file.");
        }
        if(Objects.isNull(map)){
            throw new NullPointerException("Map is Empty :" + map + ",can not get value from Null Object.");
        }
        String value =  (String) map.get(key);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Value is null for key: " + key);
        }
        if(value.isEmpty() ){
            if(key.equals("LABEL_OFFSET_X")){
                value = "-10";
            }else if(key.equals("LABEL_OFFSET_Y")){
                value = "-20";
            }
        }
        return value;
    }

    private Integer getIntegerFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        Integer intValue = 0;

        try {
            intValue = Integer.parseInt(value.split("\\.")[0]);
        }catch (Exception e){
            throw new IllegalArgumentException("String is  :" + key + ",can not parseInt from this String." + e.getMessage());
        }
        return intValue;
    }

    private Double getDoubleFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        Double doubleValue = 0.0;
        if(!"NaN".equals(value)){
            try {
                doubleValue = Double.parseDouble(value);
            }catch (Exception e){
                throw new IllegalArgumentException("String is  :" + key + ",can not parseDouble from this String." + e.getMessage());
            }
        }
        return doubleValue;
    }

    private Long getLongFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        Long longValue = 0l;
        try {
            longValue = Long.parseLong(value.split("\\.")[0]);
        }catch (Exception e){
            throw new IllegalArgumentException("String is  :" + key + ",can not parseLong from this String." + e.getMessage());
        }
        return longValue;
    }

    private Boolean getBooleanFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        Boolean booleanValue = false;
        try {
            booleanValue = value.equals("true");
        }catch (Exception e){
            throw new IllegalArgumentException("String is  :" + key + ",can not parseBoolean from this String." + e.getMessage());
        }
        return booleanValue;
    }

    private PointDto.Type getTypeFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        PointDto.Type type = null;
        try {
            type = PointDto.Type.valueOf(value+"_POSITION");
        }catch (Exception e){
            throw new IllegalArgumentException("String is  :" + key + ",can not parsePointDto.Type from this String." + e.getMessage());
        }
        return type;
    }

    private PointDto getPointDtoFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        PointDto pointDto = null;
        try {
            pointDto = sceneDto.getPointDtoByName(value);
        }catch (Exception e){
            throw new IllegalArgumentException("String is  key:" + key +" value:"+value+ ",can not parsePointDto from this String." + e.getMessage());
        }
        return pointDto;
    }

    private LocationDto getLocationDtoFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        LocationDto locationDto = null;
        try {
            locationDto = sceneDto.getLocationDtoByName(value);
        }catch (Exception e){
            throw new IllegalArgumentException("String is key:" + key + "  value:"+value+",can not parseLocationDto from this String." + e.getMessage());
        }
        return locationDto;
    }

    private LocationTypeDto getLocationTypeDtoFromMap(String key,Map map){
        String value = getStringFromMap(key,map);
        LocationTypeDto locationTypeDto = null;
        try {
            locationTypeDto = sceneDto.getLocationTypeDtoByName(value);
        }catch (Exception e){
            throw new IllegalArgumentException("String is  key:" + key + "  value:"+value+",can not parseLocationTypeDto from this String." + e.getMessage());
        }
        return locationTypeDto;
    }
}

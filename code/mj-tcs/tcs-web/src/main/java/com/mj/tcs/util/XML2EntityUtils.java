package com.mj.tcs.util;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.EntityAuditorDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

public class XML2EntityUtils {

    private SceneDto sceneDto;

    public SceneDto getSceneDto() {
        return sceneDto;
    }

    public void setSceneDto(SceneDto sceneDto) {
        this.sceneDto = sceneDto;
    }

    public EntityAuditorDto createAuditor() {
        EntityAuditorDto auditorDto = new EntityAuditorDto();
        auditorDto.setCreationDate();
        auditorDto.setCreatedBy("wang San");
        auditorDto.setChangeDate();
        auditorDto.setUpdatedBy("Li Si");
        return auditorDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public Object Map2Dto(Map<String,String> map, String type){
        switch (type){
            case "point":
                return sceneDto.addPointDto(Map2PointDto(map));
            case "path":
                return sceneDto.addPathDto(Map2PathDto(map));
            case "locationType":
                return sceneDto.addLocationTypeDto(Map2LocationTypeDto(map));
            case "location":
                return sceneDto.addLocationDto(Map2LocationDto(map));
            case "link":
                return Map2LocationLinkDto(map);
            case "block":
                return sceneDto.addBlockDto(Map2BlockDto(map));
            case "vehicle":
                return sceneDto.addVehicleDto(Map2VehicleDto(map));
            default:
                return null;
        }
    }
    /**
     *
     * @param map
     * @return
     */
    public PointDto Map2PointDto(Map<String,String> map){
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
        return pointDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public PathDto Map2PathDto(Map<String,String> map){
        PathDto pathDto = new PathDto();
        pathDto.setName(getStringFromMap("Name",map));
        pathDto.setMaxVelocity(getIntegerFromMap("maxVelocity",map));//(Integer.parseInt(map.get("maxVelocity").split("\\.")[0]));
        pathDto.setRoutingCost(getLongFromMap("cost",map));//((long)Double.parseDouble(map.get("cost")));
        pathDto.setSourcePointDto(getPointDtoFromMap("startComponent",map));//(sceneDto.getPointDtoByName(map.get("startComponent")));
        pathDto.setDestinationPointDto(getPointDtoFromMap("endComponent",map));//(sceneDto.getPointDtoByName(map.get("endComponent")));
        pathDto.setMaxReverseVelocity(getIntegerFromMap("maxReverseVelocity",map));//(Integer.parseInt(map.get("maxReverseVelocity").split("\\.")[0]));
        pathDto.setLength(getLongFromMap("length",map));//((long)Double.parseDouble(map.get("length")));
        pathDto.setLocked(getBooleanFromMap("locked",map));//(map.get("locked").equals("false"));
        return pathDto;
    }

    /**
     *
     endComponent	Goods in north 01

     Name	Point-0026 --- Goods in north 01
     startComponent	Point-0026

     * @param map
     * @return
     */
    public LocationLinkDto Map2LocationLinkDto(Map<String,String> map){
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
    public LocationTypeDto Map2LocationTypeDto(Map<String,String> map){
        LocationTypeDto locationTypeDto = new LocationTypeDto();
        locationTypeDto.setName(getStringFromMap("Name",map));//(map.get("Name"));
        locationTypeDto.setAllowedOperations(null);
        return locationTypeDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public LocationDto Map2LocationDto(Map<String,String> map){
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
        locationDto.setDisplayPositionY(getLongFromMap("POSITION_X",map));//(Long.parseLong(map.get("POSITION_Y")));
        locationDto.setProperties(null);

        return locationDto;
    }

    public BlockDto Map2BlockDto(Map<String,String> map){
        BlockDto locationDto = new BlockDto();

        locationDto.setName(getStringFromMap("Name",map));//(map.get("Name"));

        locationDto.setProperties(null);
        return locationDto;
    }

    /**
     *
     * @param map
     * @return
     */
    public VehicleDto Map2VehicleDto(Map<String,String> map){
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

        if(StringUtils.isEmpty(value)){
            throw new IllegalArgumentException("String is Empty :" + key + ",can not parse from Empty String.");
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
            booleanValue = value.equals("false");
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

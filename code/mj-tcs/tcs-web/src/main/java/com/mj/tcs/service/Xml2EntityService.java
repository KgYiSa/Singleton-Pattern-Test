package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.EntityAuditorDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.data.model.StaticRoute;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Xml2EntityService {

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
    public Object Map2Dto(Map<String,String> map,String type){
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
        triple.setX((int)Double.parseDouble(map.get("modelXPosition")));
        triple.setY((int)Double.parseDouble(map.get("modelYPosition")));
        triple.setZ((int)Double.parseDouble("0"));
        pointDto.setName(map.get("Name"));
        pointDto.setDisplayPositionX(Long.parseLong(map.get("POSITION_X")));
        pointDto.setDisplayPositionY(Long.parseLong(map.get("POSITION_Y")));
        pointDto.setLabelOffsetX(Long.parseLong(map.get("LABEL_OFFSET_X").length()<1?"0":map.get("LABEL_OFFSET_X")));
        pointDto.setLabelOffsetY(Long.parseLong(map.get("LABEL_OFFSET_Y").length()<1?"0":map.get("LABEL_OFFSET_X")));
        pointDto.setVehicleOrientationAngle(Double.parseDouble(map.get("vehicleOrientationAngle").equals("NaN")?"0":map.get("vehicleOrientationAngle")));
        pointDto.setPosition(triple);
        pointDto.setType(PointDto.Type.valueOf(map.get("Type")+"_POSITION"));
        return pointDto;
    }

    /**
     *
     CONN_TYPE	DIRECT
     endComponent	Point-0052
     startComponent	Point-0043
     Name	Point-0043 --- Point-0052
     Miscellaneous
     maxVelocity	1000.0
     cost	1
     maxReverseVelocity 0.0
     length	8443.0
     locked	false
     CONTROL_POINTS
     * @param map
     * @return
     */
    public PathDto Map2PathDto(Map<String,String> map){
        PathDto pathDto = new PathDto();
        pathDto.setName(map.get("Name"));
        pathDto.setMaxVelocity(Integer.parseInt(map.get("maxVelocity").split("\\.")[0]));
        pathDto.setRoutingCost((long)Double.parseDouble(map.get("cost")));
        pathDto.setSourcePointDto(sceneDto.getPointDtoByName(map.get("startComponent")));
        pathDto.setDestinationPointDto(sceneDto.getPointDtoByName(map.get("endComponent")));
        pathDto.setMaxReverseVelocity(Integer.parseInt(map.get("maxReverseVelocity").split("\\.")[0]));
        pathDto.setLength((long)Double.parseDouble(map.get("length")));
        pathDto.setLocked(map.get("locked").equals("false"));
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
        LocationDto locationDto = sceneDto.getLocationDtoByName(map.get("endComponent"));
        LocationLinkDto locationLinkDto = new LocationLinkDto();
        locationLinkDto.setName(map.get("Name"));
        locationLinkDto.setPointDto(sceneDto.getPointDtoByName(map.get("startComponent")));
        locationLinkDto.setLocationDto(sceneDto.getLocationDtoByName(map.get("endComponent")));
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
        locationTypeDto.setName(map.get("Name"));
        locationTypeDto.setAllowedOperations(null);
        return locationTypeDto;
    }

    /**
     *
     modelXPosition	-28000.0
     Name	Goods in north 02
     POSITION_Y	6000
     *LABEL_OFFSET_X	-10
     *LABEL_OFFSET_Y	-20
     modelYPosition	6000.0
     *Type	Transfer station
     POSITION_X	-28000
     * @param map
     * @return
     */
    public LocationDto Map2LocationDto(Map<String,String> map){
        LocationDto locationDto = new LocationDto();
        TripleDto triple = new TripleDto();
        triple.setX((int)Double.parseDouble(map.get("modelXPosition")));
        triple.setY((int)Double.parseDouble(map.get("modelYPosition")));
        triple.setZ((int)Double.parseDouble("0"));
        locationDto.setName(map.get("Name"));
        locationDto.setPosition(triple);
        locationDto.setLabelOffsetX(Long.parseLong(map.get("LABEL_OFFSET_X")));
        locationDto.setLabelOffsetY(Long.parseLong(map.get("LABEL_OFFSET_Y")));
        locationDto.setLocationTypeDto(sceneDto.getLocationTypeDtoByName(map.get("Type")));//Type
        locationDto.setDisplayPositionX(Long.parseLong(map.get("POSITION_X")));
        locationDto.setDisplayPositionY(Long.parseLong(map.get("POSITION_Y")));
        locationDto.setProperties(null);

        return locationDto;
    }

    public BlockDto Map2BlockDto(Map<String,String> map){
        BlockDto locationDto = new BlockDto();

        locationDto.setName(map.get("Name"));

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
        vehicleDto.setName(map.get("Name"));
        vehicleDto.setInitialPoint(null);
        vehicleDto.setEnergyLevel(Double.parseDouble(map.get("EnergyLevel")));
        vehicleDto.setEnergyLevelCritical(Integer.parseInt(map.get("EnergyLevelCritical").split("\\.")[0]));
        vehicleDto.setOrientationAngle(map.get("OrientationAngle").equals("NaN")?0:Double.parseDouble(map.get("OrientationAngle")));
        vehicleDto.setEnergyLevelGood(Integer.parseInt(map.get("EnergyLevelGood").split("\\.")[0]));
        vehicleDto.setLength(Long.parseLong(map.get("Length").split("\\.")[0]));
        return vehicleDto;
    }
}

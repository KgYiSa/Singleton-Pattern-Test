package com.mj.tcs.util;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.EntityAuditorDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Wang Zhen
 */
public class SceneDtoModelGenerator {
    private static final String SCENE_NAME_PREFIX = "test_scene_";
    private static final String SCENE_NAME_SUFFIX_FORMAT = "yy_MM_dd_HH_mm_ss";

    private static final String SCENE_POST_URI_FORMAT = "http://localhost:8080/api/v1/scenes/";

    private static final String POINT_NAME_FORMAT = "test_point_%d";

    private static final String PATH_NAME_FORMAT = "test_path_%d";

    private static final String VEHICLE_NAME_FORMAT = "test_vehicle_%d";

    private static int SCENE_ID = 0;

    @Autowired
    private final RestTemplate restTemplate = new RestTemplate();

    private Set<PointDto> pointDtos = new LinkedHashSet<>();

    private Set<PathDto> pathDtos = new LinkedHashSet<>();

    public synchronized PointDto createPointDto() {
        long id = pointDtos.size();

        TripleDto tripleDto = new TripleDto(new Random().nextInt(1000),
                new Random().nextInt(1000), 0);
        tripleDto.setAuditorDto(createAuditor());

        String name = String.format(POINT_NAME_FORMAT, id);

        PointDto pointDto = new PointDto();
        pointDto.setAuditorDto(createAuditor());
//        pointDto.setId(id);
        pointDto.setName(name);
        pointDto.setPosition(tripleDto);
        pointDto.setDisplayPositionX(tripleDto.getX());
        pointDto.setDisplayPositionY(tripleDto.getY());
        pointDto.setLabelOffsetX(0L);
        pointDto.setLabelOffsetY(20L);
        
        pointDtos.add(pointDto);

        return pointDto;
    }

    public synchronized PathDto createPathDto(PointDto srcPointDto, PointDto dstPointDto) {
        PathDto pathDto = new PathDto();

        long id = pathDtos.size();
        String name = String.format(PATH_NAME_FORMAT, id);

        pathDto.setAuditorDto(createAuditor());
//        pathDto.setId(id);
        pathDto.setName(name);
        pathDto.setSourcePointDto(srcPointDto);
        pathDto.setDestinationPointDto(dstPointDto);
        long length = new Random().nextInt(1000);
        length = length < 10 ? 10 : length;
        pathDto.setLength(length);
        pathDto.setLocked(false);
        pathDto.setMaxReverseVelocity(1);
        pathDto.setMaxVelocity(1);

        srcPointDto.addOutgoingPath(pathDto);
        dstPointDto.addIncomingPath(pathDto);

        pathDtos.add(pathDto);

        return pathDto;
    }

    public synchronized LocationDto createLocationDto(PointDto pointDto, LocationTypeDto locationTypeDto) {
        LocationDto locationDto = new LocationDto();
        long id = new Random().nextInt(10000);
        String name = String.format("test_location_%d", id);

        locationDto.setAuditorDto(createAuditor());
        locationDto.setName(name);
        TripleDto newTripleDto = new TripleDto();
        newTripleDto.setAuditorDto(createAuditor());
        newTripleDto.setX(100);
        newTripleDto.setY(100);
        newTripleDto.setZ(0L);
        locationDto.setPosition(newTripleDto);
        locationDto.setDisplayPositionX(newTripleDto.getX());
        locationDto.setDisplayPositionY(newTripleDto.getY());
        locationDto.setLabelOffsetX(0L);
        locationDto.setLabelOffsetY(20L);
        locationDto.setLocationTypeDto(locationTypeDto);

        // link
        LocationLinkDto linkDto = new LocationLinkDto(locationDto, pointDto);
//        linkDto.setLocationDto(locationDto);
//        linkDto.setPointDto(pointDto);
        linkDto.setAuditorDto(createAuditor());
        String linkName = String.format("test_link_%d", id);
        linkDto.setName(linkName);
        locationDto.attachLink(linkDto);
        return locationDto;
    }

    public synchronized LocationTypeDto createLocationTypeDto() {
        LocationTypeDto locationTypeDto = new LocationTypeDto();
        long id = new Random().nextInt(10000);
        String name = String.format("test_lt_%d", id);

        locationTypeDto.setAuditorDto(createAuditor());
//        setId(createPathCommand, id);
        locationTypeDto.setName(name);
        locationTypeDto.addAllowedOperation("Puts in storage");
        locationTypeDto.addAllowedOperation("Stock removal");

        return locationTypeDto;
    }

    public synchronized SceneDto createSceneDto() {
        String newSceneName = SCENE_NAME_PREFIX +
                new SimpleDateFormat(SCENE_NAME_SUFFIX_FORMAT).format(new Date()).toString();
        System.out.println(newSceneName);

        SceneDto sceneDto = new SceneDto();
        sceneDto.setAuditorDto(createAuditor());
        sceneDto.setName(newSceneName);

        // Points & Paths
        for (int i=0; i< 5; i++) {
            createPointDto();

            if (i != 0 && i % 2 == 1) {
                PointDto sourcePoint = (PointDto) pointDtos.toArray()[i-1];
                PointDto destinationPoint = (PointDto) pointDtos.toArray()[i];

                createPathDto(sourcePoint, destinationPoint);
            }
        }

        sceneDto.setPointDtos(pointDtos);
        sceneDto.setPathDtos(pathDtos);

//        LocationTypeDto locationTypeDto = createLocationTypeDto();
//        sceneDto.addLocationTypeDto(locationTypeDto);
//
//        LocationDto locationDto = createLocationDto((PointDto) pointDtos.toArray()[0], locationTypeDto);
//        sceneDto.addLocationDto(locationDto);
//
//        List<PointDto> hops = new ArrayList<>();
//        hops.add(((PointDto) pointDtos.toArray()[0]));
//        hops.add(((PointDto) pointDtos.toArray()[1]));
//        hops.add(((PointDto) pointDtos.toArray()[2]));
//        StaticRouteDto staticRouteDto = createStaticRouteDto(hops);
//        sceneDto.addStaticRouteDto(staticRouteDto);

        return sceneDto;
    }

    public synchronized StaticRouteDto createStaticRouteDto(List<PointDto> hops) {
        StaticRouteDto staticRouteDto = new StaticRouteDto();
        long id = new Random().nextInt(10000);
        String newStaticRouteName = String.format("test_static_route_%d",
                id);

        staticRouteDto.setAuditorDto(createAuditor());

        staticRouteDto.setName(newStaticRouteName);
        staticRouteDto.setHops(hops);

        return staticRouteDto;
    }

    public synchronized TransportOrderDto createTransportOrderDto(long destPointId) {
        long id = new Random().nextInt(1000);

        TransportOrderDto transportOrderDto = new TransportOrderDto();
        transportOrderDto.setAuditorDto(createAuditor());

        transportOrderDto.setName(String.format("test_to_%d", id));
        transportOrderDto.setDeadline(new Date().getTime());
        transportOrderDto.setIntendedVehicle(0);

        // Destination
        DestinationDto destinationDto = new DestinationDto();
        destinationDto.setAuditorDto(createAuditor());
        destinationDto.setLocationId(destPointId); // TODO: Point-Name
        destinationDto.setDummy(true);
        destinationDto.setOperation("NOP");

        transportOrderDto.addDestionation(destinationDto);

        return transportOrderDto;
    }

    public synchronized VehicleDto createVehicleDto() {
        String newVehicleName = String.format(VEHICLE_NAME_FORMAT,
                new Random().nextInt(10000));
        System.out.println(newVehicleName);

        VehicleDto dto = new VehicleDto();
        dto.setAuditorDto(createAuditor());
        dto.setName(newVehicleName);
        dto.setLength(1000);
        dto.setEnergyLevel(80);
        dto.setEnergyLevelCritical(30);
        dto.setEnergyLevelGood(80);
        dto.setRechargeOperation("RECHARGE");
        dto.setMaxVelocity(100);
        dto.setMaxReverseVelocity(100);
        return dto;
    }

//    public static void main(String[] args) {
//        SceneDto sceneEntityDto = new SceneDtoModelGenerator().createSceneDto();
//
//
//        String scenePostUri = String.format(SCENE_POST_URI_FORMAT, sceneEntityDto.getName());
//        System.out.println(scenePostUri);
//
//        // SENDING TO SERVER
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<SceneDto> entity = new HttpEntity<>(sceneEntityDto, headers);

//        ResponseEntity<Object> response = restTemplate.exchange(scenePostUri, HttpMethod.POST, entity, Object.class);//restTemplate.postForObject(scenePostUri, scene, ResponseEntity.class);

//        if (response.getStatusCode() == HttpStatus.CREATED) {
//            System.out.println("Create new table [" + response.getBody() + "] successfully!");
//            System.out.println(response.getBody().toString());
//        } else {
//            System.out.println("Create new table failed.");
//        }
//    }

    private synchronized EntityAuditorDto createAuditor() {
        EntityAuditorDto auditorDto = new EntityAuditorDto();
        auditorDto.setCreationDate();
        auditorDto.setChangeDate();
        auditorDto.setCreatedBy("Zhang San");
        auditorDto.setUpdatedBy("Li Si");
        return auditorDto;
    }
}

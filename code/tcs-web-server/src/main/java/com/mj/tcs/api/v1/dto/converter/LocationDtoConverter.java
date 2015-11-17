package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.LocationDto;
import com.mj.tcs.api.v1.dto.LocationLinkDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.LocationLinkDto2LocationLinkMatcher;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.util.TcsBeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
@Component(value = "LocationDtoConverter")
public class LocationDtoConverter implements DtoConverter  {

    private static final Map<String, Object> locationConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
//        locationConverters.put("longToPath", new Numeric2PathConverter());
//        locationConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());
        locationConverters.put("LocationLinkDto2LocationLinkMatcher", new LocationLinkDto2LocationLinkMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point",
                "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");

        beanFactory.registerEntity("LocationType", "com.mj.tcs.data.model.LocationType",
                "com.mj.tcs.data.model.LocationType");
        beanFactory.registerDto("LocationTypeDto", "com.mj.tcs.api.v1.dto.LocationTypeDto");

        beanFactory.registerEntity("Location.Link", "com.mj.tcs.data.model.Location$Link",
                "com.mj.tcs.data.model.Location$Link");
        beanFactory.registerDto("LocationLinkDto", "com.mj.tcs.api.v1.dto.LocationLinkDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Location) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        LocationDto locationDto = new LocationDto();
        Location location = (Location) entity;

        DTOAssembler.newAssembler(locationDto.getClass(), location.getClass())
                .assembleDto(locationDto, location, locationConverters, beanFactory);

        if (locationDto.getAttachedLinks() != null) {
            for (LocationLinkDto linkDto : locationDto.getAttachedLinks()) {
                linkDto.setLocationDto(locationDto);

                Optional<Location.Link> link = location.getAttachedLinkById(linkDto.getId());
                if (!link.isPresent()) {
                    throw new TcsServerRuntimeException("link entity is not found during conversion for ID " + linkDto.getId());
                }
                linkDto.setPointId(link.get().getPoint().getId());
            }
        }

        if (location.getType() != null) {
            locationDto.setLocationTypeId(location.getType().getId());
        }

        return locationDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof LocationDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        LocationDto locationDto = (LocationDto) dto;
        Location location = new Location();

        DTOAssembler.newAssembler(locationDto.getClass(), location.getClass())
                .assembleEntity(locationDto, location, locationConverters, beanFactory);

        if (location.getAttachedLinks() != null) {
            for (Location.Link link : location.getAttachedLinks()) {
                link.setLocation(location);
                // convert outside
//                link.setPoint();
            }
        }
        return location;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof LocationDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(dto, properties);

        return dto;
    }

    @Override
    public boolean canMergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {

        if (entity != null && entity instanceof Location) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(entity, properties);

        return entity;
    }
}

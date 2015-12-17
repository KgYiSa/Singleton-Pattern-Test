package com.mj.tcs.util;

import com.mj.tcs.api.v1.dto.BlockDto;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.ResourceUnknownException;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
public class TcsDtoUtils {

    public static SceneDto resolveSceneDtoRelationships(SceneDto sceneDto) {
        if (sceneDto.getPointDtos() != null) {
            sceneDto.getPointDtos().forEach(p -> {
                p.setSceneDto(sceneDto);

                if (p.getIncomingPaths() != null) {
                    p.setIncomingPaths(p.getIncomingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setDestinationPointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
                if (p.getOutgoingPaths() != null) {
                    p.setOutgoingPaths(p.getOutgoingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setSourcePointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
                // TODO: We do NOT need to contains the raw links, just use the LocationDto->LocationLinkDto to do the associations
                if (p.getAttachedLinks() != null) {
                    p.setAttachedLinks(new LinkedHashSet<>());
                }
            });
        }
        if (sceneDto.getPathDtos() != null) {
            sceneDto.getPathDtos().forEach(p -> {
                p.setSceneDto(sceneDto);

                // CHECK RELATIONSHIP (Already linked in points' settings) TODO?
                if (p.getSourcePointDto() == null || p.getDestinationPointDto() == null) {
                    throw new ObjectUnknownException("Path : " + p.getName() + " point(s) is null!");
                }
            });
        }
        if (sceneDto.getLocationTypeDtos() != null) {
            sceneDto.getLocationTypeDtos().forEach(t -> t.setSceneDto(sceneDto));
        }
        if (sceneDto.getLocationDtos() != null) {
            sceneDto.getLocationDtos().forEach(l -> {
                l.setSceneDto(sceneDto);
                if (l.getAttachedLinks() != null) {
                    l.getAttachedLinks().forEach(li -> {
                        li.setSceneDto(sceneDto);

                        PointDto linkedPointDto = Objects.requireNonNull(sceneDto.getPointDtoByUUID(li.getPointDto().getUUID()));
                        li.setPointDto(linkedPointDto);
                        linkedPointDto.addAttachedLinks(li);
                        li.setLocationDto(sceneDto.getLocationDtoByUUID(li.getLocationDto().getUUID()));
                    });
                }

                l.setLocationTypeDto(sceneDto.getLocationTypeDtoByUUID(l.getLocationTypeDto().getUUID()));
            });
        }
        if (sceneDto.getBlockDtos() != null) {
            sceneDto.getBlockDtos().forEach(b -> {
                b.setSceneDto(sceneDto);

                // UUID -> Point/Path/Block
                if (b.getMembers() != null) {
                    Set<BlockDto.BlockElementDto> elementDtos = new LinkedHashSet<>();
                    for (BlockDto.BlockElementDto mem : b.getMembers()) {
                        String uuid = Objects.requireNonNull(mem.getUUID());

                        // Point
                        BaseEntityDto memDto = sceneDto.getPointDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Path
                        memDto = sceneDto.getPathDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Location
                        memDto = sceneDto.getLocationDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        throw new ResourceUnknownException("Resource: " + uuid + " not found!");
                    }
                    b.setMembers(elementDtos);
                }
            });
        }
        if (sceneDto.getStaticRouteDtos() != null) {
            sceneDto.getStaticRouteDtos().forEach(r -> {
                r.setSceneDto(sceneDto);

                r.setHops(r.getHops().stream().map(p -> sceneDto.getPointDtoByUUID(p.getUUID())).collect(Collectors.toList()));
            });
        }
        if (sceneDto.getVehicleDtos() != null) {
            sceneDto.getVehicleDtos().forEach(v -> {
                v.setSceneDto(sceneDto);

                if (v.getInitialPoint() != null) {
                    v.setInitialPoint(sceneDto.getPointDtoByUUID(v.getInitialPoint().getUUID()));
                }
            });
        }

        return sceneDto;
    }


    /**
     * Copy from one class object (or a Map object) to another class object.
     *
     * @param dst
     * @param src
     */
    public static void copyProperties(Object dst, Object src) throws RuntimeException {
        try {
            if (src instanceof Map) {
                Iterator names = ((Map) src).keySet().iterator();
                while (names.hasNext()) {
                    String name = (String) names.next();
                    if (PropertyUtils.isWriteable(dst, name)) {
                        Object value = ((Map)src).get(name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dst, name, value);
                        }
                    }
                }
            } else {
                Field[] fields = src.getClass().getDeclaredFields();
                for (int i=0; i<fields.length; i++) {
                    String name = fields[i].getName();
                    if (PropertyUtils.isReadable(src, name) && PropertyUtils.isWriteable(dst, name)) {
                        Object value = PropertyUtils.getSimpleProperty(src, name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dst, name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("copyProperties error from " + src.toString() + " to " + dst.toString(),
                    e);
        }
    }

    public static boolean checkObjProperty(Object src, Map dst) throws RuntimeException {
        try {
            Field[] fields = src.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (!dst.containsKey(name)) {
                    if (PropertyUtils.isReadable(src, name)) {
                        Object value = PropertyUtils.getSimpleProperty(src, name);
                        if (value == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("checkObjProperty error", e);
        }
    }
}

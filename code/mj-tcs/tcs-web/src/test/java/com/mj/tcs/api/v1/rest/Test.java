package com.mj.tcs.api.v1.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.util.SceneDtoModelGenerator;

import java.io.IOException;

/**
 * @author Wang Zhen
 */
public class Test {
    public static void main(String[] args) throws IOException {
        SceneDtoModelGenerator generator = new SceneDtoModelGenerator();

        SceneDto dto = generator.createSceneDto();
//        TcsSockRequestEntity dto = new TcsSockRequestEntity(TcsSockRequestEntity.Action.SCENE_PROFILE,"yy");
//        TcsSockResponseEntity dto = new TcsSockResponseEntity(TcsSockResponseEntity.Status.ERROR, "yy", "hello");
//        dto.setUUID("xxxx");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String json = mapper.writeValueAsString(dto);
//        TcsSockRequestEntity dto2 = mapper.readValue(json, TcsSockRequestEntity.class);
//        TcsSockResponseEntity dto2 = mapper.readValue(json, TcsSockResponseEntity.class);
        System.out.println(json);
        SceneDto dto2 = mapper.readValue(json, SceneDto.class);
//        if (dto2.getPointDtos() != null) {
//            dto2.getPointDtos().forEach(p -> p.setSceneDto(dto));
//        }
    }
}

package com.mj.tcs.api.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.mj.tcs.api.dto.SceneDto;
import com.mj.tcs.util.SceneDtoModelGenerator;

import java.io.IOException;

/**
 * @author Wang Zhen
 */
public class Test {
    public static void main(String[] args) throws IOException {
        SceneDtoModelGenerator generator = new SceneDtoModelGenerator();

        SceneDto dto = generator.createSceneDto();
//        TCSRequestEntity dto = new TCSRequestEntity(TCSRequestEntity.Action.SCENE_PROFILE,"yy");
//        TCSResponseEntity dto = new TCSResponseEntity(TCSResponseEntity.Status.ERROR, "yy", "hello");
//        dto.setUUID("xxxx");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String json = mapper.writeValueAsString(dto);
//        TCSRequestEntity dto2 = mapper.readValue(json, TCSRequestEntity.class);
//        TCSResponseEntity dto2 = mapper.readValue(json, TCSResponseEntity.class);
        System.out.println(json);
        SceneDto dto2 = mapper.readValue(json, SceneDto.class);
//        if (dto2.getPointDtos() != null) {
//            dto2.getPointDtos().forEach(p -> p.setSceneDto(dto));
//        }
    }
}

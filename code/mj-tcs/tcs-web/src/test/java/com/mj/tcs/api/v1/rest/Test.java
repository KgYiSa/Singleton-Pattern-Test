package com.mj.tcs.api.v1.rest;

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

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);
        SceneDto dto2 = mapper.readValue(json, SceneDto.class);
        if (dto2.getPointDtos() != null) {
            dto2.getPointDtos().forEach(p -> p.setSceneDto(dto));
        }
        System.out.println(json);
    }
}

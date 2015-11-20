package com.mj.tcs.api.v1.dto.resource;

import com.mj.tcs.api.v1.dto.SceneDto;
import org.springframework.hateoas.Resource;

/**
 * @author Wang Zhen
 */
public class SceneDtoResource extends Resource{

//    private final Scene scene;

    public SceneDtoResource(SceneDto scene) {
        super(scene);

//        this.scene = scene;
//        Long listId = this.scene.getId();

        //TODO: Meaningful
        // href, ref (default is None)
//        add(linkTo(methodOn(SceneController.class).getAllScenes()).withSelfRel());
//        add(new LinkDto("http://localhost:8080/getAllScenes"));
//        add(new LinkDto("http://localhost:8080/scene/0", "item"));
    }

//    public Scene getSceneDto() {
//        return scene;
//    }
}

package com.mj.tcs.api.v1.dto.resource;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.web.SceneController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Used to convert a Scene object to the corresponding SceneDtoResource object.
 *
 * @author Wang Zhen
 */
public class SceneDtoResourceAssembler extends ResourceAssemblerSupport<SceneDto, SceneDtoResource> {
    public SceneDtoResourceAssembler() {
        super(SceneController.class, SceneDtoResource.class);
    }

    @Override
    public SceneDtoResource toResource(SceneDto scene) {
        if (scene == null) {
            throw new NullPointerException("SceneDtoResourceAssembler: scene is null!");
        }

        SceneDtoResource resource = createResourceWithId(scene.getId(), scene);
        return resource;
    }

    @Override
    protected SceneDtoResource instantiateResource(SceneDto entity) {
        return new SceneDtoResource(entity);
    }
}

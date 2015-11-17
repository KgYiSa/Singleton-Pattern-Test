package com.mj.tcs.service.modelling;

import com.mj.tcs.util.TcsStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
public class ServiceGetParams {
    public static final String NAME_SCENE_ID = "sceneId"; // scene_id also available
    public static final String NAME_ELEMENT_ID = "elementID"; // point, path, vehicle, locationType ...
//    public static final String NAME_POINT_ID = "pointID"; // point_id ...
//    public static final String NAME_PATH_ID = "pathId"; // path_id ...
//    public static final String NAME_VEHICLE_ID = "vehicleId"; // vehicle_id ...

    // TODO: The following features undone
    public static final String NAME_LIMIT = "limit"; // int: specifies the number of resources to be returned.
    public static final String NAME_OFFSET = "offset"; // int: specifies the start position of resources to be returned

    public static final String NAME_PAGE_START = "page"; // int: start page
    public static final String NAME_ELEMENTS_PER_PAGE = "perPage"; // int: the recorded number for each page

    public static final String NAME_SORTED_BY = "sortedBy"; // string: the property to be used to sort
    public static final String NAME_ORDER = "order"; // string: asc

    //
    private Map<String, Object> parameters = new HashMap<>();

    private GetType type = GetType.GET_UNKNOW;

    public GetType getType() {
        return type;
    }

    public ServiceGetParams setType(GetType type) {
        this.type = type;

        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public ServiceGetParams addParameter(String name, Object value) {
        this.parameters.put(name, value);

        return this;
    }

    public Object getParameter(String name) {
        Object answer = this.parameters.get(name);
        if (answer == null) {
            answer = this.parameters.get(TcsStringUtils.toUnderlineString(name));
        }

        return answer;
    }

    public enum GetType {
        GET_UNKNOW,
        GET_ONE_BY_ELEMENT_ID,
        GET_ALL,
        GET_ALL_BY_SCENE_ID
    }
}

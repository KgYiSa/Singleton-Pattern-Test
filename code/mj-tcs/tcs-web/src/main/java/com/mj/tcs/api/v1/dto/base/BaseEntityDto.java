package com.mj.tcs.api.v1.dto.base;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.Identifiable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class BaseEntityDto implements Identifiable<Long>, Serializable {
    /**
     * Each type in the same scene should be unique!!!
     * To accept same names in the same scene in that way (Not Recommended).
     *
     * It is not the same as the one in database, it is just used to identify the component from the others.
     */
    @DtoField
    private Long id;
    @DtoField
    private Long version;
    private Map<String, Object> properties = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Add property. It can be used to put any unknown propery during deSerialization.
     *
     * @param name
     * @param value
     */
    @JsonAnySetter
    public void addProperty(String name, Object value) {
        properties.put(name, value);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);
        properties.forEach((id, val) -> stringBuilder.append(id, val));
        return stringBuilder.toString();
    }
}

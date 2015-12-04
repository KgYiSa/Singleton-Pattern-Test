package com.mj.tcs.api.v1.dto.base;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Embeddable
public class EntityProperty implements Serializable, Cloneable {
    @Column
    private String name;

    @Column
    private String value;

    @Column
    private String type;

    public String getName() {
        return name;
    }

    public EntityProperty setName(String name) {
        this.name = Objects.requireNonNull(name);

        return this;
    }

    public String getValue() {
        return value;
    }

    public EntityProperty setValue(String value) {
        this.value = Objects.requireNonNull(value);
        return this;
    }

    public String getType() {
        return type;
    }

    public EntityProperty setType(String type) {
        this.type = Objects.requireNonNull(type);
        return this;
    }

//    public Object getValueByType() throws ClassNotFoundException {
//        return Class.forName(this.type).cast(value);
//    }
}

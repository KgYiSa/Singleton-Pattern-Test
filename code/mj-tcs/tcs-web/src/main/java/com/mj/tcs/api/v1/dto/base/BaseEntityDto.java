package com.mj.tcs.api.v1.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
@Dto
@MappedSuperclass
public class BaseEntityDto implements Serializable, Cloneable {

    @JsonProperty("UUID")
//    @Transient
    private /*final */String uuid;

    /**
     * Each type in the same scene should be unique!!!
     * To accept same names in the same scene in that way (Not Recommended).
     *
     * It is not the same as the one in database, it is just used to identify the component from the others.
     */
    @DtoField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(name = "version", nullable = false)
    @Version
    private Long version;

//    @JsonProperty("auditor")
    @JsonIgnore // TODO: [TBD] Ignore or display in debug mode???
    @Embedded
    private EntityAuditorDto auditorDto = null;

    public BaseEntityDto() {
        uuid = this.getClass().getSimpleName().toLowerCase() + "-" + UUID.randomUUID().toString();
    }

    public String getUUID() {
        return uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void clearId() {
        this.id = null;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public EntityAuditorDto getAuditorDto() {
        return auditorDto;
    }

    public void setAuditorDto(EntityAuditorDto auditorDto) {
        this.auditorDto = auditorDto;
    }

    @Override
    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);
        // TODO
//        properties.forEach(p -> stringBuilder.append(p.getName(), p.getValue()));
        return stringBuilder.toString();
    }
}

package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.mj.tcs.config.ServiceGateFactory;
import com.mj.tcs.data.model.Path;
import com.mj.tcs.exception.TcsServerRuntimeException;

/**
 * @author Wang Zhen
 */
public class Numeric2PathConverter implements ValueConverter {

    @Override
    public Object convertToDto(Object object, BeanFactory beanFactory) {
        // Path class -> path id
        if (object == null) {
            throw new NullPointerException("path object is null");
        }

        if (!(object instanceof Path)) {
            throw new TcsServerRuntimeException("the class type is not Path class");
        }

        return ((Path)object).getId();
    }

    @Override
    public Object convertToEntity(Object object, Object oldEntity, BeanFactory beanFactory) {
        // Path id -> path class
        if (object == null) {
            throw new TcsServerRuntimeException("path id is null");
        }

        if (!(object instanceof Number)) {
            throw new TcsServerRuntimeException("the class type is not Number class");
        }

        return ServiceGateFactory.getServiceGateway().getModellingService().getPath((Long)object);
    }
}

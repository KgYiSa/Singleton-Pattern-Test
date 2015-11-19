/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.service.modelling;

import com.mj.tcs.data.base.BaseEntity;

import java.util.Collection;

/**
 * Created by xiaobai on 2015/8/23.
 */
public interface IEntityService<T extends BaseEntity> {

    public boolean canSupportEntityClass(Class entityClass);

    /**
     * Http GET, list all
     * @param params
     * @return
     */
    public Collection<T> get(ServiceGetParams params);

    /**
     * Http POST, create a new valueconverter.
     *
     * @param entity
     */
    public T create(T entity);

    /**
     *
     * Http PUT
     * @param entity
     */
    public T update(T entity);

    /**
     *
     * Http DELETE
     * @param id
     */
    public void delete(Long id);
}

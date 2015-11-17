package com.mj.tcs.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Retrieve & Update only
 *
 * Created by xiaobai on 2015/8/25.
 */
@NoRepositoryBean
public interface RURepository<T, ID extends Serializable> extends Repository<T, ID> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * Please do <B>NOT</B> create a new one here, it is used only for updating to keep the entity integrity.
     *
     * @param entity
     * @return the saved entity
     */
    <S extends T> S save(S entity);

    /**
     * Saves all given entities.
     *
     * Please do <B>NOT</B> create a new one here, it is used only for updating to keep the entity integrity.
     *
     * @param entities
     * @return the saved entities
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    <S extends T> Iterable<S> save(Iterable<S> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    T findOne(ID id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false} otherwise
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    boolean exists(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Returns all instances of the type in the scene.
     *
     * @param scene the ID of the scene
     * @return all entities of the scene
     */
    Iterable<T> findAllByScene(/*@Param("sceneId")*/ ID scene);

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    Iterable<T> findAll(Iterable<ID> ids);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    long count();
}

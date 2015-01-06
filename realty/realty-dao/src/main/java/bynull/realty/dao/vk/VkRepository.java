package bynull.realty.dao.vk;

import bynull.realty.dto.vk.ItemDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by trierra on 12/24/14.
 */
public interface VkRepository extends JpaRepository<ItemDTO, Long> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity
     * @return the saved entity
     */
    @Override
    <S extends ItemDTO> S save(S entity);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param aLong must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false} otherwise
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    boolean exists(Long aLong);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param aLong must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    @Override
    void delete(Long aLong);

    /**
     * Deletes a given entity.
     *
     * @param entity
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    @Override
    void delete(ItemDTO entity);

    /**
     * Retrieves an entity by its id.
     *
     * @param aLong must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    ItemDTO findOne(Long aLong);

    /**
     * Returns a reference to the entity with the given identifier.
     *
     * @param aLong must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see EntityManager#getReference(Class, Object)
     */
    @Override
    ItemDTO getOne(Long aLong);

    @Override
    List<ItemDTO> findAll();

    @Override
    List<ItemDTO> findAll(Sort sort);

    @Override
    List<ItemDTO> findAll(Iterable<Long> longs);
}

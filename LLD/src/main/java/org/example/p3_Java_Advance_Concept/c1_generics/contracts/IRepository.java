package org.example.p3_Java_Advance_Concept.c1_generics.contracts;

/**
 * Generic repository interface for CRUD operations on entities.
 * <p>
 * This interface demonstrates a common design pattern where generics
 * allow one interface to work with any entity type and ID type.
 * </p>
 *
 * <h3>Type Parameters:</h3>
 * <ul>
 * <li>{@code T} - The entity type (e.g., User, Product, Order)</li>
 * <li>{@code ID} - The identifier type (e.g., Long, String, UUID)</li>
 * </ul>
 *
 * <h3>Example Implementation:</h3>
 * 
 * <pre>{@code
 * public class UserRepository implements IRepository<User, Long> {
 *     &#64;Override
 *     public User findById(Long id) { ... }
 * }
 * }</pre>
 *
 * @param <T>  the type of entity managed by this repository
 * @param <ID> the type of the entity's unique identifier
 */
public interface IRepository<T, ID> {

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier
     * @return the entity, or null if not found
     */
    T findById(ID id);

    /**
     * Saves an entity (creates or updates).
     *
     * @param entity the entity to save
     */
    void save(T entity);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the unique identifier of the entity to delete
     * @return true if entity was deleted, false if not found
     */
    boolean delete(ID id);

    /**
     * Returns the count of all entities.
     *
     * @return the total number of entities
     */
    int count();
}

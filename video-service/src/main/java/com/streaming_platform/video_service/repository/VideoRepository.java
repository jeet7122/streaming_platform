package com.streaming_platform.video_service.repository;

import com.streaming_platform.video_service.model.Status;
import com.streaming_platform.video_service.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Video} entity.
 *
 * <p>This interface extends {@link JpaRepository} to provide standard CRUD
 * operations and query execution for the {@code videos} table.
 *
 * <p><b>Key Features:</b>
 * <ul>
 *     <li>Built-in CRUD operations (save, findById, delete, etc.)</li>
 *     <li>Pagination and sorting support</li>
 *     <li>Custom query methods using Spring Data JPA conventions</li>
 * </ul>
 *
 * <p><b>Usage:</b>
 * <ul>
 *     <li>Injected into service layer for database interactions</li>
 *     <li>Used to fetch and persist video records</li>
 * </ul>
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    /**
     * Retrieves all videos uploaded by a specific user.
     *
     * <p>This method uses Spring Data JPA's method naming convention
     * to automatically generate the query based on the {@code userId} field.
     *
     * @param userId the unique identifier of the user
     * @return list of videos associated with the given user
     */
    List<Video> findByUserId(String userId);

    @Query("""
    SELECT v FROM Video v
    WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY v.createdAt DESC
    """)
    List<Video> search(String query);

    Page<Video> findByStatus(Status status, Pageable pageable);
}
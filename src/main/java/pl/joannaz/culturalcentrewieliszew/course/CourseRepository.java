package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 1. sposób: SELECT * FROM student WHERE name = ?
    // 2. sposób: @Query("SELECT c FROM Course c WHERE c.name = ?1")
    // it uses Java Persistence Query Language - JPQL
    Optional<Course> findCourseByName(String name);

    @Query("SELECT c FROM Course c " +
            "LEFT JOIN CourseDetails cd ON c.id = cd.id " +
            "WHERE (:minimumAge IS NULL OR cd.minAge = :minimumAge) " +
            "AND (:maximumAge IS NULL OR cd.maxAge = :maximumAge) " +
            "AND (:price IS NULL OR cd.price <= :price)" +
            "AND (:teacher IS NULL OR c.teacher = :teacher) " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:name IS NULL OR c.name = :name)" +
            "AND (:location IS NULL OR cd.address.city = :location)")
            // +
            //"AND (:date IS NULL OR cd.date = :date)") <- wymaga zmiany formatu daty, np. na dzień, godziny przedpołudniowe/popołudniowe
    List<Course> findCoursesByCriteria(
            @Param("minimumAge") Integer minimumAge,
            @Param("maximumAge") Integer maximumAge,
            @Param("price") BigDecimal price,
            @Param("teacher") String teacher,
            @Param("category") Category category,
            @Param("name") String name,
            @Param("location") String location
    );
}

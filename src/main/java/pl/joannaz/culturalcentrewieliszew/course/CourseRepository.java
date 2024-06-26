package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.course.CourseBasicInfo(c.id, c.name) " +
    "FROM Course c WHERE c.teacher.id = :teacherId")
    List<CourseBasicInfo> findCourseNamesByTeacherId(@Param("teacherId") UUID teacherId);

    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId")
    List<Course> findTeacherCourses(@Param("teacherId") UUID teacherId);

    @Query("SELECT c FROM Course c " +
            "LEFT JOIN CourseDetails cd ON c.id = cd.id " +
            "LEFT JOIN cd.address a " +
            "WHERE (:minimumAge IS NULL OR cd.minAge >= :minimumAge) " +
            "AND (:maximumAge IS NULL OR cd.maxAge <= :maximumAge) " +
            "AND (:price IS NULL OR cd.price <= :price)" +
            "AND (cast(:teacherId as uuid) IS NULL OR c.teacher.id = :teacherId) " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:name IS NULL OR c.name = :name) " +
            "AND (:location IS NULL OR a.id = :location)"
    )
    List<Course> findCoursesByCriteria(
            @Param("minimumAge") Integer minimumAge,
            @Param("maximumAge") Integer maximumAge,
            @Param("price") BigDecimal price,
            @Param("teacherId") UUID teacherId,
            @Param("category") Category category,
            @Param("name") String name,
            @Param("location") Integer location
    );
}

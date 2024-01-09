package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 1. sposób: SELECT * FROM student WHERE name = ?
    // 2. sposób: @Query("SELECT c FROM Course c WHERE c.name = ?1")
    // it uses Java Persistence Query Language - JPQL
    Optional<Course> findCourseByName(String name);
}

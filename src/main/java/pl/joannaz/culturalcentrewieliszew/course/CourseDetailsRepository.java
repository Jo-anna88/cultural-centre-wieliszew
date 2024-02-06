package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseDetailsRepository extends JpaRepository<CourseDetails, Long> {
}

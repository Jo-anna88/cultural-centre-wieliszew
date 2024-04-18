package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.address.Address;
import pl.joannaz.culturalcentrewieliszew.address.AddressRepository;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailsRepository detailsRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    public CourseService (CourseRepository courseRepository,
                          CourseDetailsRepository detailsRepository,
                          UserRepository userRepository,
                          AddressRepository addressRepository) {
        this.courseRepository = courseRepository;
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public List<CourseDTO> getAllCourses() {
        logger.info("Fetching all courses.");
        return courseRepository.findAll().stream().map(CourseDTO::new).collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        logger.info("Fetching course with id {}.", id);
        return new CourseDTO(courseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Error during fetching course with id {}", id);
                    return new EntityNotFoundException(String.format("Course with id %s not found.", id));
                }));
    }

    public CourseDetailsDTO getDetailsById (Long id) {
        logger.info("Fetching course details with id {}.", id);
        return detailsRepository.findById(id)
                .map(CourseDetailsDTO::new)
                .orElseThrow(() -> {
                    logger.error("Error during fetching course details with id {}", id);
                    return new EntityNotFoundException(String.format("Course Details with id %s not found.", id));
                });
    }

    public CourseDTO addCourse(CourseDTO courseDTO) {
        logger.info("Adding {} course to the database.", courseDTO.getName());
        Course newCourse = new Course(courseDTO);
        if(courseDTO.getTeacher().getId() != null) {
            logger.info("Setting {} {} as a teacher of the {} course.",
                    courseDTO.getTeacher().getFirstName(),
                    courseDTO.getTeacher().getLastName(),
                    courseDTO.getName());
            newCourse.setTeacher(getTeacherById(courseDTO.getTeacher().getId()));
        }
        return new CourseDTO(courseRepository.save(newCourse));
    }

    public User getTeacherById(UUID teacherId) {
        logger.info("Fetching teacher with id {}.", teacherId);
        return userRepository.findById(teacherId)
                .orElseThrow(() -> {
                    logger.error("Error during fetching course details with id {}", teacherId);
                    return new EntityNotFoundException(String.format("Teacher with id %s not found.", teacherId));
                });
    }

    public CourseDTO updateCourse(CourseDTO updatedCourseDTO) {
        logger.info("Fetching original course data.");
        Course originalCourse = courseRepository.findById(updatedCourseDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Error during fetching {} course.", updatedCourseDTO.getName());
                    return new EntityNotFoundException(String.format(
                        "Course with id %s not found.", updatedCourseDTO.getId()));
                }
        );

        logger.info("Validation of the course name '{}' for uniqueness constraint.", updatedCourseDTO.getName());
        if(!Objects.equals(originalCourse.getName(), updatedCourseDTO.getName())) {
            Optional<Course> courseOptional = courseRepository.findCourseByName(updatedCourseDTO.getName());
            if (courseOptional.isPresent()) {
                logger.error("Course with name {} already exists.", updatedCourseDTO.getName());
                throw new DataIntegrityViolationException("Such a name already exists.");
            }
            originalCourse.setName(updatedCourseDTO.getName());
        }

        originalCourse.setDescription(updatedCourseDTO.getDescription());
        originalCourse.setImgSource(updatedCourseDTO.getImgSource());
        originalCourse.setTeacher(getTeacherById(updatedCourseDTO.getTeacher().getId()));
        originalCourse.setCategory(updatedCourseDTO.getCategory());
        originalCourse.setMaxParticipantsNumber(updatedCourseDTO.getMaxParticipantsNumber());

        logger.info("Saving updated {} course.", originalCourse.getName());
        return new CourseDTO(courseRepository.save(originalCourse));
    }


    @Transactional
    public Long deleteCourse(Long id) {
        logger.info("Checking if course with id: {} exists.", id);
        boolean courseExists = courseRepository.existsById(id);
        if (!courseExists) {
            logger.error("Course with id: {} does not exist.", id);
            throw new EntityNotFoundException(String.format("Course with id %s does not exist.", id));
        }

        logger.info("Checking if course details with id: {} exists.", id);
        boolean detailsExists = detailsRepository.existsById(id);
        if (detailsExists) {
            logger.info("Deleting course details with id: {}", id);
            detailsRepository.deleteById(id);
        }

        logger.info("Deleting course with id: {}", id);
        courseRepository.deleteById(id);
        return id;
    }

    public CourseDetailsDTO addCourseDetails(CourseDetailsDTO courseDetailsDTO) {
        logger.info("Adding course details for course with id: {}.", courseDetailsDTO.getId());
        logger.info("Fetching course with id: {}", courseDetailsDTO.getId());
        Course course = courseRepository.findById(courseDetailsDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Error during fetching course with id: {}", courseDetailsDTO.getId());
                    return new EntityNotFoundException(
                            String.format("Course with id %s not found.", courseDetailsDTO.getId())
                    );
                });
        CourseDetails courseDetails = new CourseDetails(courseDetailsDTO);
        courseDetails.setAddress(getAddressById(courseDetailsDTO.getLocation().getId()));
        courseDetails.setCourse(course);

        logger.info("Saving course details");
        return new CourseDetailsDTO(detailsRepository.save(courseDetails));
    }

    public Address getAddressById(Integer addressId) {
        logger.info("Fetching address with id: {}", addressId);
        return addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Error during fetching address with id: {}", addressId);
                    return new EntityNotFoundException(String.format("Location with id %s not found.", addressId));
                });
    }

    public CourseDetailsDTO updateCourseDetails(CourseDetailsDTO updatedCourseDetailsDTO) {
        logger.info("Fetching course details with id: {}.", updatedCourseDetailsDTO.getId());
        CourseDetails originalCourseDetails = detailsRepository.findById(updatedCourseDetailsDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Error during fetching course with id: {}.", updatedCourseDetailsDTO.getId());
                    return new EntityNotFoundException(
                            String.format("Course Details with id %s not found.", updatedCourseDetailsDTO.getId()));
                });

        logger.info("Updating course details for course with id: {}.", updatedCourseDetailsDTO.getId());
        originalCourseDetails.update(updatedCourseDetailsDTO);
        if (!Objects.equals(originalCourseDetails.getAddress().getId(), updatedCourseDetailsDTO.getLocation().getId())) {
            logger.info("Updating course details address.");
            originalCourseDetails.setAddress(getAddressById(updatedCourseDetailsDTO.getLocation().getId()));
        }

        logger.info("Saving updated course details.");
        return new CourseDetailsDTO(detailsRepository.save(originalCourseDetails));
    }

    public Long deleteCourseDetails(Long id) {
        logger.info("Checking if course details with id: {} exists.", id);
        boolean detailsExists = detailsRepository.existsById(id);
        if (!detailsExists) {
            logger.error("Course details with id: {} not found.", id);
            throw new EntityNotFoundException(String.format("Course details with id %s does not exist.", id));
        }

        logger.info("Deleting course details with id: {}", id);
        detailsRepository.deleteById(id);
        return id;
    }

    public List<String> getParticipantsByCourseId(Long id) {
        logger.info("Fetching course with id: {}", id);
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            logger.info("Fetching participants of {} course.", course.getName());
            List<UserCourse> userCourses = course.getParticipants();
            return userCourses.stream()
                    .map(UserCourse::getParticipant)
                    .map(user -> user.getFirstName() + " " + user.getLastName()) // Map User to UserDTO using constructor reference
                    .collect(Collectors.toList());
        } else {
            logger.error("Error during fetching course with id: {}", id);
            throw new EntityNotFoundException(String.format("Course with id %s not found.", id));
        }
    }

    public List<CourseDTO> findCoursesByCriteria(
            Integer minAge, Integer maxAge, BigDecimal price, UUID teacher, Category category, String name, Integer location) {
        logger.info("Searching courses by given criteria");
        return courseRepository.findCoursesByCriteria(minAge, maxAge, price, teacher, category, name, location).stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<CourseBasicInfo> getCoursesLedByTeacher(UUID teacherId) {
        logger.info("Fetching courses led by teacher with id: {}", teacherId);
        return courseRepository.findCourseNamesByTeacherId(teacherId);
    }
}
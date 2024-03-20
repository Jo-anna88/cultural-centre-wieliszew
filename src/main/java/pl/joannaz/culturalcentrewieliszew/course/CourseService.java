package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailsRepository detailsRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    //private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    //@Autowired
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
        //log.info("Fetching all courses.");
        return courseRepository.findAll().stream().map(CourseDTO::new).collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        //log.info("Fetching course with id {}.", id);
        return new CourseDTO(courseRepository.findById(id)
                .orElseThrow(() -> {
                    //log.error("Error during fetching course with id {}", id);
                    return new NoSuchElementException(String.format("Course with id %s not found.", id));
                }));
    }

    public CourseDetailsDTO getDetailsById (Long id) {
        return detailsRepository.findById(id)
                .map(CourseDetailsDTO::new)
                .orElse(new CourseDetailsDTO());
                //.orElseThrow(() -> new NoSuchElementException(String.format("Course Details with id %s not found.", id))));
    }

    public CourseDTO addCourse(CourseDTO courseDTO) {
        //log.info("Saving new course: {} to the database", courseDTO.getName());
        Course newCourse = new Course(courseDTO);
        newCourse.setTeacher(getTeacherById(courseDTO.getTeacher().getId()));
        return new CourseDTO(courseRepository.save(newCourse));
    }

    public User getTeacherById(UUID teacherId) {
        return userRepository.findById(teacherId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Teacher with id %s not found.", teacherId)));
    }

    //because we use @Transactional we can use setters against methods from repository
    public CourseDTO updateCourse(CourseDTO updatedCourseDTO) {
        Course originalCourse = courseRepository.findById(updatedCourseDTO.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format(
                        "Course with id %s not found.", updatedCourseDTO.getId()))
        );

        if(!Objects.equals(originalCourse.getDescription(), updatedCourseDTO.getDescription())) {
            originalCourse.setDescription(updatedCourseDTO.getDescription());
        }
        if(!Objects.equals(originalCourse.getName(), updatedCourseDTO.getName())) {
            Optional<Course> courseOptional = courseRepository.findCourseByName(updatedCourseDTO.getName());
            if (courseOptional.isPresent()) {
                throw new IllegalStateException("Such a name already exists.");
            }
            originalCourse.setName(updatedCourseDTO.getName());
        }
        // if...
        originalCourse.setImgSource(updatedCourseDTO.getImgSource());
        // if...
        originalCourse.setTeacher(getTeacherById(updatedCourseDTO.getTeacher().getId()));
        // if...
        originalCourse.setCategory(updatedCourseDTO.getCategory());

        originalCourse.setMaxParticipantsNumber(updatedCourseDTO.getMaxParticipantsNumber());

        return new CourseDTO(courseRepository.save(originalCourse));
        //return originalCourse;
    }

    public Long deleteCourse(Long id) { // UUID
        boolean exists = courseRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException(String.format("Course with id %s not exist.", id));
        }

        boolean detailsExist = detailsRepository.existsById(id);
        if (detailsExist) {
            detailsRepository.deleteById(id);
        }

        courseRepository.deleteById(id);
        return id;
    }

    public CourseDetailsDTO addCourseDetails(CourseDetailsDTO courseDetailsDTO) {
        Course course = courseRepository.findById(courseDetailsDTO.getId())
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Course with id %s not found.", courseDetailsDTO.getId())
                ));
        CourseDetails courseDetails = new CourseDetails(courseDetailsDTO);
        courseDetails.setAddress(getAddressById(courseDetailsDTO.getLocation().getId()));
        courseDetails.setCourse(course);

        return new CourseDetailsDTO(detailsRepository.save(courseDetails));
    }

    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Location with id %s not found.", addressId)));
    }

    public CourseDetailsDTO updateCourseDetails(CourseDetailsDTO updatedCourseDetailsDTO) {
        CourseDetails originalCourseDetails = detailsRepository.findById(updatedCourseDetailsDTO.getId())
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Course Details with id %s not found.", updatedCourseDetailsDTO.getId())
                ));
//        originalCourseDetails.setMinAge(updatedCourseDetails.getMinAge());
//        originalCourseDetails.setMaxAge(updatedCourseDetails.getMaxAge());
//        originalCourseDetails.setPrice(updatedCourseDetails.getPrice());
//        originalCourseDetails.setMaxParticipantsNumber(updatedCourseDetails.getMaxParticipantsNumber());
//        originalCourseDetails.setLessonDurationMinutes(updatedCourseDetails.getLessonDurationMinutes());
//        originalCourseDetails.setDate(updatedCourseDetails.getDate());
//        originalCourseDetails.setRoomId(updatedCourseDetails.getRoomId());
        originalCourseDetails.update(updatedCourseDetailsDTO);
        if (!Objects.equals(originalCourseDetails.getAddress().getId(), updatedCourseDetailsDTO.getLocation().getId())) {
            originalCourseDetails.setAddress(getAddressById(updatedCourseDetailsDTO.getLocation().getId()));
        }
        return new CourseDetailsDTO(detailsRepository.save(originalCourseDetails));
    }

    public Long deleteCourseDetails(Long id) {
        boolean detailsExist = detailsRepository.existsById(id);
        if (!detailsExist) {
            throw new IllegalStateException(String.format("Course details with id %s not exist.", id));
        }
        detailsRepository.deleteById(id);
        return id;
    }

    public List<String> getParticipantsByCourseId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<UserCourse> userCourses = course.getParticipants();
            return userCourses.stream()
                    .map(UserCourse::getParticipant)
                    .map(user -> user.getFirstName() + " " + user.getLastName()) // Map User to UserDTO using constructor reference
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format("Course with id %s not found.", id));
        }
    }

    public List<CourseDTO> findCoursesByCriteria(
            Integer minAge, Integer maxAge, BigDecimal price, String teacher, Category category, String name, String location) {

        return courseRepository.findCoursesByCriteria(minAge, maxAge, price, teacher, category, name, location).stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<String> getCoursesLedByTeacher(UUID teacherId) {
        return courseRepository.findCourseNamesByTeacherId(teacherId);
    }

}

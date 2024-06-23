package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername (String username);
    boolean existsByUsername (String username);
    List<User> findByParentId(UUID parentId);

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.user.UserBasicInfo(user.id, user.firstName, user.lastName) " +
            "FROM User user WHERE user.position = 'Teacher'")
    List<UserBasicInfo> findTeachers();

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.user.EmployeeProfile(" +
            "user.id, user.firstName, user.lastName, user.headshot, user.position, user.description) " +
            "FROM User user WHERE user.role = 'ADMIN' OR user.role = 'EMPLOYEE'")
    List<EmployeeProfile> findEmployees();

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.user.UserBasicInfo(user.id, user.firstName, user.lastName) " +
            "FROM User user WHERE (user.id = :uuid) ")
    UserBasicInfo findUserBasicInfo(@Param("uuid") UUID uuid);

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.user.UserBasicInfo(child.id, child.firstName, child.lastName) " +
            "FROM User child WHERE (child.parent.id = :parentId)")
    List<UserBasicInfo> findChildrenBasicInfo(@Param("parentId") UUID parentId);
}

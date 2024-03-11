package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername (String username);
    boolean existsByUsername (String username);
    List<User> findByParentId(UUID parentId);

    @Query("SELECT new pl.joannaz.culturalcentrewieliszew.user.UserDetailsDTO(user.id, user.firstName, user.lastName) " +
            "FROM User user WHERE user.role = pl.joannaz.culturalcentrewieliszew.user.Role.EMPLOYEE")
    List<UserDetailsDTO> findTeachers();
}

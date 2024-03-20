package pl.joannaz.culturalcentrewieliszew.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT a.id, a.location FROM address a")
    List<Object[]> findAllLocations();
}

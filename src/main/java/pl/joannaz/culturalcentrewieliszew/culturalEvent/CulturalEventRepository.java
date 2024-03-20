package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long> {
}

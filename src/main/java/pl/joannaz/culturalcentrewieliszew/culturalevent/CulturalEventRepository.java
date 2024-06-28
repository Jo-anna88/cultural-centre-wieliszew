package pl.joannaz.culturalcentrewieliszew.culturalevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long> {
    @Query("SELECT ce.maxParticipantsNumber FROM CulturalEvent ce WHERE ce.id = :culturalEventId")
    int findMaxParticipantsNumberById(@Param("culturalEventId") Long culturalEventId);
}

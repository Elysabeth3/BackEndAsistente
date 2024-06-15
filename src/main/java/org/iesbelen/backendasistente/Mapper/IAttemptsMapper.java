package org.iesbelen.backendasistente.Mapper;

import org.iesbelen.backendasistente.Model.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface IAttemptsMapper extends JpaRepository<Attempts, Long> {
    @Query("SELECT COUNT(a) FROM Attempts a WHERE a.userId = :userId AND a.attemptTime >= :timeThreshold")
    long countAttemptsForUserInLast24Hours(long userId, Timestamp timeThreshold);
}

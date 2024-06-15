package org.iesbelen.backendasistente.Service;

import org.iesbelen.backendasistente.Model.Attempts;
import org.iesbelen.backendasistente.Model.Users;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface IAttemptsService {
    long countFailedAttemptsPerUserInTimePeriod(Users usuario, Timestamp tiempo);

    Attempts saveAttempt(Attempts attempt);
}

package org.iesbelen.backendasistente.Service.imp;

import org.iesbelen.backendasistente.Model.Attempts;
import org.iesbelen.backendasistente.Model.Users;
import org.iesbelen.backendasistente.Mapper.IAttemptsMapper;
import org.iesbelen.backendasistente.Service.IAttemptsService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AttemptsService implements IAttemptsService {
    private final IAttemptsMapper attemptsRepository;

    public AttemptsService(IAttemptsMapper attemptsRepository) {
        this.attemptsRepository = attemptsRepository;
    }

    @Override
    public long countFailedAttemptsPerUserInTimePeriod(Users usuario, Timestamp tiempo) {
        return attemptsRepository.countAttemptsForUserInLast24Hours(usuario.getId(), tiempo);
    }

    @Override
    public Attempts saveAttempt(Attempts attempt) {
        return attemptsRepository.save(attempt);
    }
}

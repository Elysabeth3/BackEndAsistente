package org.iesbelen.backendasistente.Mapper;

import org.iesbelen.backendasistente.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserMapper extends JpaRepository<Users, String> {
    Optional<Users> findUserByUsername(String username);
    Optional<Users> findByEmail(String email);
}

package com.mastera.atelier.Repositories;

import com.mastera.atelier.Models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    Optional<User> findById(Long aLong);

    User findUserByUsername(String username);

    List<User> findUsersByRole(String role);

    List<User> findAll();
}

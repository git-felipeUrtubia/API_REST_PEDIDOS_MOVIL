package com.empresa.api_level_up_movil.repository;

import com.empresa.api_level_up_movil.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

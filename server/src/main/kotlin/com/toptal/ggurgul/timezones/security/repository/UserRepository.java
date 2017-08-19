package com.toptal.ggurgul.timezones.security.repository;

import com.toptal.ggurgul.timezones.domain.models.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}

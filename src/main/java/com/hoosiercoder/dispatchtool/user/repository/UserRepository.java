package com.hoosiercoder.dispatchtool.user.repository;

import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: HoosierCoder
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastName(String lastName);

    List<User> findByUserRole(UserRole userRole);

    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();
}

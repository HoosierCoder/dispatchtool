package com.hoosiercoder.dispatchtool.customer.repository;

import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: HoosierCoder
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Spring Data magic: It generates the SQL for you based on the method name!
    Optional<Customer> findByEmail(String email);

    // Handy for your dispatch search: Find by last name or phone
    Optional<Customer> findByLastName(String lastName);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}

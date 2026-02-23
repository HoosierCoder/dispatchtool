package com.hoosiercoder.dispatchtool.location.repository;

import com.hoosiercoder.dispatchtool.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByZipCode(String zipCode);

    List<Location> findByCityIgnoreCase(String city);
}

package org.hibernateSource.repository;

import java.util.Optional;

import org.hibernateSource.models.ERole;
import org.hibernateSource.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
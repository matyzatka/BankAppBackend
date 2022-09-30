package mzatka.bankappbackend.repositories;

import mzatka.bankappbackend.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findRoleByName(String name);
}

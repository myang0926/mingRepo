package repository;

import model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface  RoleRepository  extends JpaRepository<Role,UUID> {
}

package itm.codingmaxima.contact.application.repository;

import itm.codingmaxima.contact.application.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {
}

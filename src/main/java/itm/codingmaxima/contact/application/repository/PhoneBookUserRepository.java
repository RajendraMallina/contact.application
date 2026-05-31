package itm.codingmaxima.contact.application.repository;

import itm.codingmaxima.contact.application.model.PhoneBookUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneBookUserRepository extends JpaRepository<PhoneBookUser, Integer> {

    PhoneBookUser findByName(String name);

    boolean existsByEmail(String email);
}

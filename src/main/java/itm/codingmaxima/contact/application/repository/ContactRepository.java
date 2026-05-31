package itm.codingmaxima.contact.application.repository;

import itm.codingmaxima.contact.application.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByName(String name);

    List<Contact> findByNameContainingIgnoreCase(String key);
}

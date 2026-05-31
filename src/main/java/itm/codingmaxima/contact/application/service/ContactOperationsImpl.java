package itm.codingmaxima.contact.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import itm.codingmaxima.contact.application.model.Contact;
import itm.codingmaxima.contact.application.repository.ContactRepository;

@Service
public class ContactOperationsImpl implements ContactOperations {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public boolean addContact(Contact contact) {

		if (contactRepository.existsById(contact.getMobileNumber())) {
			return false;
		}

		contactRepository.save(contact);
		return true;
	}

	@Override
	public Contact getContact(long mobileNumber) {
		return contactRepository.findById(mobileNumber).orElse(null);
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	@Override
	public List<Contact> getContactByName(String name) {
		return contactRepository.findByName(name);
	}

	@Override
	public boolean deleteContact(long mobileNumber) {

		if (!contactRepository.existsById(mobileNumber)) {
			return false;
		}

		contactRepository.deleteById(mobileNumber);
		return true;
	}

	@Override
	public Contact updateContactName(long mobileNumber, String name) {

		Contact contact = contactRepository.findById(mobileNumber)
				.orElse(null);

		if (contact == null) {
			return null;
		}

		contact.setName(name);

		return contactRepository.save(contact);
	}

	@Override
	public Contact updateContact(long mobileNumber, Contact contact) {

		Contact existingContact = contactRepository.findById(mobileNumber)
				.orElse(null);

		if (existingContact == null) {
			return null;
		}

		long updatedMobileNumber = contact.getMobileNumber();

		if (updatedMobileNumber != mobileNumber
				&& contactRepository.existsById(updatedMobileNumber)) {
			return null;
		}

		existingContact.setMobileNumber(updatedMobileNumber);
		existingContact.setName(contact.getName());
		existingContact.setGender(contact.getGender());
		existingContact.setType(contact.getType());

		return contactRepository.save(existingContact);
	}

	@Override
	public List<Contact> searchContact(String key) {
		return contactRepository.findByNameContainingIgnoreCase(key);
	}

	@Override
	public List<Contact> sortContacts(String property) {

		if (property == null || property.isBlank()) {
			return contactRepository.findAll(
					Sort.by("mobileNumber"));
		}

		return contactRepository.findAll(
				Sort.by(property));
	}
}
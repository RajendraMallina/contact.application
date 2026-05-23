package itm.codingmaxima.contact.application.service;

import java.util.List;

import itm.codingmaxima.contact.application.model.Contact;

public interface ContactOperations {

	public boolean addContact(Contact c);
	public Contact getContact(long mobileNumber);
	public List<Contact> getAllContacts();
	public List<Contact> getContactByName(String name);
	public boolean deleteContact(long mobileNumber);
	public Contact updateContactName(long mobileNumber, String name);
	public Contact updateContact(long mobileNumber, Contact contact);
	public List<Contact> searchContact(String key);
	public List<Contact> sortContacts(String property);
}

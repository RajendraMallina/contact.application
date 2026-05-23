package itm.codingmaxima.contact.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import itm.codingmaxima.contact.application.model.Contact;

public class ContactOperationsImpl implements ContactOperations {

	List<Contact> contacts = new ArrayList();
	@Override
	public boolean addContact(Contact c) {
		
		for(Contact cc : contacts) {
			if(cc.getMobileNumber() == c.getMobileNumber()) {
				return false;
			}
		}	
		contacts.add(c);
		return true;	
	}

	@Override
	public Contact getContact(long mobileNumber) {
		
		for(Contact c : contacts) {
			if(c.getMobileNumber() == mobileNumber) {
				return c;
			}
		}
		return null;
	}

	@Override
	public List<Contact> getAllContacts() {
		return contacts;
	}

	@Override
	public List<Contact> getContactByName(String name) {
		List<Contact> list = new ArrayList();
		
		for(Contact c : contacts) {
			if(c.getName().equals(name)) {
				list.add(c);
			}
		}
		return list;
	}

	@Override
	public boolean deleteContact(long mobileNumber) {
		
		Contact c = getContact(mobileNumber);
		
		if(c instanceof Contact) {
			contacts.remove(c);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Contact updateContactName(long mobileNumber, String name) {
		for(Contact c : contacts) {
			if(c.getMobileNumber() == mobileNumber) {
				c.setName(name);
				return c;
			}
		}
		return null;
	}

	@Override
	public Contact updateContact(long mobileNumber, Contact contact) {
		Contact existingContact = getContact(mobileNumber);
		if(existingContact == null) {
			return null;
		}

		long updatedMobileNumber = contact.getMobileNumber();
		if(updatedMobileNumber != mobileNumber && getContact(updatedMobileNumber) != null) {
			return null;
		}

		existingContact.setMobileNumber(updatedMobileNumber);
		existingContact.setName(contact.getName());
		existingContact.setGender(contact.getGender());
		existingContact.setType(contact.getType());
		return existingContact;
	}

	@Override
	public List<Contact> searchContact(String key) {
		List<Contact> list = new ArrayList();
		for(Contact c: contacts) {
			if(c.getName().contains(key)){
				list.add(c);
			}
		}
		return list;
	}

	@Override
	public List<Contact> sortContacts(String property) {
		
		List<Contact> list = getAllContacts();
		if(property.isEmpty()) {
			Collections.sort(list);
		}else if(property.equals("name")){
			Collections.sort(list, new SortByName());
		}else {
			return null;
		}
		return list;
	}

}

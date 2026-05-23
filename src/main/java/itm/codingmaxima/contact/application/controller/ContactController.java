package itm.codingmaxima.contact.application.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import itm.codingmaxima.contact.application.model.Contact;
import itm.codingmaxima.contact.application.service.ContactOperations;
import itm.codingmaxima.contact.application.service.ContactOperationsImpl;

@RestController
public class ContactController {

	ContactOperations op = new ContactOperationsImpl();
	
	@RequestMapping(path="/contact/add", method=RequestMethod.POST)
	public boolean addContact(@RequestBody Contact c) {
		return op.addContact(c);
	}
	
	@RequestMapping(path="/contact/getall", method=RequestMethod.GET)
	public List<Contact> getAllContacts(){
		return op.getAllContacts();
	}
	
	@RequestMapping(path="/contact/get/{mobileNumber}", method=RequestMethod.GET)
	public Contact getContact(@PathVariable long mobileNumber) {
		return op.getContact(mobileNumber);
	}
	
	@RequestMapping(path="/contact/sort", method=RequestMethod.GET)
	public List<Contact> sort(@RequestParam(defaultValue = "") String property){
		return op.sortContacts(property);
	}

	@RequestMapping(path="/contact/user", method=RequestMethod.GET)
	public Map<String, Object> getCurrentUser(Authentication authentication) {
		boolean admin = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		return Map.of(
				"username", authentication.getName(),
				"admin", admin
		);
	}
	
	@RequestMapping(path="/contact/updatename/{mobileNumber}", method = RequestMethod.PUT)
	public Contact updateContactName(@PathVariable long mobileNumber, @RequestBody String name) {
		return op.updateContactName(mobileNumber, name);
	}

	@RequestMapping(path="/contact/update/{mobileNumber}", method = RequestMethod.PUT)
	public Contact updateContact(@PathVariable long mobileNumber, @RequestBody Contact contact) {
		return op.updateContact(mobileNumber, contact);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(path="/contact/delete/{mobileNumber}", method = RequestMethod.DELETE)
	public boolean deleteContact(@PathVariable long mobileNumber) {
		return op.deleteContact(mobileNumber);
	}
}

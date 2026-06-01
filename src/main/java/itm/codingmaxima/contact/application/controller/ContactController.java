package itm.codingmaxima.contact.application.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itm.codingmaxima.contact.application.model.Contact;
import itm.codingmaxima.contact.application.service.ContactOperations;
import itm.codingmaxima.contact.application.service.ContactOperationsImpl;

@RestController
@Tag(name = "Contact Controller", description = "Contact Management APIs")
public class ContactController {

	@Autowired
	ContactOperations op;

	@Operation(summary = "Add Contact")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/contact/add")
	public boolean addContact(@RequestBody Contact c) {
		return op.addContact(c);
	}

	@Operation(summary = "Get All Contacts")
	@GetMapping("/contact/getall")
	public List<Contact> getAllContacts() {
		return op.getAllContacts();
	}

	@Operation(summary = "Get Contact By Mobile Number")
	@GetMapping("/contact/get/{mobileNumber}")
	public Contact getContact(@PathVariable long mobileNumber) {
		return op.getContact(mobileNumber);
	}

	@Operation(summary = "Sort Contacts")
	@GetMapping("/contact/sort")
	public List<Contact> sort(
			@RequestParam(defaultValue = "") String property) {
		return op.sortContacts(property);
	}

	@Operation(summary = "Get Current Logged-In User")
	@GetMapping("/contact/user")
	public Map<String, Object> getCurrentUser(Authentication authentication) {

		String role = authentication.getAuthorities()
				.stream()
				.map(authority -> authority.getAuthority())
				.findFirst()
				.orElse("NO_ROLE");

		return Map.of(
				"username", authentication.getName(),
				"role", role
		);
	}

	@Operation(summary = "Update Contact Name")
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/contact/updatename/{mobileNumber}")
	public Contact updateContactName(
			@PathVariable long mobileNumber,
			@RequestBody String name) {

		return op.updateContactName(mobileNumber, name);
	}

	@Operation(summary = "Update Complete Contact")
	@PutMapping("/contact/update/{mobileNumber}")
	public Contact updateContact(
			@PathVariable long mobileNumber,
			@RequestBody Contact contact) {

		return op.updateContact(mobileNumber, contact);
	}

	@Operation(summary = "Delete Contact (Admin Only)")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@DeleteMapping("/contact/delete/{mobileNumber}")
	public boolean deleteContact(@PathVariable long mobileNumber) {
		return op.deleteContact(mobileNumber);
	}
}
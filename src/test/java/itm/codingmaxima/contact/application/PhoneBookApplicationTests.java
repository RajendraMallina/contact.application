package itm.codingmaxima.contact.application;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import itm.codingmaxima.contact.application.model.Contact;
import itm.codingmaxima.contact.application.model.ContactType;
import itm.codingmaxima.contact.application.service.ContactOperations;
import itm.codingmaxima.contact.application.service.ContactOperationsImpl;

@SpringBootTest
class PhoneBookApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testDefalutContactObject() {
		
		Contact c = new Contact();
		assertTrue(c instanceof Contact);
	}
	
	@Test
	void testParamContactObject() {
		
		Contact c = new Contact(123, "Raj", "Male", ContactType.Business);
		
		assertTrue(c.getMobileNumber() == 123);
		assertTrue(c.getName().equals("Raj"));
		assertTrue(c.getGender().equals("Male"));
		assertTrue(c.getType().equals(ContactType.Business));
	}
	
	@Test
	void testUpdateContactObject() {
		Contact c = new Contact();
		c.setMobileNumber(123);
		c.setName("Raj");
		c.setGender("Male");
		c.setType(ContactType.Family);
		assertTrue(c.getMobileNumber() == 123);
		assertTrue(c.getName().equals("Raj"));
		assertTrue(c.getGender().equals("Male"));
		assertTrue(c.getType().equals(ContactType.Family));
	}
	
	@Test
	void testToStringOfContactObject() {
		
		Contact c = new Contact(123, "Raj", "Male", ContactType.Business);
		System.out.println(c);
		String s = "Contact [mobileNumber=123, name=Raj, gender=Male, type=Business]";
		assertTrue(s.equals(c.toString()));
	}
	
	@Test
	void testServiceObject() {
		
		ContactOperations c = new ContactOperationsImpl();
		
		assertTrue(c instanceof ContactOperationsImpl);
	}
	
	@Test
	void testAddContact() {
		ContactOperations op = new ContactOperationsImpl();
		Contact c = new Contact(123, "Raj", "Male", ContactType.Business);
		assertTrue(op.getAllContacts().size() == 0);
		op.addContact(c);
		op.addContact(c);
		Contact c2 = new Contact(234, "Raj", "Male", ContactType.Business);
		op.addContact(c2);
		assertTrue(op.getAllContacts().size() == 2);
		List<Contact> list = new ArrayList<>();
		list.add(c);
		list.add(c2);
		for(int i = 0; i < op.getAllContacts().size(); i++) {
			assertTrue(list.get(i).equals(op.getAllContacts().get(i)));
		}
	}

}

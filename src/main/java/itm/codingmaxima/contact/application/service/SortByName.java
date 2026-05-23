package itm.codingmaxima.contact.application.service;

import java.util.Comparator;

import itm.codingmaxima.contact.application.model.Contact;

public class SortByName implements Comparator<Contact> {

	@Override
	public int compare(Contact o1, Contact o2) {
		return o1.getName().compareTo(o2.getName());
	}

}

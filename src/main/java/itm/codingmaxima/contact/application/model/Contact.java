package itm.codingmaxima.contact.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Contact implements Comparable<Contact>{

	@Id
	private long mobileNumber;
	private String name;
	private String gender;
	@Enumerated(EnumType.STRING)
	private ContactType type;
	
	public Contact() {}

	public Contact(long mobileNumber, String name, String gender, ContactType type) {
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.gender = gender;
		this.type = type;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Contact [mobileNumber=" + mobileNumber + ", name=" + name + ", gender=" + gender + ", type=" + type
				+ "]";
	}

	@Override
	public int compareTo(Contact o) {
		return (int) (this.mobileNumber - o.mobileNumber);
	}
}

package org.mateuszsikorski.masscorrespondencebuilder.correspondence;

import java.io.Serializable;

public class Recipient implements Serializable{
	
	private final String firstName;
	private final String lastName;
	private final String street;
	private final String postalCode;
	private final String city;
	
	private static final long serialVersionUID = 8820368896918241168L;
	
	public Recipient(String firstName, String lastName, String street, String postalCode, String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.postalCode = postalCode;
		this.city = city;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreet() {
		return street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "Recipient [firstName=" + firstName + ", lastName=" + lastName + ", street=" + street + ", postalCode="
				+ postalCode + ", city=" + city + "]";
	}

}

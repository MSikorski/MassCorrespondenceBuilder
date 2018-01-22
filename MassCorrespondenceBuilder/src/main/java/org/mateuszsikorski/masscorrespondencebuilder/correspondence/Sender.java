package org.mateuszsikorski.masscorrespondencebuilder.correspondence;

import java.io.Serializable;

public class Sender implements Serializable{

	private final String sendingDate;
	private final String companyName;
	private final String companyStreet;
	private final String companyPostalCode;
	private final String companyCity;
	private final String companyDetail;
	
	private static final long serialVersionUID = -8458680387068344399L;
	
	public Sender(String sendingDate,String companyName, String companyStreet, 
			String companyPostalCode, String companyCity,String companyDetail) {
		this.sendingDate = sendingDate;
		this.companyName = companyName;
		this.companyStreet = companyStreet;
		this.companyPostalCode = companyPostalCode;
		this.companyCity = companyCity;
		this.companyDetail = companyDetail;
	}

	public String getSendingDate() {
		return sendingDate;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyStreet() {
		return companyStreet;
	}

	public String getCompanyPostalCode() {
		return companyPostalCode;
	}

	public String getCompanyCity() {
		return companyCity;
	}

	public String getCompanyDetail() {
		return companyDetail;
	}

	@Override
	public String toString() {
		return "Sender [sendingDate=" + sendingDate + ", companyName=" + companyName + ", companyStreet="
				+ companyStreet + ", companyPostalCode=" + companyPostalCode + ", companyCity=" + companyCity
				+ ", companyDetail=" + companyDetail + "]";
	}
	
}

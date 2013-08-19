package com.futurePayment.model;

import java.sql.Date;
import java.util.ArrayList;

public class RegistInformation {
	private String name;
	private String loginPassword;
	private String payPassword;
	private String realName;
	private Date birthday;
	private String sex;
	private ArrayList<Contact> contact = new ArrayList<Contact>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void appendContact(String type, String number) {
		contact.add(new Contact(type, number));
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	private class Contact {
		private String type;
		private String number;

		public Contact(String t, String n) {
			type = t;
			number = n;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}
	}
}

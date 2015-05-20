package com.limxing.safe.domain;

public class ContactInfo {
	private String name;
	private String qq;
	private String email;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", qq=" + qq + ", email=" + email
				+ ", phone=" + phone + "]";
	}

}

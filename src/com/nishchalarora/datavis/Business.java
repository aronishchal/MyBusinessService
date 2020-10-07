package com.nishchalarora.datavis;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "idea")
public class Business implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;

	@NotNull
	@Size(min = 1, max = 255)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String address;

	public Business() {}

	public Business(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

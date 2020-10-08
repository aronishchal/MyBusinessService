package com.nishchalarora.datavis;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "idea")
public class Business implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private double lat;
	private double lon;

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	@NotNull
	@Size(min = 1, max = 255)
	private String address;

	public Business() {
	}

	public Business(int id, double lat, double lon, @NotNull @Size(min = 1, max = 255) String name,
			@NotNull @Size(min = 1, max = 255) String address) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
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

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
}

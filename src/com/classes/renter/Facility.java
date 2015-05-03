package com.classes.renter;

public class Facility {

	String facilityName;
	int facilityTotal;
	int facilityAvailable;
	
	
	public Facility(String facilityName, int facilityTotal,
			int facilityAvailable) {
		super();
		this.facilityName = facilityName;
		this.facilityTotal = facilityTotal;
		this.facilityAvailable = facilityAvailable;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public int getFacilityTotal() {
		return facilityTotal;
	}
	public void setFacilityTotal(int facilityTotal) {
		this.facilityTotal = facilityTotal;
	}
	public int getFacilityAvailable() {
		return facilityAvailable;
	}
	public void setFacilityAvailable(int facilityAvailable) {
		this.facilityAvailable = facilityAvailable;
	}
	@Override
	public String toString() {
		return "Facility [facilityName=" + facilityName + ", facilityTotal="
				+ facilityTotal + ", facilityAvailable=" + facilityAvailable
				+ "]";
	}
	
	
	
}

package com.classes.renter;

public class Flat {
	String flatNumber = null;
	String tenantName = null;
	String tenantMailId = null;
	String flatOccupiedStatus;
	
	public String getFlatNumber() {
		return flatNumber;
	}
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getTenantMailId() {
		return tenantMailId;
	}
	public void setTenantMailId(String tenantMailId) {
		this.tenantMailId = tenantMailId;
	}
	public String getFlatOccupiedStatus() {
		return flatOccupiedStatus;
	}
	public void setFlatOccupiedStatus(String flatOccupiedStatus) {
		this.flatOccupiedStatus = flatOccupiedStatus;
	}
	public Flat(String flatNumber, String tenantName, String tenantMailId,
			String flatOccupiedStatus) {
		super();
		this.flatNumber = flatNumber;
		this.tenantName = tenantName;
		this.tenantMailId = tenantMailId;
		this.flatOccupiedStatus = flatOccupiedStatus;
	}
	
}

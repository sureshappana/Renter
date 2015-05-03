package com.classes.renter;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable {

	String mId,mTitle, mDescription, mApartmentNo, mPriority, 
					mTenantId, mCommunityId, mStatus;								 
	Date mStartDate,mCloseDate;
	
	
	
	

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}
	
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmDescription() {
		return mDescription;
	}
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public String getmApartmentNo() {
		return mApartmentNo;
	}
	public void setmApartmentNo(String mApartmentNo) {
		this.mApartmentNo = mApartmentNo;
	}
	public String getmPriority() {
		return mPriority;
	}
	public void setmPriority(String mPriority) {
		this.mPriority = mPriority;
	}
	public String getmTenantId() {
		return mTenantId;
	}
	public void setmTenantId(String mTenantId) {
		this.mTenantId = mTenantId;
	}
	public String getmCommunityId() {
		return mCommunityId;
	}
	public void setmCommunityId(String mCommunityId) {
		this.mCommunityId = mCommunityId;
	}
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public Date getmStartDate() {
		return mStartDate;
	}
	public void setmStartDate(Date mStartDate) {
		this.mStartDate = mStartDate;
	}
	public Date getmCloseDate() {
		return mCloseDate;
	}
	public void setmCloseDate(Date mCloseDate) {
		this.mCloseDate = mCloseDate;
	}
	@Override
	public String toString() {
		return "Ticket [id=" + mId + ", mTitle=" + mTitle + ", mDescription="
				+ mDescription + ", mApartmentNo=" + mApartmentNo
				+ ", mPriority=" + mPriority + ", mTenantId=" + mTenantId
				+ ", mCommunityId=" + mCommunityId + ", mStatus=" + mStatus
				+ ", mStartDate=" + mStartDate + ", mCloseDate=" + mCloseDate
				+ "]";
	}    
	

}

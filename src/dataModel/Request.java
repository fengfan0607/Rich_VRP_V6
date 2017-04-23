package dataModel;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Request {
	private int id;
	private int start_Time;
	private int end_Time;
	private int numOfDaysRequest;
	private int requestToolKind;
	private int requestToolNumber;
	private List<Integer> associateRequestForPickUpDelivery;
	private List<Integer> associateRequestForDeliveryPickUp;
	private int customerID;
	private Set<Integer> associateSet;
	private Set<Integer> nearByRequests;
	private List<Integer> mustTogether;
	private Hashtable<Integer, List<Integer>> associationTable;
	private int planedDay;

	public int getPlanedDay() {
		return planedDay;
	}

	public void setPlanedDay(int planedDay) {
		this.planedDay = planedDay;
	}

	public Hashtable<Integer, List<Integer>> getAssociationTable() {
		return associationTable;
	}

	public void setAssociationTable(Hashtable<Integer, List<Integer>> associationTable) {
		this.associationTable = associationTable;
	}

	public List<Integer> getMustTogether() {
		return mustTogether;
	}

	public void setMustTogether(List<Integer> mustTogether) {
		this.mustTogether = mustTogether;
	}

	public Set<Integer> getNearByRequests() {
		return nearByRequests;
	}

	public void setNearByRequests(Set<Integer> nearByRequests) {
		this.nearByRequests = nearByRequests;
	}

	public Set<Integer> getAssociateSet() {
		return associateSet;
	}

	public void setAssociateSet(Set<Integer> associateSet) {
		this.associateSet = associateSet;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int cutomerID) {
		this.customerID = cutomerID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStart_Time() {
		return start_Time;
	}

	public void setStart_Time(int start_Time) {
		this.start_Time = start_Time;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", start_Time=" + start_Time + ", end_Time=" + end_Time + ", numOfDaysRequest="
				+ numOfDaysRequest + ", requestToolKind=" + requestToolKind + ", requestToolNumber=" + requestToolNumber
				+ ", customerID=" + customerID + ", nearByRequests=" + nearByRequests + ", associationTable="
				+ associationTable + ", planedDay=" + planedDay + "]" + "\n";
	}

	public int getEnd_Time() {
		return end_Time;
	}

	public Request() {
		super();
	}

	public void setEnd_Time(int end_Time) {
		this.end_Time = end_Time;
	}

	public int getNumOfDaysRequest() {
		return numOfDaysRequest;
	}

	public void setNumOfDaysRequest(int numOfDaysRequest) {
		this.numOfDaysRequest = numOfDaysRequest;
	}

	public int getRequestToolKind() {
		return requestToolKind;
	}

	public void setRequestToolKind(int requestToolKind) {
		this.requestToolKind = requestToolKind;
	}

	public int getRequestToolNumber() {
		return requestToolNumber;
	}

	public void setRequestToolNumber(int requestToolNumber) {
		this.requestToolNumber = requestToolNumber;
	}

	public List<Integer> getAssociateRequestForPickUpDelivery() {
		return associateRequestForPickUpDelivery;
	}

	public void setAssociateRequestForPickUpDelivery(List<Integer> associateRequestForPickUpDelivery) {
		this.associateRequestForPickUpDelivery = associateRequestForPickUpDelivery;
	}

	public List<Integer> getAssociateRequestForDeliveryPickUp() {
		return associateRequestForDeliveryPickUp;
	}

	public void setAssociateRequestForDeliveryPickUp(List<Integer> associateRequestForDeliveryPickUp) {
		this.associateRequestForDeliveryPickUp = associateRequestForDeliveryPickUp;
	}

}

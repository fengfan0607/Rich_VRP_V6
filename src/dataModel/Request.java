package dataModel;

import java.util.List;

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
				+ ", cutomerID=" + customerID + "]";
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

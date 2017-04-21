package DataIO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.base.CharMatcher;

import dataModel.Distance;
import dataModel.Depot;
import dataModel.BlackBoard;
import dataModel.Customer;
import dataModel.Request;
import dataModel.Tool;

public class DataRead implements DataIO {

	public int[] config;
	public List<Tool> toolList;
	public List<Request> requests;
	public TreeMap<Integer, List<Integer>> requestToolMapping;
	public TreeMap<Integer, List<Integer>> fixedDayDelivery;
	public List<Customer> customers;
	public Depot depot;
	public Distance distance;
	public int[] toolStocks;
	public String fileName;
	public int[][] distanceValue;
	public int circle;
	private long distanceCost;
	private long vehicleCost;
	private long vehcileDayCost;

	public DataRead(String file) {
		// TODO Auto-generated constructor stub
		config = new int[CONFIG_SIZE];
		toolList = new ArrayList<>();
		requests = new ArrayList<>();
		fixedDayDelivery = new TreeMap<>();
		requestToolMapping = new TreeMap<>();
		customers = new ArrayList<>();
		distance = new Distance();
		this.fileName = file;

	}

	public BlackBoard readData(BlackBoard blackBoard) {
		try {
			FileInputStream io = new FileInputStream(fileName);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(io));
			int lineNum = 1;
			String line = null;
			while ((line = bReader.readLine()) != null) {
				if (lineNum < 4) {
					lineNum++;
					continue;
				} else if (lineNum <= 11) {
					readConfig(lineNum, line);

				} else {
					String theDigits = CharMatcher.DIGIT.retainFrom(line);
					if (line.contains("TOOLS")) {
						config[NUM_OF_TOOLS] = Integer.valueOf(theDigits);
						toolStocks = new int[(int) config[NUM_OF_TOOLS]];
					} else if (line.contains("COORDINATES")) {
						config[NUM_OF_CUSTOMER] = Integer.valueOf(theDigits);
						distanceValue = new int[(int) config[NUM_OF_CUSTOMER]][(int) config[NUM_OF_CUSTOMER]];
					} else if (line.contains("REQUESTS")) {
						config[NUM_OF_REQUESTS] = Integer.valueOf(theDigits);
					} else if (config[NUM_OF_TOOLS] != 0 && lineNum > 13 && lineNum <= 13 + config[NUM_OF_TOOLS]) {
						// System.err.println("******************************");
						Tool tool = new Tool();
						String[] splited = line.split("\\s+");
						tool.setID(Integer.valueOf(splited[0]));
						tool.setSize(Integer.valueOf(splited[1]));
						tool.setNumOfTools(Integer.valueOf(splited[2]));
						tool.setCostOfTools(Long.valueOf(splited[3]));
						toolList.add(tool);
						// toolStock.put(tool.getID(), tool.getNumOfTools());
						toolStocks[tool.getID() - 1] = tool.getNumOfTools();
					} else if (config[NUM_OF_CUSTOMER] != 0 && lineNum > 15 + config[NUM_OF_TOOLS]
							&& lineNum <= 15 + config[NUM_OF_TOOLS] + config[NUM_OF_CUSTOMER]) {
						String[] splited = line.split("\\s+");
						if (Integer.valueOf(splited[0]) == 0) {
							depot = new Depot();
							depot.setId(0);
							depot.setxCor(Integer.valueOf(splited[1]));
							depot.setyCor(Integer.valueOf(splited[2]));
						} else {
							Customer customer = new Customer();
							customer.setId(Integer.valueOf(splited[0]));
							customer.setxCor(Integer.valueOf(splited[1]));
							customer.setyCor(Integer.valueOf(splited[2]));
							// customer.setRequests(new Requests());
							customers.add(customer);
						}
					} else if (lineNum == 16 + config[NUM_OF_TOOLS] + config[NUM_OF_CUSTOMER]) {
						distance.setDistance(distanceCal());
					} else if (config[NUM_OF_REQUESTS] != 0
							&& lineNum > 17 + config[NUM_OF_TOOLS] + config[NUM_OF_CUSTOMER] && lineNum <= 17
									+ config[NUM_OF_TOOLS] + config[NUM_OF_CUSTOMER] + config[NUM_OF_REQUESTS]) {
						Request request = new Request();
						String[] splited = line.split("\\s+");
						request.setNearByRequests(new HashSet<>());
						request.setAssociateRequestForPickUpDelivery(new ArrayList<>());
						request.setAssociateRequestForDeliveryPickUp(new ArrayList<>());
						request.setMustTogether(new ArrayList<>());
						request.setAssociateSet(new HashSet<Integer>());
						request.setId(Integer.valueOf(splited[0]));
						request.setCustomerID(Integer.valueOf(splited[1]));
						request.setStart_Time(Integer.valueOf(splited[2]));
						request.setEnd_Time(Integer.valueOf(splited[3]));
						request.setNumOfDaysRequest(Integer.valueOf(splited[4]));
						request.setRequestToolKind(Integer.valueOf(splited[5]));
						request.setRequestToolNumber(Integer.valueOf(splited[6]));
						requests.add(request);
					}
				}
				lineNum++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestHandle();
		findAssociation();
		blackBoard.setConfig(config);
		blackBoard.setDistance(distance.getDistance());
		blackBoard.setToolList(toolList);
		blackBoard.setRequests(requests);
		blackBoard.setToolStock(toolStocks);
		blackBoard.setRequestToolMapping(requestToolMapping);
		blackBoard.setFixedDayDelivery(fixedDayDelivery);
		blackBoard.setDistanceCost(distanceCost);
		blackBoard.setVehcileDayCost(vehcileDayCost);
		blackBoard.setVehicleCost(vehicleCost);
		// System.err.println(blackBoard);
		return blackBoard;
	}

	public void findNeighbour() {

	}

	public int[][] distanceCal() {
		int maxDistance = Integer.MIN_VALUE;
		int minDistance = Integer.MAX_VALUE;

		for (int i = 0; i < config[NUM_OF_CUSTOMER]; i++) {
			int id1;
			int x1;
			int y1;
			if (i == 0) {
				id1 = 0;
				x1 = depot.getxCor();
				y1 = depot.getyCor();
			} else {
				Customer c1 = customers.get(i - 1);
				id1 = c1.getId();
				x1 = c1.getxCor();
				y1 = c1.getyCor();
			}

			for (int j = 0; j < config[NUM_OF_CUSTOMER]; j++) {
				int id2;
				int x2;
				int y2;
				if (j == 0) {
					id2 = 0;
					x2 = depot.getxCor();
					y2 = depot.getyCor();
				} else {
					Customer c2 = customers.get(j - 1);
					id2 = c2.getId();
					x2 = c2.getxCor();
					y2 = c2.getyCor();
				}
				double a = Math.pow(x1 - x2, 2);
				double b = Math.pow(y1 - y2, 2);
				// System.err.println(id1 + "," + id2);
				distanceValue[id1][id2] = (int) Math.sqrt(a + b);
				maxDistance = Math.max(distanceValue[id1][id2], maxDistance);
				if (distanceValue[id1][id2] != 0) {
					minDistance = Math.min(distanceValue[id1][id2], minDistance);
				}

			}
		}
		circle = (maxDistance - minDistance) / 10;
		System.err.println("The maximum distance is " + maxDistance);
		System.err.println("The minimum distance is " + minDistance);
		System.err.println("The neighbourhood circle " + circle);
		return distanceValue;
	}

	public void readConfig(int lineNum, String line) {
		String theDigits = CharMatcher.DIGIT.retainFrom(line);
		if (lineNum <= 13) {
			switch (lineNum) {
			case 4:
				config[DAYS] = Integer.valueOf(theDigits);
				break;
			case 5:
				config[CAPACITY] = Integer.valueOf(theDigits);
			case 6:
				config[MAX_TRIP_DISTANCE] = Integer.valueOf(theDigits);
				break;
			case 7:
				config[DEPOT_COORDINATE] = Integer.valueOf(theDigits);
				break;
			case 9:
				config[VEHICLE_COST] = Integer.valueOf(theDigits);
				vehicleCost = Long.valueOf(theDigits);
				break;
			case 10:
				config[VEHICLE_DAY_COST] = Integer.valueOf(theDigits);
				vehcileDayCost = Long.valueOf(theDigits);
				break;
			case 11:
				config[DISTANCE_COST] = Integer.valueOf(theDigits);
				distanceCost = Long.valueOf(theDigits);
				break;
			default:
				break;
			}
		}
	}

	public void findAssociation() {
		for (int i = 0; i < requests.size(); i++) {
			Request r1 = requests.get(i);
			for (int j = 0; j < requests.size(); j++) {
				Request r2 = requests.get(j);
				if (r1.getRequestToolKind() == r2.getRequestToolKind()
						&& r1.getRequestToolNumber() >= r2.getRequestToolNumber()
						&& r1.getStart_Time() + r1.getNumOfDaysRequest() >= r2.getEnd_Time()
						&& r1.getEnd_Time() + r1.getNumOfDaysRequest() <= r2.getEnd_Time()) {
					r1.getAssociateRequestForPickUpDelivery().add(r2.getId());
					r1.getAssociateSet().add(r2.getId());
				} else if (r1.getRequestToolKind() == r2.getRequestToolKind()
						&& r2.getRequestToolNumber() >= r1.getRequestToolNumber()
						&& r2.getStart_Time() + r2.getNumOfDaysRequest() >= r1.getEnd_Time()
						&& r2.getEnd_Time() + r2.getNumOfDaysRequest() <= r1.getEnd_Time()) {
					r2.getAssociateRequestForPickUpDelivery().add(r1.getId());
					r2.getAssociateSet().add(r1.getId());
				}
				if (distanceValue[r1.getCustomerID()][r2.getCustomerID()] < circle) {
					r1.getNearByRequests().add(r2.getId());
					r2.getNearByRequests().add(r1.getId());
				}

				if (r1.getAssociateSet().contains(r2.getId()) && r1.getNearByRequests().contains(r2.getId())) {
					r1.getMustTogether().add(r2.getId());
				}
				requests.set(i, r1);
				requests.set(j, r2);
			}
		}
	}

	public void requestHandle() {
		// System.err.println("from request handle");
		for (int i = 0; i < requests.size(); i++) {
			Request curRequest = requests.get(i);
			int toolKind = curRequest.getRequestToolKind();
			int timeWindow = curRequest.getEnd_Time() - curRequest.getStart_Time();
			if (timeWindow == 0) {
				if (!fixedDayDelivery.containsKey(curRequest.getStart_Time())) {
					List<Integer> list = new ArrayList<>();
					list.add(curRequest.getId());
					fixedDayDelivery.put(curRequest.getStart_Time(), list);
				} else {
					fixedDayDelivery.get(curRequest.getStart_Time()).add(curRequest.getId());
				}
			}
			if (!requestToolMapping.containsKey(toolKind)) {
				List<Integer> requests2 = new ArrayList<>();
				requests2.add(curRequest.getId());
				requestToolMapping.put(toolKind, requests2);
			} else {
				requestToolMapping.get(toolKind).add(curRequest.getId());
			}

		}
	}
}

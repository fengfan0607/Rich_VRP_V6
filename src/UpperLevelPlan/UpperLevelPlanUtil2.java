package UpperLevelPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.UpperPlan;

public class UpperLevelPlanUtil2 implements DataIO {

	BlackBoard data;
	int numberOfDays;
	int numberOfRequest;
	List<Request> requests;
	Boolean[] requestsFlag;
	int counter;
	int[][] plan;
	int numberOfTools;
	int[] depotStock;
	int[] dynamicStock;
	int[] config;
	List<List<Integer[]>> associateList;
	Set<Integer> hasAssociationReq;
//	int[] currentPlan;
//	int maximumReq;
	public UpperLevelPlanUtil2(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		this.data = bb;
		// System.err.println(Arrays.toString(data.getConfig()));
		config = data.getConfig();
		numberOfDays = config[DAYS];
		numberOfRequest = config[NUM_OF_REQUESTS];
		requests = data.getRequests();
		requestsFlag = new Boolean[numberOfRequest];
		counter = 0;
		for (int i = 0; i < numberOfRequest; i++) {
			requestsFlag[i] = false;
		}
		plan = new int[numberOfDays][numberOfRequest];
		numberOfTools = config[NUM_OF_TOOLS];
		dynamicStock = new int[data.getToolList().size()];
		depotStock = new int[data.getToolList().size()];
		for (int i = 0; i < numberOfTools; i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
			dynamicStock[i] = data.getToolList().get(i).getNumOfTools();
		}
		associateList = new ArrayList<>();
		for (int i = 0; i < numberOfDays; i++) {
			List<Integer[]> list = new ArrayList<>();
			associateList.add(list);
		}
		hasAssociationReq = new HashSet<>();
//		maximumReq = Integer.MIN_VALUE;
//		currentPlan = new int[numberOfDays];
	}

	public List<List<Integer[]>> getAssociateList() {
		return associateList;
	}

	public void setAssociateList(List<List<Integer[]>> associateList) {
		this.associateList = associateList;
	}

	public int[][] planCreate1() {
		// TODO Auto-generated method stub
		// insert fixed day delivery requests
		TreeMap<Integer, List<Integer>> fixedDayPlan = data.getFixedDayDelivery();
		Set<Integer> keys = fixedDayPlan.keySet();
		Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			int day = itr.next();
			List<Integer> list = fixedDayPlan.get(day);
			for (int i = 0; i < list.size(); i++) {
				int rID = list.get(i);
				Request curReq = requests.get(rID - 1);
				if (!requestsFlag[curReq.getId() - 1]) {
					confirmPlan(day - 1, curReq);
					int dd = day - 1;
					while (curReq.getAssociationTable().containsKey(day + curReq.getNumOfDaysRequest())) {
						Request associateCurReq = findAssociationPickedUpDelivery1(
								curReq.getAssociationTable().get(day + curReq.getNumOfDaysRequest()), curReq);
						if (!requestsFlag[associateCurReq.getId() - 1]) {
							confirmPlan(dd + curReq.getNumOfDaysRequest(), associateCurReq);
							updateAssociateList(dd + curReq.getNumOfDaysRequest(), curReq.getId(),
									associateCurReq.getId());
						}
						dd = dd + curReq.getNumOfDaysRequest();
						curReq = associateCurReq;
					}
				}

			}
		}
		// insert unfixed request
		for (int i = 0; i < requests.size(); i++) {
			if (requestsFlag[i]) {
				continue;
			} else {
				Request curReq = requests.get(i);
				int day = fingDayDeliver(curReq);
				confirmPlan(day - 1, curReq);
				while (curReq.getAssociationTable().containsKey(day + curReq.getNumOfDaysRequest())) {
					Request associateCurReq = findAssociationPickedUpDelivery1(
							curReq.getAssociationTable().get(day + curReq.getNumOfDaysRequest()), curReq);
					if (!requestsFlag[associateCurReq.getId() - 1]) {
						confirmPlan(day + curReq.getNumOfDaysRequest() - 1, associateCurReq);
						updateAssociateList(day + curReq.getNumOfDaysRequest() - 1, curReq.getId(),
								associateCurReq.getId());
					}
					day = day + curReq.getNumOfDaysRequest();
					curReq = associateCurReq;
				}
			}
		}
		printAssociation();
		return plan;
	}

	public void printAssociation() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < associateList.size(); i++) {
			sBuilder.append("day: " + (i + 1));
			List<Integer[]> list = associateList.get(i);
			for (int j = 0; j < list.size(); j++) {
				sBuilder.append(Arrays.toString(list.get(j)));
			}
			sBuilder.append("\n");
		}
		System.err.println(sBuilder.toString());
	}

	public Set<Integer> getHasAssociationReq() {
		return hasAssociationReq;
	}

	public void setHasAssociationReq(Set<Integer> hasAssociationReq) {
		this.hasAssociationReq = hasAssociationReq;
	}

	public int fingDayDeliver(Request curReq) {
		Set<Integer> nearByReq = curReq.getNearByRequests();
		Iterator<Integer> iterator = nearByReq.iterator();
		int day = 0;
		int potentialDay = -1;
		while (iterator.hasNext()) {
			Integer req = iterator.next();
			day = requests.get(req - 1).getPlanedDay();
			if (requestsFlag[req - 1] && day >= curReq.getStart_Time() && day <= curReq.getEnd_Time()) {
				if (curReq.getNearByRequests().contains(requests.get(req - 1).getId())) {
					return day;
				}
				potentialDay = day;
			}
		}
		if (potentialDay != -1) {
			return potentialDay;
		}
		day = ThreadLocalRandom.current().nextInt(curReq.getStart_Time(), curReq.getEnd_Time() + 1);
		return day;
	}

	public Request findAssociationPickedUpDelivery1(List<Integer> list, Request pickUpReq) {
		for (int i = 0; i < list.size(); i++) {
			int reqId = list.get(i);
			if (pickUpReq.getNearByRequests().contains(reqId)) {
				return requests.get(reqId - 1);
			}
		}
		int randomaRequest = ThreadLocalRandom.current().nextInt(0, list.size());
		int requestId = list.get(randomaRequest);
		Request associateRequest = requests.get(requestId - 1);
		return associateRequest;
	}

	public void confirmPlan(int day, Request r) {
		plan[day][r.getId() - 1] = 1;
		plan[day + r.getNumOfDaysRequest()][r.getId() - 1] = -1;
		requestsFlag[r.getId() - 1] = true;
		r.setPlanedDay(day + 1);
		updateCounter();
	}

	public void updateAssociateList(int day, int pickUpReq, int deliverReq) {
		Integer[] ass = new Integer[] { -1 * pickUpReq, deliverReq };
		associateList.get(day).add(ass);
		hasAssociationReq.add(pickUpReq);
		hasAssociationReq.add(deliverReq);
	}

	public void updateCounter() {
		this.counter++;
	}

	public void print(int[][] plans) {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < plans.length; i++) {
			for (int j = 0; j < plans[0].length; j++) {
				sBuilder.append(plans[i][j] + ",");
			}
			sBuilder.append("\n");
		}
		System.err.println(sBuilder.toString());
	}

	public int toolUsedCal(UpperPlan ph) {
		int[] maximumToolUsed = new int[numberOfTools];
		int[][] plan = ph.getPlans();
		for (int i = 0; i < plan.length; i++) {
			int[] toolUsed = new int[numberOfTools];
			for (int j = 0; j < data.getRequests().size(); j++) {
				if (plan[i][j] == 0) {
					continue;
				} else {
					Request request = data.getRequests().get(j);
					int toolKind = request.getRequestToolKind();
					int toolNumber = request.getRequestToolNumber();
					if (plan[i][j] == 1) {
						dynamicStock[toolKind - 1] -= toolNumber;
					} else {
						dynamicStock[toolKind - 1] += toolNumber;
					}
				}
			}

			for (int k = 0; k < numberOfTools; k++) {
				toolUsed[k] = depotStock[k] - dynamicStock[k];
				if (toolUsed[k] > maximumToolUsed[k]) {
					maximumToolUsed[k] = toolUsed[k];
				}
			}
			System.err.println("day" + (i + 1) + "estimated tool uesed" + Arrays.toString(toolUsed));
		}

		int cost = 0;
		for (int k = 0; k < numberOfTools; k++) {
			// System.err.println("tool cost" +
			// data.getToolList().get(k).getCostOfTools());
			cost += maximumToolUsed[k] * data.getToolList().get(k).getCostOfTools();
		}
		return cost;
	}

	public boolean checkToolUsed(UpperPlan ph) {
		int[] maximumToolUsed = new int[numberOfTools];
		int[][] plan = ph.getPlans();
		int[][] toolUsedAll = new int[numberOfDays][numberOfTools];
		for (int i = 0; i < plan.length; i++) {
			int[] toolUsed = new int[numberOfTools];
			for (int j = 0; j < data.getRequests().size(); j++) {
				if (plan[i][j] == 0) {
					continue;
				} else {
					Request request = data.getRequests().get(j);
					int toolKind = request.getRequestToolKind();
					int toolNumber = request.getRequestToolNumber();
					if (plan[i][j] == 1) {
						dynamicStock[toolKind - 1] -= toolNumber;
					} else {
						dynamicStock[toolKind - 1] += toolNumber;
					}
				}
			}

			for (int k = 0; k < numberOfTools; k++) {
				toolUsed[k] = depotStock[k] - dynamicStock[k];
				if (toolUsed[k] > depotStock[k]) {
					return false;
				}
			}
			toolUsedAll[i] = toolUsed;
			System.err.println("day" + (i + 1) + "estimated tool uesed" + Arrays.toString(toolUsed));
		}
		ph.setToolUsed(toolUsedAll);
		return true;

	}

	public int requestDistributionCal(UpperPlan up) {
		int maximumRequestPerDay = Integer.MIN_VALUE;
		int[][] plan = up.getPlans();
		for (int i = 0; i < plan.length; i++) {
			int taskExecutes = 0;
			for (int j = 0; j < plan[0].length; j++) {
				if (plan[i][j] != 0) {
					taskExecutes++;
				}
			}
			maximumRequestPerDay = Math.max(maximumRequestPerDay, taskExecutes);
		}
		return maximumRequestPerDay;
	}
}

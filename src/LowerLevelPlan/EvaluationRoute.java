package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;

public class EvaluationRoute implements DataIO {
	int[] config;
	BlackBoard data;
	int[] depotStock;
	int[] dynamicStock;
	int[][] distance;
	TreeMap<Integer, List<Integer[]>> pickUpStock;
	int numberOfToos;

	public EvaluationRoute(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		this.config = data.getConfig();
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = data.getDistance();
		// dynamicStock = new int[data.getToolList().size()];
		depotStock = new int[data.getToolList().size()];
		pickUpStock = new TreeMap<>();
		for (int i = 0; i < data.getToolList().size(); i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
		}
		numberOfToos = config[NUM_OF_TOOLS];
	}

	public int[] getToolUsedPerDay(List<Integer> route) {
		for (int i = 0; i < route.size(); i++) {
			if (route.get(i) != 0) {
//				updateStock(route.get(i), key);
			}
		}
		int[] currentToolUsed = getToolUsed().clone();
		return currentToolUsed;
	}

	// pickup stock: keep tracks of the tools been picked up per day
	public void updateStock(int request, int vehicleID) {
		int id = Math.abs(request);
		Request r = data.getRequests().get(id - 1);
		int toolID = r.getRequestToolKind();
		int toolNumber = r.getRequestToolNumber();
		if (request > 0) {
			// a deliver action
			if (pickUpStock.containsKey(toolID)) {
				List<Integer[]> toolSet = pickUpStock.get(toolID);
				for (int i = 0; i < toolSet.size(); i++) {
					Integer[] ts = toolSet.get(i).clone();
					if (vehicleID == ts[0]) {
						if (toolNumber >= ts[1]) {
							toolNumber -= ts[1];
							ts[1] = 0;
						} else {
							ts[1] -= toolNumber;
							toolNumber = 0;
						}
						toolSet.set(i, ts);
						pickUpStock.put(toolID, toolSet);
						break;
					}
				}
			}
			dynamicStock[toolID - 1] -= toolNumber;
		} else {
			// a pick up action
			if (!pickUpStock.containsKey(toolID)) {
				List<Integer[]> list = new ArrayList<>();
				Integer[] array = new Integer[] { vehicleID, toolNumber };
				list.add(array);
				pickUpStock.put(toolID, list);
			} else {
				List<Integer[]> toolSet = new ArrayList<>(pickUpStock.get(toolID));
				int index = 0;
				for (int i = 0; i < toolSet.size(); i++) {
					Integer[] ts = toolSet.get(i).clone();
					if (vehicleID == ts[0]) {
						ts[1] += toolNumber;
						toolSet.set(i, ts);
						pickUpStock.put(toolID, toolSet);
						break;
					} else {
						index++;
					}
				}
				if (index == toolSet.size()) {
					Integer[] array = new Integer[] { vehicleID, toolNumber };
					toolSet.add(array);
					pickUpStock.put(toolID, toolSet);
				}
			}

		}
	}

	public int[] getToolUsed() {
		int[] toolUsed = new int[config[NUM_OF_TOOLS]];
		for (int i = 0; i < toolUsed.length; i++) {
			toolUsed[i] = depotStock[i] - dynamicStock[i];
			if (pickUpStock.containsKey(i + 1)) {
				List<Integer[]> list = pickUpStock.get(i + 1);
				for (int k = 0; k < list.size(); k++) {
					Integer[] array = list.get(k).clone();
					// toolUsed[i] += array[1];
					dynamicStock[i] += array[1];
				}
			}
		}
		// setDynamicStock(dynamicStock);
		pickUpStock = new TreeMap<>();
		return toolUsed;
	}
}

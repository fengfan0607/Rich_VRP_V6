package LowerLevelPlan;

import java.util.List;
import java.util.TreeMap;

import dataModel.BlackBoard;

public class toolUsedCal {
	int[] config;
	BlackBoard data;
	int[] depotStock;
	int[] dynamicStock;
	TreeMap<Integer, List<Integer[]>> pickUpStock;

	public toolUsedCal(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		this.config = data.getConfig();
		pickUpStock = new TreeMap<>();
		for (int i = 0; i < data.getToolList().size(); i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
			dynamicStock[i] = data.getToolList().get(i).getNumOfTools();
		}
	}
}

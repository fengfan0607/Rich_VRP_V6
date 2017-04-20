package UpperLevelPlan;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.UpperPlan;

public class UpperPlanGen implements DataIO {
	BlackBoard data;
	UpperLevelPlanUtil utils;

	public UpperPlanGen(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		this.data = bb;
		utils = new UpperLevelPlanUtil(data);
	}

	public UpperPlan planGen() {
		return utils.planCreate();
	}
}

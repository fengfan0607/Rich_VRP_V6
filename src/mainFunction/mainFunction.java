package mainFunction;

import java.util.Arrays;

import DataIO.DataRead;
import LowerLevelPlan.LocalSearch;
import UpperLevelPlan.UpperPlanGen;
import dataModel.BlackBoard;
import dataModel.UpperPlan;

public class mainFunction {
	public static void main(String[] args) {
		DataRead dRead = new DataRead();
		System.err.println("read data .....");
		BlackBoard data = dRead.readData(new BlackBoard());
		UpperPlanGen planGen = new UpperPlanGen(data);
		UpperPlan plan = planGen.planGen();
		LocalSearch ls = new LocalSearch(data);
		ls.search(plan);
	}
}

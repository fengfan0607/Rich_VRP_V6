package LowerLevelPlan;

import java.awt.Event;
import java.util.List;
import java.util.TreeMap;

import org.omg.CORBA.PUBLIC_MEMBER;

import DataIO.DataIO;
import DataIO.DataOutPut;
import dataModel.SolutionForEachDay;
import dataModel.BlackBoard;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class LocalSearch implements DataIO {
	BlackBoard data;
	Evaluation evaluation;
	// interRouteSearch interSwap;
	// intraRouteSearch intraSwap;
	routeGenerationS1 irs1;
	routeGenerationS2 irs2;

	public LocalSearch(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		evaluation = new Evaluation(bb);
		irs1 = new routeGenerationS1(data);
		irs2 = new routeGenerationS2(data);
	}

	public SolutionsAll newLocalSearch(UpperPlan plan) {

		SolutionsAll sa = irs1.createSoltuionByMiminiDistance(plan);
		// SolutionsAll sa = irs2.crateSolutionByInterSwapReq(plan);
		Evaluation evaluation = new Evaluation(data);
		evaluation.costCal(sa);
		return sa;
	}

}

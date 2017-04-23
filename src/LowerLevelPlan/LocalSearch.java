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
	routeGenerationS3 irs3;

	public LocalSearch(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		evaluation = new Evaluation(bb);
		irs1 = new routeGenerationS1(data);
		irs2 = new routeGenerationS2(data);
		irs3 = new routeGenerationS3(data);
	}

	public SolutionsAll newLocalSearch(UpperPlan plan) {

//		SolutionsAll sa1 = irs1.createSoltuionByMiminiDistance(plan);
		// System.err.println("start inter swap");
		// SolutionsAll sa2 = irs2.crateSolutionByInterSwapReq(sa1);
		SolutionsAll sa1 = irs3.createSoltuionByMaximizeToolUse(plan);
		Evaluation evaluation = new Evaluation(data);
		evaluation.costCal(sa1);
		return sa1;
	}

}

package mainFunction;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import DataIO.DataIO;
import DataIO.DataOutPut;
import DataIO.DataRead;
import DataIO.readSolution;
import LowerLevelPlan.Evaluation;
import LowerLevelPlan.LocalSearch;
import LowerLevelPlan.RouteValidator;
import UpperLevelGA.GA;
import UpperLevelGA.Individual;
import UpperLevelPlan.UpperLevelPlanUtil2;
import UpperLevelPlan.UpperPlanGen;
import dataModel.BlackBoard;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;
import test.test;

public class mainFunction implements DataIO {
	public static TreeMap<Integer, SolutionsAll> solutionMap;
	public static List<SolutionsAll> bestSolutionSet;
	public static String alltimebest1 = "r100d5_3";
	public static String alltimebest2 = "r100d10_3";
	public static String alltimebest3 = "r500d15_3";
	public static String alltimebest4 = "r1000d25_3";
	public static String alltimebest5 = "r1000d30_3";

	public static String titleAllTimeBest1 = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 100 requests over 5 days" + "\n\n";
	public static String titleAllTimeBest2 = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 100 requests over 10 days" + "\n\n";
	public static String titleAllTimeBest3 = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 500 requests over 15 days" + "\n\n";
	public static String titleAllTimeBest4 = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 1000 requests over 25 days" + "\n\n";
	public static String titleAllTimeBest5 = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 1000 requests over 30 days" + "\n\n";

	public static String testFile = "testInstance/ParticipantsSuite/ORTEC_Test/ORTEC_Test_03.txt";
	public static String testOutPut = "testInstance/ParticipantsSuite/ORTEC_Test_solution/ORTEC_Test_05_sol.txt";

	public static void main(String[] args) {
		String fileNameAllTimeBest = "testInstance/ParticipantsSuite/AllTimeBest/VeRoLog_";
		String[] allTimeBest = new String[] { alltimebest1, alltimebest2, alltimebest3, alltimebest4, alltimebest5 };
		String[] title = new String[] { titleAllTimeBest1, titleAllTimeBest2, titleAllTimeBest3, titleAllTimeBest4,
				titleAllTimeBest5 };
		bestSolutionSet = new ArrayList<>();
		int numberOfTest = 1;
		solutionMap = new TreeMap<>();
		for (int i = 0; i < numberOfTest; i++) {
			String fileName = fileNameAllTimeBest + allTimeBest[i] + ".txt";
			String outputFileName = "testInstance/ParticipantsSuite/mySolutions/solution.txt";
			DataRead dRead = new DataRead(fileName);
			System.err.println("read data ....." + fileName);
			BlackBoard data = dRead.readData(new BlackBoard());
			// System.err.println(data.getRequests());
//			Individual ind = GA.GA_plan(data);
//			printAssociation(ind.getAssociateList());
			 generateSolutionForInstance(title[i], outputFileName, data, 1,1);
			// generateSolutionForInstance(title[i], testOutPut, data, 5, 5);
		}
		System.err.println("----" + solutionMap);
	}
	public static void printAssociation(List<List<Integer[]>> associateList) {
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

	public static void generateSolutionForInstance(String outputFileTitle, String outputFile, BlackBoard data,
			int numberOfUpperPlan, int numberOfLowerIte) {
		LocalSearch ls = new LocalSearch(data);
		int counter = 0;
		SolutionsAll bestSolution = null;
		while (counter++ < numberOfUpperPlan) {
			List<SolutionsAll> solutionSet = new ArrayList<>();
			DataOutPut.writeFile(outputFile + counter, outputFileTitle, true);
			// use GA to generate upper plan
//			int[][] GA_plan = new int[data.getConfig()[DAYS]][data.getRequests().size()];
			Individual GA_plan = GA.GA_plan(data);
			UpperPlan plan = new UpperPlan();
			plan.setPlans(GA_plan.getChromsome());
			plan.setAssociateList(GA_plan.getAssociateList());
			// lower scheduling
			long bestCost = Long.MAX_VALUE;
			UpperLevelPlanUtil2 utilPlan = new UpperLevelPlanUtil2(data);
			for (int i = 0; i < numberOfLowerIte; i++) {
				plan.setID(i + 1);
				SolutionsAll sa = ls.newLocalSearch(plan);
				solutionSet.add(sa);
				if (sa.getTotalCost() < bestCost) {
					bestSolution = sa;
					bestCost = bestSolution.getTotalCost();
					System.err.println("current best" + bestSolution.toString());
				}
			}
			System.err.println("best solotuion" + bestSolution.toString());
			DataOutPut.writeFile(outputFile + counter, bestSolution.getOutput(), false);
			solutionMap.put(counter, bestSolution);
		}
	}
}

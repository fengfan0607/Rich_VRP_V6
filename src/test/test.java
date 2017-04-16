package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.net.PercentEscaper;

public class test {
	public static void main(String[] args) {
		Integer[] test = new Integer[] { 0, 14, 179, 25, 0};
		List<Integer> testList = new ArrayList<>(Arrays.asList(test));

		Integer[] test1 = new Integer[] { 0, 200, 0};
		List<Integer> testList1 = new ArrayList<>(Arrays.asList(test1));
		// System.err.println(performSwapForEachRoute(testList1));

		System.err.println("cur list " + testList1);
		System.err.println("remove" + selectTask(testList1));
		System.err.println("cur list " + testList1);

		testList.addAll(testList.size(), testList1);
		System.err.println(testList);

	}

	public static int selectTask(List<Integer> list) {
		int n = list.size();
		int pos = ThreadLocalRandom.current().nextInt(0, n);
		int task = list.get(pos);
		list.remove(pos);
		return pos;
	}

	// 0 14 0 25 179 0 283 0 280 0
	// 0 14 179 25 0 283 0 280 0
	public static List<Integer> performSwapForEachRoute(List<Integer> curRoute) {
		System.err.println(curRoute);
		if (curRoute.size() == 3) {
			return curRoute;
		}
		List<Integer> newRoute = new ArrayList<>(curRoute);
		int p1 = getPos(newRoute);
		int p2 = getPos(newRoute);
		while (p1 == p2) {
			p1 = getPos(newRoute);
			p2 = getPos(newRoute);
		}

		int req1 = newRoute.get(p1);
		int req2 = newRoute.get(p2);

		newRoute.set(p1, req2);
		newRoute.set(p2, req1);

		return checkFeasibility(newRoute);
	}

	public static List<Integer> checkFeasibility(List<Integer> route) {
		System.err.println(route);
		int index = 1;
		while (route.get(index) == 0) {
			index++;
		}
		List<Integer> nList = new ArrayList<>();
		nList.add(0);
		for (int i = index; i < route.size() - 1; i++) {
			if (route.get(i) == 0 && route.get(i + 1) == 0) {
				continue;
			}
			nList.add(route.get(i));
		}
		nList.add(0);
		return nList;
	}

	public static int getPos(List<Integer> route) {
		return ThreadLocalRandom.current().nextInt(1, route.size() - 1);
	}
}

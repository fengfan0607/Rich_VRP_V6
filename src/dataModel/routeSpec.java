package dataModel;

import java.util.List;

public class routeSpec {
	private int distanceCost;
	private List<Integer> route;

	@Override
	public String toString() {
		return "routeSpec [distanceCost=" + distanceCost + ", route=" + route + "]";
	}

	public routeSpec() {
		super();
		// TODO Auto-generated constructor stub
	}

	public routeSpec(int distanceCost, List<Integer> route) {
		super();
		this.distanceCost = distanceCost;
		this.route = route;
	}

	public int getDistanceCost() {
		return distanceCost;
	}

	public void setDistanceCost(int distanceCost) {
		this.distanceCost = distanceCost;
	}

	public List<Integer> getRoute() {
		return route;
	}

	public void setRoute(List<Integer> route) {
		this.route = route;
	}

}

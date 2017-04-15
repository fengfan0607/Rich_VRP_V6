package dataModel;

public class Distance {
	private int[][] distance;

	public int[][] getDistance() {
		return distance;
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}

	public int getDistanceXY(int x, int y) {
		return distance[x][y];
	}

	public void setDistanceXY(int x, int y, int value) {
		distance[x][y] = value;
	}

	public Distance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < distance.length; i++) {
			for (int j = 0; j < distance[0].length; j++) {
				sBuilder.append(distance[i][j] + ",");
			}
			sBuilder.append("\n");
		}
//		sBuilder.append(distance[0][3]);
//		sBuilder.append(distance[0][5]);
//		sBuilder.append(distance[0][7]);
		return sBuilder.toString();
	}
}

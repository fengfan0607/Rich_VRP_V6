package dataModel;

public class Depot {
	private int id;
	private int xCor;
	private int yCor;

	@Override
	public String toString() {
		return "Depot [id=" + id + ", xCor=" + xCor + ", yCor=" + yCor + "]";
	}

	public Depot() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getxCor() {
		return xCor;
	}

	public void setxCor(int xCor) {
		this.xCor = xCor;
	}

	public int getyCor() {
		return yCor;
	}

	public void setyCor(int yCor) {
		this.yCor = yCor;
	}
}

package dataModel;

public class Customer {
	// private Requests requests;
	private int xCor;
	private int yCor;
	private int id;


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

	@Override
	public String toString() {
		return "Customer [ xCor=" + xCor + ", yCor=" + yCor + ", id=" + id + "]";
	}

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

package dataModel;

public class Tool {
	private int ID;
	private int size;
	private int numOfTools;
	private int costOfTools;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Tool() {
		super();
	}

	@Override
	public String toString() {
		return "Tool [ID=" + ID + ", size=" + size + ", numOfTools=" + numOfTools + ", costOfTools=" + costOfTools
				+ "]";
	}

	public int getNumOfTools() {
		return numOfTools;
	}

	public void setNumOfTools(int numOfTools) {
		this.numOfTools = numOfTools;
	}

	public int getCostOfTools() {
		return costOfTools;
	}

	public void setCostOfTools(int costOfTools) {
		this.costOfTools = costOfTools;
	}
}

package cs.ualberta.conditionlog;

public class PhotoList {
	private String name = "";
	
	PhotoList(String name) {
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
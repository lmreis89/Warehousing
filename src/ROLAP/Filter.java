package ROLAP;

public class Filter {

	private String factID;
	private String display;
	
	public Filter(String factID, String display) {
		super();
		this.factID = factID;
		this.display = display;
	}

	public String getFactID() {
		return factID;
	}

	public String getDisplay() {
		return display;
	}
	
	
}

package ROLAP;

public class Slice {

	private String dimID;
	private String levelID;
	private int type;
	private String display;
	public Slice(String dimID, String levelID, int type, String display) {
		super();
		this.dimID = dimID;
		this.levelID = levelID;
		this.type = type;
		this.display = display;
	}
	public String getDimID() {
		return dimID;
	}
	public String getLevelID() {
		return levelID;
	}
	public int getType() {
		return type;
	}
	public String getDisplay() {
		return display;
	}
	
}

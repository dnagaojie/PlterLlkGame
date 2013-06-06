package com.plter.linkgame.reader;

public class GamePkg {
	
	public GamePkg(String name,Background background,Picture[] pictures) {
		this.background=background;
		this.name=name;
		this.pictures=pictures;
	}
	
	public String getName() {
		return name;
	}
	public Picture[] getPictures() {
		return pictures;
	}
	public Background getBackground() {
		return background;
	}
	
	private Background background=null;
	private String name="";
	private Picture[] pictures=null;
	
}

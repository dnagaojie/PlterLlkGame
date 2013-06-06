package com.plter.linkgame.reader;

import android.graphics.Bitmap;

public class Picture {
	
	public Picture(Bitmap bitmap) {
		this.bitmap=bitmap;
		id=__getId();
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	private int id=0;
	public int getId() {
		return id;
	}

	private Bitmap bitmap=null;
	private static int __id=0;
	private static int __getId(){
		__id++;
		return __id;
	}
}

package com.plter.linkgame.game;

public class Level {


	public static final Level[] LEVELS = new Level[]{
		new Level(3, 4,10),
		new Level(3, 6,13),
		new Level(4, 5,15),
		new Level(4, 6,18),
		new Level(5, 6,25),
		new Level(5, 8,28),
		new Level(6, 7,30),
		new Level(6, 8,35),
		new Level(6, 9,38),
		new Level(6, 10,40)
	};

	public static final int MAX_H_CARDS_COUNT=6;
	public static final int MAX_V_CARDS_COUNT=10;

	public Level(int h_cards_count,int v_cards_count,int maxTime) {
		this.h_cards_count=h_cards_count;
		this.v_cards_count=v_cards_count;
		this.maxTime=maxTime;
	}

	/**
	 * 水平方向上的卡片的数量
	 * @return
	 */
	public int getH_cards_count() {
		return h_cards_count;
	}

	/**
	 * 垂直方向上的卡片的数量
	 * @return
	 */
	public int getV_cards_count() {
		return v_cards_count;
	}

	public int getMaxTime() {
		return maxTime;
	}

	/**
	 * 当前关卡的最大时间
	 */
	private int maxTime=0;
	private int h_cards_count=0;
	private int v_cards_count=0;

}

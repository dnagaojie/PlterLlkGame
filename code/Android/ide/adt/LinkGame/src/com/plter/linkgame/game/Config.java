package com.plter.linkgame.game;

public class Config {

	
	private static float cardWidth=0;
	private static float cardHeight=0;
	
	private static float screenWidth=0;
	private static float screenHeight=0;
	
	private static float cardsOffsetX=0;
	private static float cardsOffsetY=0;
	
	
	/**
	 * 游戏区域的高度
	 */
	private static float gameCardsAreaWidth=0;
	
	/**
	 * 游戏区域的高度
	 */
	private static float gameCardsAreaHeight=0;
	
	/**
	 * 卡片的上边距
	 */
	public static final float GAME_CARDS_AREA_TOP=80;
	/**
	 * 卡片的下边距
	 */
	public static final float GAME_CARDS_AREA_BOTTOM=80;
	/**
	 * 卡片区域左边距
	 */
	public static final float GAME_CARDS_AREA_LEFT=0;
	
	/**
	 * 卡片区域右边距
	 */
	public static final float GAME_CARDS_AREA_RIGHT=0;

	/**
	 * @return the screenWidth
	 */
	public static float getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @param screenWidth the screenWidth to set
	 */
	public static void setScreenWidth(float screenWidth) {
		Config.screenWidth = screenWidth;
		
		Config.setGameCardsAreaWidth(Config.getScreenWidth()-Config.GAME_CARDS_AREA_LEFT-Config.GAME_CARDS_AREA_RIGHT);
		computeCardWidthAndHeight();
	}

	/**
	 * @return the screenHeight
	 */
	public static float getScreenHeight() {
		return screenHeight;
	}

	/**
	 * @param screenHeight the screenHeight to set
	 */
	public static void setScreenHeight(float screenHeight) {
		Config.screenHeight = screenHeight;
		
		Config.setGameCardsAreaHeight(Config.getScreenHeight()-Config.GAME_CARDS_AREA_BOTTOM-Config.GAME_CARDS_AREA_TOP);
		computeCardWidthAndHeight();
	}
	
	private static void computeCardWidthAndHeight(){
		float cardWidth=Config.getGameCardsAreaWidth()/Level.MAX_H_CARDS_COUNT;
		float cardHeight=Config.getGameCardsAreaHeight()/Level.MAX_V_CARDS_COUNT;
		float min = Math.min(cardWidth, cardHeight);
		Config.setCardWidth(min);
		Config.setCardHeight(min);
	}

	/**
	 * @return the cardWidth
	 */
	public static float getCardWidth() {
		return cardWidth;
	}

	/**
	 * @param cardWidth the cardWidth to set
	 */
	private static void setCardWidth(float cardWidth) {
		Config.cardWidth = cardWidth;
	}

	/**
	 * @return the cardHeight
	 */
	public static float getCardHeight() {
		return cardHeight;
	}

	/**
	 * @param cardHeight the cardHeight to set
	 */
	private static void setCardHeight(float cardHeight) {
		Config.cardHeight = cardHeight;
	}

	/**
	 * @return the cardsOffsetX
	 */
	public static float getCardsOffsetX() {
		return cardsOffsetX;
	}

	/**
	 * @param cardsOffsetX the cardsOffsetX to set
	 */
	public static void setCardsOffsetX(float cardsOffsetX) {
		Config.cardsOffsetX = cardsOffsetX;
	}

	/**
	 * @return the cardsOffsetY
	 */
	public static float getCardsOffsetY() {
		return cardsOffsetY;
	}

	/**
	 * @param cardsOffsetY the cardsOffsetY to set
	 */
	public static void setCardsOffsetY(float cardsOffsetY) {
		Config.cardsOffsetY = cardsOffsetY;
	}

	/**
	 * @return the gameCardsAreaWidth
	 */
	public static float getGameCardsAreaWidth() {
		return gameCardsAreaWidth;
	}

	/**
	 * @param gameCardsAreaWidth the gameCardsAreaWidth to set
	 */
	private static void setGameCardsAreaWidth(float gameCardsAreaWidth) {
		Config.gameCardsAreaWidth = gameCardsAreaWidth;
	}

	/**
	 * @return the gameCardsAreaHeight
	 */
	public static float getGameCardsAreaHeight() {
		return gameCardsAreaHeight;
	}

	/**
	 * @param gameCardsAreaHeight the gameCardsAreaHeight to set
	 */
	private static void setGameCardsAreaHeight(float gameCardsAreaHeight) {
		Config.gameCardsAreaHeight = gameCardsAreaHeight;
	}
}

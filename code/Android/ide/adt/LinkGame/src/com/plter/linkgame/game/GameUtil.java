package com.plter.linkgame.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import android.graphics.RectF;

public class GameUtil {

	
	public static float getXByIndexi(int indexI){
		return indexI*(Config.getCardWidth()+1)+Config.getCardsOffsetX();
	}
	
	
	public static float getYByIndexJ(int indexJ){
		return indexJ*(Config.getCardHeight()+1)+Config.getCardsOffsetY();
	}
	
	
	public static float getCenterXByIndexI(int indexI){
		return getXByIndexi(indexI)+Config.getCardWidth()/2;
	}
	
	public static float getCenterYByIndexJ(int indexJ){
		return getYByIndexJ(indexJ)+Config.getCardHeight()/2;
	}
	
	
	public static PointF getCenterByIndexIJ(int indexI,int indexJ){
		return new PointF(getCenterXByIndexI(indexI), getCenterYByIndexJ(indexJ));
	}
	
	
	/**
	 * 根据中心点取得卡片区域
	 * @return
	 */
	public static RectF getRectFByCenter(PointF p){
		return new RectF(p.x-Config.getCardWidth()/2, p.y-Config.getCardHeight()/2, p.x+Config.getCardWidth()/2, p.y+Config.getCardHeight()/2);
	}
	
	
	/**
	 * 对卡片执行一级测试
	 * @param	genLines 是否生成连线
	 * @return
	 */
	public static boolean testCards1(int i1,int j1,int i2,int j2,boolean genLines){

		boolean conn=true;

		if (i1==i2) {
			int min,max;
			if (j1>j2) {
				min=j2;
				max=j1;
			}else{
				min=j1;
				max=j2;
			}

			//纵向测试
			conn=true;
			for (int j = min+1; j < max; j++) {
				if (currentGameCardsMap[i1][j]!=null) {
					conn=false;
					break;
				}
			}
			if (conn) {
				if (genLines) {
					lastLinkedLinePoints.clear();
					lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
					lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
				}
				return true;
			}
		}else if (j1==j2) {

			int min,max;
			if (i1>i2) {
				min=i2;
				max=i1;
			}else{
				min=i1;
				max=i2;
			}

			//横向测试
			conn=true;
			for (int i = min+1; i < max; i++) {
				if (currentGameCardsMap[i][j1]!=null) {
					conn=false;
					break;
				}
			}
			if (conn) {
				if (genLines) {
					lastLinkedLinePoints.clear();
					lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
					lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
				}
				return true;
			}
		}

		return false;
	}


	/**
	 * 对卡片执行二级测试
	 * @param genLines 是否生成连线
	 * @return
	 */
	public static boolean testCards2(int i1,int j1,int i2,int j2,boolean genLines){

		int middleI1=i1,middleJ1=j2,middleI2=i2,middleJ2=j1;
		PointF middle1Center=new PointF(GameUtil.getCenterXByIndexI(middleI1), GameUtil.getCenterYByIndexJ(middleJ1)),
				middle2Center=new PointF(GameUtil.getCenterXByIndexI(middleI2), GameUtil.getCenterYByIndexJ(middleJ2));

		if (currentGameCardsMap[middleI1][middleJ1]==null&&
				testCards1(middleI1, middleJ1, i1, j1, false)&&
				testCards1(middleI1, middleJ1, i2, j2, false)) {

			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(middle1Center);
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}
			return true;
		}

		if (currentGameCardsMap[middleI2][middleJ2]==null&&
				testCards1(middleI2, middleJ2, i1, j1, false)&&
				testCards1(middleI2, middleJ2, i2, j2, false)) {

			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(middle2Center);
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}
			return true;
		}


		return false;
	}


	/**
	 * 对卡片执行三级横向测试
	 * @return
	 */
	public static boolean testCards3H(int i1,int j1,int i2,int j2,boolean genLines){
		int i=0;
		for (i = i1+1; i < currentLevel.getH_cards_count(); i++) {
			if (i!=i2&&currentGameCardsMap[i][j1]==null) {
				if (testCards1(i, j1, i1, j1, false)&&testCards2(i, j1, i2, j2, false)) {
					if (genLines) {
						lastLinkedLinePoints.clear();
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j2));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
					}

					return true;
				}
			}
		}
		i=currentLevel.getH_cards_count();
		if (testCards1(i, j1, i1, j1, false)&&testCards1(i, j2, i2, j2, false)) {
			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j2));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}

			return true;
		}

		for (i = i1-1; i >0; i--) {
			if (i!=i2&&currentGameCardsMap[i][j1]==null) {
				if (testCards1(i, j1, i1, j1, false)&&testCards2(i, j1, i2, j2, false)) {

					if (genLines) {
						lastLinkedLinePoints.clear();
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j2));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
					}

					return true;
				}
			}
		}

		i=-1;
		if (testCards1(i, j1, i1, j1, false)&&testCards1(i, j2, i2, j2, false)) {
			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i, j2));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}

			return true;
		}

		return false;
	}


	/**
	 * 对卡片执行三级纵向测试
	 * @return
	 */
	public static boolean testCards3V(int i1,int j1,int i2,int j2,boolean genLines){
		int j=0;
		for (j = j1+1; j < currentLevel.getV_cards_count(); j++) {
			if (j!=j2&&currentGameCardsMap[i1][j]==null) {
				if (testCards1(i1, j, i1, j1, false)&&testCards2(i1, j, i2, j2, false)) {
					if (genLines) {
						lastLinkedLinePoints.clear();
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
					}
					return true;
				}
			}
		}
		j=currentLevel.getV_cards_count();
		if (testCards1(i1, j, i1, j1, false)&&testCards1(i2, j, i2, j2, false)) {

			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}
			return true;
		}

		for (j = j1-1; j >0; j--) {
			if (j!=j2&&currentGameCardsMap[i1][j]==null) {
				if (testCards1(i1, j, i1, j1, false)&&testCards2(i1, j, i2, j2, false)) {

					if (genLines) {
						lastLinkedLinePoints.clear();
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j));
						lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
					}
					return true;
				}
			}
		}

		j=-1;
		if (testCards1(i1, j, i1, j1, false)&&testCards1(i2, j, i2, j2, false)) {

			if (genLines) {
				lastLinkedLinePoints.clear();
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j1));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i1, j));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j));
				lastLinkedLinePoints.add(GameUtil.getCenterByIndexIJ(i2, j2));
			}
			return true;
		}
		return false;
	}


	/**
	 * 对卡片执行三级测试
	 * @return
	 */
	public static boolean testCards3(int i1,int j1,int i2,int j2,boolean genLines){

		if (i1==i2) {
			if (testCards3H(i1, j1, i2, j2, genLines)) {
				return true;
			}
		}else if (j1==j2) {
			if (testCards3V(i1, j1, i2, j2, genLines)) {
				return true;
			}
		}else{
			if (testCards3H(i1, j1, i2, j2, genLines)) {
				return true;
			}
			if (testCards3V(i1, j1, i2, j2, genLines)) {
				return true;
			}
		}

		return false;
	}


	public static boolean testCards(Level currentLevel,Card[][] currentGameCardsMap,int i1,int j1,int i2,int j2,boolean genLines){
		GameUtil.currentLevel=currentLevel;
		GameUtil.currentGameCardsMap=currentGameCardsMap;
		
		
		if (i1==i2||j1==j2) {
			//一级测试
			if (testCards1(i1,j1,i2,j2,genLines)) {
				return true;
			}else if (testCards3(i1,j1,i2,j2,genLines)) {//三级测试
				return true;
			}
		}else{
			//二级测试
			if (testCards2(i1,j1,i2,j2,genLines)) {
				return true;
			}else if(testCards3(i1,j1,i2,j2,genLines)){//三级测试
				return true;
			}
		}	

		return false;
	}
	
	
	/**
	 * 剩余的卡片是否连通
	 * @param connectedCards 长度为2的Card数组，用于存放连通的卡片
	 * @return
	 */
	public static boolean isGameConnected(Level currentLevel,List<Card> currentGameCards,Card[][] currentGameCardsMap,Card[] connectedCards){
		
		Card card1,card2;
				
		for (int i = 0; i < currentGameCards.size(); i++) {
			card1=currentGameCards.get(i);
			
			for (int j = i+1; j < currentGameCards.size(); j++) {				
				card2=currentGameCards.get(j);
				
				if (card1.getPicture().getId()==card2.getPicture().getId()) {
					
					if (testCards(currentLevel, currentGameCardsMap, card1.getIndexI(), card1.getIndexJ(), card2.getIndexI(), card2.getIndexJ(), false)) {						
						
						if (connectedCards!=null&&connectedCards.length>=2) {
							connectedCards[0]=card1;
							connectedCards[1]=card2;
						}
						
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	
	public static final List<PointF> lastLinkedLinePoints = new ArrayList<PointF>();
	
	private static Level currentLevel=null;
	private static Card[][] currentGameCardsMap=null;
}

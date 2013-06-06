package com.plter.linkgame.game;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plter.linkgame.reader.GamePkg;
import com.plter.linkgame.reader.Picture;

public class GameView extends FrameLayout{

	public GameView(Context context) {
		super(context);
	}


	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	/**
	 * 根据游戏资源包初始化
	 * @param gamePkg
	 */
	public void initWithGamePkg(GamePkg gamePkg){

		setGamePkg(gamePkg);

		//设置背景
		setBackgroundDrawable(new BitmapDrawable(getGamePkg().getBackground().getBitmap()));

		//构建卡片容器
		cardsContainer=new RelativeLayout(getContext());
		addView(cardsContainer, -1, -1);

		//构建连线容器
		linesContainer=new LinesContainer(getContext());
		addView(linesContainer, -1, -1);
	}

	public void showStartGameAlert(){
		setEnabled(false);
		new AlertDialog.Builder(getContext())
		.setTitle("准备好了吗？")
		.setMessage("点击“开始”按钮开始游戏")
		.setCancelable(false)
		.setPositiveButton("开始", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				reset();
				startGame();				
			}
		}).show();
	}


	/**
	 * 重置关卡
	 */
	public void reset(){
		levelNum=0;
	}


	/**
	 * 开始游戏
	 */
	public void startGame(){
		level=Level.LEVELS[levelNum];
		currentTime=level.getMaxTime();

		getLevelTv().setText(String.format("第%d关，", levelNum+1));

		genGameCards();
		addGameCards();

		startGameTimerHandler();

		gameRunning=true;

		setEnabled(true);
	}


	/**
	 * 打乱卡片
	 */
	public void breakGameCards(){
		gameCardsMap=new Card[level.getH_cards_count()][level.getV_cards_count()];

		allIndex.clear();
		for (int i = 0; i < level.getH_cards_count(); i++) {
			for (int j = 0; j < level.getV_cards_count(); j++) {
				allIndex.add(new Point(i, j));
			}
		}

		Card card=null;
		Point point=null;

		for (int i = 0; i < gameCards.size(); i++) {
			card=gameCards.get(i);
			point=allIndex.remove((int) (Math.random()*allIndex.size()));

			card.setIndexI(point.x);
			card.setIndexJ(point.y);
			card.resetPositionByIndexIJ();
			gameCardsMap[point.x][point.y]=card;
		}

		if (!GameUtil.isGameConnected(level, gameCards, gameCardsMap,null)) {
			breakGameCards();
		}
	}

	/**
	 * 所有的可用索引
	 */
	private final List<Point> allIndex=new ArrayList<Point>();



	/**
	 * 将游戏卡片添加到场景中 
	 */
	public void addGameCards(){

		breakGameCardsArray();

		Card card;
		int index=0;

		Config.setCardsOffsetX(Config.GAME_CARDS_AREA_LEFT+(Config.getGameCardsAreaWidth()-Config.getCardWidth()*level.getH_cards_count())/2);
		Config.setCardsOffsetY(Config.GAME_CARDS_AREA_TOP+(Config.getGameCardsAreaHeight()-Config.getCardHeight()*level.getV_cards_count())/2);

		gameCardsMap=new Card[level.getH_cards_count()][level.getV_cards_count()];

		for (int i = 0; i < level.getH_cards_count(); i++) {
			for (int j = 0; j < level.getV_cards_count(); j++) {
				index=j+i*level.getV_cards_count();
				card = gameCards.get(index);
				cardsContainer.addView(card, (int)(Config.getCardWidth()), (int)(Config.getCardHeight()));

				card.setIndexI(i);
				card.setIndexJ(j);
				card.resetPositionByIndexIJ();
				gameCardsMap[i][j]=card;
			}
		}

		if (!GameUtil.isGameConnected(level, gameCards, gameCardsMap, null)) {
			breakGameCards();
		}
	}


	/**
	 * 生成游戏卡片
	 */
	public void genGameCards(){

		cardsContainer.removeAllViews();
		gameCards.clear();

		int halfCardsCount=level.getH_cards_count()*level.getV_cards_count()/2;
		Picture pic=null;
		Card card;

		for (int i = 0; i < halfCardsCount; i++) {
			pic=getGamePkg().getPictures()[(int) (Math.random()*getGamePkg().getPictures().length)];

			card=new Card(getContext(),pic);
			card.setOnClickListener(cardClickHandler);
			gameCards.add(card);
			card=new Card(getContext(),pic);
			card.setOnClickListener(cardClickHandler);		
			gameCards.add(card);
		}
	}

	private final OnClickListener cardClickHandler=new OnClickListener() {

		public void onClick(View v) {
			currentCheckedCard = (Card) v;
			currentCheckedCard.setChecked(true);

			if (lastCheckedCard!=null) {
				if (lastCheckedCard!=currentCheckedCard) {
					if(testCards()){
						gameCardsMap[currentCheckedCard.getIndexI()][currentCheckedCard.getIndexJ()]=null;
						gameCardsMap[lastCheckedCard.getIndexI()][lastCheckedCard.getIndexJ()]=null;
						cardsContainer.removeView(currentCheckedCard);
						cardsContainer.removeView(lastCheckedCard);

						gameCards.remove(currentCheckedCard);
						gameCards.remove(lastCheckedCard);

						currentCheckedCard.setOnClickListener(null);
						lastCheckedCard.setOnClickListener(null);

						linesContainer.showLines(GameUtil.lastLinkedLinePoints);

						if (gameCards.size()>0){
							if (!GameUtil.isGameConnected(level, gameCards, gameCardsMap,null)) {
								breakGameCards();
							}
						}else {
							
							//如果还有时间，则弹出成功对话框
							if (gameRunning) {
								stopGameTimerHandler();
								gameRunning=false;

								if (levelNum<Level.LEVELS.length-1) {

									setEnabled(false);

									new AlertDialog.Builder(getContext())
									.setTitle("恭喜")
									.setMessage(String.format("游戏将进入第%d关", levelNum+2))
									.setCancelable(false)
									.setPositiveButton("继续", new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											levelNum++;
											startGame();										
										}
									}).show();

								}else{

									setEnabled(false);
									new AlertDialog.Builder(getContext())
									.setTitle("恭喜")
									.setMessage("你真厉害，已经通关了")
									.setCancelable(false)
									.setNegativeButton("退出", new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											System.exit(0);
										}
									})
									.setPositiveButton("重新再来", new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											reset();
											startGame();
										}
									})
									.show();
								}
							}
						}
					}else{
						lastCheckedCard.setChecked(false);
						currentCheckedCard.setChecked(false);
					}
				}else{
					lastCheckedCard.setChecked(false);
				}

				lastCheckedCard=null;
				currentCheckedCard=null;

			}else{
				lastCheckedCard=currentCheckedCard;
			}
		}
	};


	/**
	 * 测试卡片是否联通
	 * @return 
	 */
	private boolean testCards(){
		if (lastCheckedCard.getPicture().getId()==currentCheckedCard.getPicture().getId()) {

			int i1=lastCheckedCard.getIndexI(),		j1=lastCheckedCard.getIndexJ(),
					i2=currentCheckedCard.getIndexI(),	j2=currentCheckedCard.getIndexJ();

			if (GameUtil.testCards(level,gameCardsMap,i1, j1, i2, j2, true)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 取得关卡数
	 * @return
	 */
	public int getLevelNum(){
		return levelNum;
	}


	public Level getLevel(){
		return level;
	}

	
	/**
	 * 暂停游戏
	 */
	public void pauseGame(){
		gameRunning=false;
		pause();
		
		new AlertDialog.Builder(getContext())
		.setCancelable(false)
		.setTitle("暂停中")
		.setMessage("游戏暂停中，请点击“继续”按钮继续游戏")
		.setPositiveButton("继续", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				gameRunning=true;
				resume();
			}
		})
		.show();
	}
	

	/**
	 * 继续
	 */
	public void resume(){
		if (gameRunning) {
			startGameTimerHandler();
		}
	}


	/**
	 * 暂停
	 */
	public void pause(){
		stopGameTimerHandler();
	}


	public boolean isTimerRunning() {
		return timerRunning;
	}


	/**
	 * 打乱卡片数组
	 */
	private void breakGameCardsArray(){
		for (int i = 0; i < 200; i++) {
			gameCards.add(gameCards.remove((int)(Math.random()*gameCards.size())));
		}
	}


	/**
	 * 停止计时
	 */
	private void stopGameTimerHandler(){
		gameTimerHandler.removeMessages(1);
		timerRunning=false;
	}


	/**
	 * 启动游戏计时
	 */
	private void startGameTimerHandler(){
		gameTimerHandler.sendEmptyMessage(1);
		timerRunning=true;
	}


	public TextView getTimeTv() {
		return timeTv;
	}


	public void setTimeTv(TextView timeTv) {
		this.timeTv = timeTv;
	}

	public TextView getLevelTv() {
		return levelTv;
	}


	public void setLevelTv(TextView levelTv) {
		this.levelTv = levelTv;
	}

	public Button getBreakCardsBtn() {
		return breakCardsBtn;
	}


	public void setBreakCardsBtn(Button breakCardsBtn) {
		if (this.breakCardsBtn!=null) {
			this.breakCardsBtn.setOnClickListener(null);
		}

		this.breakCardsBtn = breakCardsBtn;
		breakCardsBtn.setOnClickListener(breakCardsBtnClickHandler);
	}


	private final OnClickListener breakCardsBtnClickHandler=new OnClickListener() {

		public void onClick(View v) {
			breakGameCards();
		}
	};

	private final OnClickListener noteBtnClickHandler=new OnClickListener() {

		public void onClick(View v) {

			if (gameCards.size()>=2) {
				Card[] notedCards = new Card[2];
				if(GameUtil.isGameConnected(level, gameCards, gameCardsMap, notedCards)){
					notedCards[0].startNoteAnim();
					notedCards[1].startNoteAnim();
				}
			}
		}
	};

	private final OnClickListener pauseBtnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			pauseGame();
		}
	};


	public Button getNoteBtn() {
		return noteBtn;
	}


	public void setNoteBtn(Button noteBtn) {
		if (this.noteBtn!=null) {
			this.noteBtn.setOnClickListener(null);
		}
		this.noteBtn = noteBtn;

		noteBtn.setOnClickListener(noteBtnClickHandler);
	}

	/**
	 * @return the gamePkg
	 */
	public GamePkg getGamePkg() {
		return gamePkg;
	}


	/**
	 * @param gamePkg the gamePkg to set
	 */
	private void setGamePkg(GamePkg gamePkg) {
		this.gamePkg = gamePkg;
	}

	/**
	 * @return the pauseBtn
	 */
	public Button getPauseBtn() {
		return pauseBtn;
	}


	/**
	 * @param pauseBtn the pauseBtn to set
	 */
	public void setPauseBtn(Button pauseBtn) {
		if (this.pauseBtn!=null) {
			this.pauseBtn.setOnClickListener(null);
		}
		
		this.pauseBtn = pauseBtn;
		
		this.pauseBtn.setOnClickListener(pauseBtnClickListener);
	}

	private final List<Card> gameCards = new ArrayList<Card>();

	/**
	 * 游戏包对象
	 */
	private GamePkg gamePkg=null;

	/**
	 * 关卡数
	 */
	private int levelNum=1;
	private Level level=null;
	private int currentTime=0;


	/**
	 * 游戏卡片图
	 */
	private Card[][] gameCardsMap = null;
	private Card lastCheckedCard=null;
	private Card currentCheckedCard=null;
	private LinesContainer linesContainer=null;
	private RelativeLayout cardsContainer=null;
	private TextView timeTv=null;
	private TextView levelTv=null;
	private Button breakCardsBtn=null,noteBtn=null,pauseBtn;

	/**
	 * 游戏是否正在运行，计时器是否正在运行
	 */
	private boolean gameRunning=false,timerRunning=false;

	private final Handler gameTimerHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {

			getTimeTv().setText(String.format("时间：%d", currentTime));

			if (currentTime>0) {
				currentTime--;
				gameTimerHandler.sendEmptyMessageDelayed(1, 1000);
			}else{
				if (gameCards.size()>0) {

					gameRunning=false;
					timerRunning=false;

					setEnabled(false);
					new AlertDialog.Builder(getContext())
					.setTitle("很遗憾")
					.setMessage("本关未通过，游戏结束")
					.setCancelable(false)
					.setPositiveButton("重新再来", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							reset();
							startGame();							
						}
					})
					.setNegativeButton("退出", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					}).show();
				}
			}
		}
	};
}

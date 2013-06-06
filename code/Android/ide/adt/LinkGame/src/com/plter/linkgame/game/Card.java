package com.plter.linkgame.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plter.linkgame.reader.Picture;

public class Card extends FrameLayout {

	
	public Card(Context context,Picture pic) {
		super(context);
		
		picture=pic;
		
		imageView=new ImageView(getContext());
		imageView.setImageBitmap(getPicture().getBitmap());
		addView(imageView, -1, -1);
		
		noteRect=createRectLine(0xFF00FF00);
		addView(noteRect, -1, -1);
		noteRect.setVisibility(View.INVISIBLE);
		
		checkedRect=createRectLine(0xFFFF0000);
		addView(checkedRect, -1, -1);
		setChecked(false);
		
		//config note animation
		cna.setDuration(3000);
		cna.setAnimationListener(cardNoteAnimListener);
	}
	
	
	public void setX(float x) {
		lp=getLayoutParams();
		lp.leftMargin=(int) x;
		setLayoutParams(lp);
		resetCenter();
	}
	
	
	public void setY(float y) {
		lp=getLayoutParams();
		lp.topMargin=(int) y;
		setLayoutParams(lp);
		resetCenter();
	}
	
	public void setXY(float x,float y){
		lp=getLayoutParams();
		lp.topMargin=(int) y;
		lp.leftMargin=(int) x;
		setLayoutParams(lp);
		resetCenter();
	}
	
	
	/**
	 * 根据index i j 重置图片的位置
	 */
	public void resetPositionByIndexIJ(){
		setX(GameUtil.getXByIndexi(getIndexI()));
		setY(GameUtil.getYByIndexJ(getIndexJ()));
	}
	
	
	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}


	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;	
		
		if (checked) {
			checkedRect.setVisibility(View.VISIBLE);
			
			stopNoteAnim();
		}else{
			checkedRect.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * @return the indexI
	 */
	public int getIndexI() {
		return indexI;
	}


	/**
	 * @param indexI the indexI to set
	 */
	public void setIndexI(int indexI) {
		this.indexI = indexI;
	}

	/**
	 * @return the indexJ
	 */
	public int getIndexJ() {
		return indexJ;
	}


	/**
	 * @param indexJ the indexJ to set
	 */
	public void setIndexJ(int indexJ) {
		this.indexJ = indexJ;
	}
	
	
	public float getCenterX() {
		return getCenter().x;
	}
	
	
	public float getCenterY() {
		return getCenter().y;
	}
	
	
	public float getX(){
		return getLayoutParams().leftMargin;
	}
	
	public float getY(){
		return getLayoutParams().topMargin;
	}
	
	
	
	public RelativeLayout.LayoutParams getLayoutParams() {
		return (android.widget.RelativeLayout.LayoutParams) super.getLayoutParams();
	}
	
	
	private void resetCenter(){
		getCenter().x=getX()+Config.getCardWidth()/2;
		getCenter().y=getY()+Config.getCardHeight()/2;
	}

	/**
	 * @return the center
	 */
	public PointF getCenter() {
		return center;
	}
	
	
	/**
	 * 开始提示动画
	 */
	public void startNoteAnim(){
		noteRect.setVisibility(View.VISIBLE);
		noteRect.startAnimation(cna);
	}
	
	
	/**
	 * 停止提示动画
	 */
	public void stopNoteAnim(){
		if (noteRect.getVisibility()!=View.VISIBLE) {
			noteRect.setAnimation(null);
			noteRect.setVisibility(View.INVISIBLE);
		}
	}
	
	private View createRectLine(int color){
		final Paint frontShapePaint=new Paint();
		frontShapePaint.setColor(color);
		frontShapePaint.setStyle(Style.STROKE);
		frontShapePaint.setStrokeWidth(lineWidth);
		
		return new View(getContext()){
			
			protected void onDraw(Canvas canvas) {
				canvas.drawRect(halfWidth, halfWidth, Config.getCardWidth()-lineWidth, Config.getCardHeight()-lineWidth, frontShapePaint);
				super.onDraw(canvas);
			}
		};
	}
	

	private Picture picture=null;
	private boolean checked=false;
	private int indexI=0;
	private int indexJ=0;
	private final PointF center=new PointF();
	private final float lineWidth=3;
	private final float halfWidth=lineWidth/2;
	private RelativeLayout.LayoutParams lp;
	private ImageView imageView=null;
	private View checkedRect=null;
	private View noteRect=null;
	private final CardNoteAnim cna = new CardNoteAnim();
	private final AnimationListener cardNoteAnimListener=new AnimationListener() {
		
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		public void onAnimationEnd(Animation animation) {
			noteRect.setVisibility(View.INVISIBLE);
		}
	};
}

package com.plter.linkgame.game;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class LinesContainer extends View implements AnimationListener{

	public LinesContainer(Context context) {
		super(context);
		
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setColor(0xFFFF0000);
		
		aa.setDuration(500);
		aa.setAnimationListener(this);
		
		setVisibility(View.GONE);
	}
	
	
	public void showLines(List<PointF> points){
		if (points.size()<2) {
			throw new RuntimeException("点的个数不能小于2");
		}else{
			
			setVisibility(View.VISIBLE);
			
			this.points=points;
			
			invalidate();
			startAnimation(aa);
		}
	}
	
	
	
	protected void onDraw(Canvas canvas) {
		
		if (points==null||points.size()<2) {
			return;
		}
		
		
		path.reset();
		
		PointF p=points.get(0);
		path.moveTo(p.x, p.y);
		
		for (int i = 1; i < points.size(); i++) {
			p=points.get(i);
			path.lineTo(p.x, p.y);
		}
		
		canvas.drawPath(path, paint);
		super.onDraw(canvas);
	}
	
	
	private List<PointF> points=null;
	private final Paint paint=new Paint();
	private final Path path = new Path();
	private final AlphaAnimation aa = new AlphaAnimation(1, 0);
	
	public void onAnimationStart(Animation animation) {}

	public void onAnimationEnd(Animation animation) {
		setVisibility(View.GONE);
	}

	public void onAnimationRepeat(Animation animation) {}
}

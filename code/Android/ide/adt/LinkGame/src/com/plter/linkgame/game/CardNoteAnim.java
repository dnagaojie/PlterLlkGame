package com.plter.linkgame.game;

import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * 卡片提示动画效果
 * @author xtiqin
 *
 */
public class CardNoteAnim extends Animation {

	
	
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		t.setAlpha((float) Math.abs(Math.sin(interpolatedTime*100)));
		super.applyTransformation(interpolatedTime, t);
	}
}

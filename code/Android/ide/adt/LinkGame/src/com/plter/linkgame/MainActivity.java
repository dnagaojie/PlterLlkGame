package com.plter.linkgame;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.plter.lib.android.java.controls.ArrayAdapter;

public class MainActivity extends ListActivity {

	
	private ArrayAdapter<GameListCellData> adapter;
	private ProgressDialog dialog=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		adapter=new ArrayAdapter<MainActivity.GameListCellData>(this,R.layout.game_list_cell) {
			
			@Override
			public void initListCell(int position, View listCell, ViewGroup parent) {
				ImageView iconIv = (ImageView) listCell.findViewById(R.id.iconIv);
				TextView labelTv=(TextView) listCell.findViewById(R.id.labelTv);
				
				GameListCellData data = getItem(position);
				iconIv.setImageResource(data.iconResId);
				labelTv.setText(data.label);
			}
		};
		
		setListAdapter(adapter);
		
		adapter.add(new GameListCellData("水果连连看", R.drawable.sg_icon, "sg_config.json"));
		adapter.add(new GameListCellData("蔬菜连连看", R.drawable.sc_icon, "sc_config.json"));
		adapter.add(new GameListCellData("动物连连看", R.drawable.dw_icon, "dw_config.json"));
		adapter.add(new GameListCellData("爱心连连看", R.drawable.love_icon, "love_config.json"));
		adapter.add(new GameListCellData("宝石连连看", R.drawable.coin_icon, "coin_config.json"));
	}
	
	
	@Override
	protected void onPause() {
		
		if (dialog!=null) {
			dialog.dismiss();
			dialog=null;
		}
		
		super.onPause();
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		dialog=ProgressDialog.show(this, "请稍候", "正在加载游戏资源");
		
		GameListCellData data = adapter.getItem(position);
		Intent i = new Intent(this, LinkGameActivity.class);
		i.putExtra("configFile", data.gameConfigFile);
		startActivity(i);
		
		super.onListItemClick(l, v, position, id);
	}
	
	
	public static class GameListCellData{
		
		public GameListCellData(String label,int iconResId,String gameConfigFile) {
			this.label=label;
			this.iconResId=iconResId;
			this.gameConfigFile=gameConfigFile;
		}
		
		public String label=null;
		public int iconResId=0;
		public String gameConfigFile=null;
	}
}

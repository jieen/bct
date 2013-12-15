package com.jieen.bct;

import com.baidu.frontia.Frontia;
import com.jieen.bct.Pcs.pcsMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BctMain extends Activity {

	private Button btnPcsMain = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bctmain);
		btnPcsMain = (Button) findViewById(R.id.btnPcs);
		btnPcsMain.setOnClickListener(btnMainClickListener);
		
	}
	//主页面按钮统一处理函数
	private OnClickListener btnMainClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.btnPcs:	//进入PCS主页按钮
				Intent pcsIntent = new Intent(BctMain.this,pcsMain.class);
				startActivity(pcsIntent);
				break;
			}
		}
	};
	
}

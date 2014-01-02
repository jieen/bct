package com.jieen.bct;

import com.jieen.bct.Pcs.pcsMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class BctMain extends Activity {

	private ImageButton btnPcsMain = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bctmain);
		btnPcsMain = (ImageButton) findViewById(R.id.ibpcs);
		btnPcsMain.setOnClickListener(btnMainClickListener);
		
	}
	//��ҳ�水ťͳһ������
	private OnClickListener btnMainClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.ibpcs:	//����PCS��ҳ��ť
				Intent pcsIntent = new Intent(BctMain.this,pcsMain.class);
				startActivity(pcsIntent);
				break;
			}
		}
	};
	
}

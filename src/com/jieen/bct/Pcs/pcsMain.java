package com.jieen.bct.Pcs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaResult;
import com.jieen.bct.R;

public class pcsMain extends Activity {
	
	private FrontiaPersonalStorage mCloudStorage;
	private FrontiaAuthorization authorization;

	private TextView mResultTextView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pcsmain);
		Frontia.init(this.getApplicationContext(), PcsConf.APIKEY);
		ArrayList<String> list = new ArrayList<String>();
		list.add("basic");
		list.add("netdisk");
		mCloudStorage = Frontia.getPersonalStorage();	//��ȡPCSʵ��
		authorization = Frontia.getAuthorization();		//��ȡ��Ȩʵ��
		authorization.authorize(this,MediaType.BAIDU.toString(),list,new AuthorizationListener() {
			//��Ȩ�ɹ�
			public void onSuccess(FrontiaUser arg0) {
				Frontia.setCurrentAccount(arg0);
				setupViews();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				TextView view = new TextView(pcsMain.this);
				view.setText("��¼�ٶ��˺�ʧ�ܣ�����Ϊ:"+arg0+""+arg1+"��ֻ�е�¼�ٶ��˺Ų���ʹ�ø����ļ��洢���ܣ��뷵�����³��Ե�¼");
				setContentView(view);
			}
			
			@Override
			public void onCancel() {
				TextView view = new TextView(pcsMain.this);
				view.setText("ֻ�е�¼�ٶ��˺Ų���ʹ�ø����ļ��洢���ܣ��뷵�����³��Ե�¼");
				setContentView(view);
			}
		});
		
	}
	
	private void setupViews() {
		setContentView(R.layout.pcsmain);
		mResultTextView = (TextView) findViewById(R.id.personalFileResult);

		Button createDirButton = (Button) findViewById(R.id.createDir);
		createDirButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				createDir();		//����Ŀ¼
			}
		});
		
		Button listButton = (Button) findViewById(R.id.personalList);
		listButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				list();				//�г��ļ�
			}

		});
		
		Button quotaButton = (Button) findViewById(R.id.personalQuota);
		quotaButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				quota();		//��ѯ�ռ�
			}

		});
	}
	//����Ŀ¼ʵ�ַ���
	protected void createDir() {
		mCloudStorage.makeDir(
				PcsConf.PERSON_STORAGE_DIR_NAME,
				new FileInfoListener() {
					@Override
					public void onSuccess(FileInfoResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append("create dir success\n")
								.append(result.getPath())
								.append('\n')
								.append("size: ")
								.append(result.getSize())
								.append('\n')
								.append("modified time: ")
								.append(new Date(result.getModifyTime() * 1000));
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}
	//�г��ļ�����
	protected void list() {
		mCloudStorage.list(PcsConf.PERSON_STORAGE_DIR_NAME, null,
				null, new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {
							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime()*1000)
											.toString()).append('\n');
						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}
	//��ѯʹ�ÿռ䷽��
	protected void quota() {

		mCloudStorage.quota(
				new QuotaListener() {

					@Override
					public void onSuccess(QuotaResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append("total: ").append(result.getTotal())
								.append('\n').append("used: ")
								.append(result.getUsed()).toString();
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

}

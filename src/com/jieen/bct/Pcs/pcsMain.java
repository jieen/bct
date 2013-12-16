package com.jieen.bct.Pcs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileTransferListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileUploadListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.ThumbnailListener;
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
		
		 Button deleteDirButton = (Button) findViewById(R.id.deleteDir);
	        deleteDirButton.setOnClickListener(new View.OnClickListener(){

	            @Override
	            public void onClick(View view) {
	                deleteDir();
	            }
	        });
        Button uploadFileButton = (Button) findViewById(R.id.uploadPersonalFile);
		uploadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadFile();
			}

		});    
		
		Button stopUploadFileButton = (Button) findViewById(R.id.stopUploadPersonalFile);
		stopUploadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopUploadFile();
			}

		});

		Button downloadFileButton = (Button) findViewById(R.id.downloadPersonalFile);
		downloadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadFile();
			}

		});

		Button downloadStreamFileButton = (Button) findViewById(R.id.downloadPersonalStreamFile);
		downloadStreamFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadStreamFile();
			}

		});

		Button stopDownloadFileButton = (Button) findViewById(R.id.stopDownloadPersonalFile);
		stopDownloadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopDownloadFile();
			}

		});

		Button deleteFileButton = (Button) findViewById(R.id.deletePersonalFile);
		deleteFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteFile();
			}

		});
		
		Button imageListButton = (Button) findViewById(R.id.personalImageList);
		imageListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				imageList();
			}

		});

		Button videoListButton = (Button) findViewById(R.id.personalVideoList);
		videoListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				videoList();
			}

		});

		Button audioListButton = (Button) findViewById(R.id.personalAudioList);
		audioListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				audioList();
			}

		});

		Button docListButton = (Button) findViewById(R.id.personalDocList);
		docListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				docList();
			}

		});
		
		Button thumbnailButton = (Button) findViewById(R.id.personalThumbnail);
		thumbnailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				thumbnail();
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
	//ɾ��Ŀ¼
	private void deleteDir(){
        mCloudStorage.deleteFile(PcsConf.PERSON_STORAGE_DIR_NAME, new FileOperationListener() {
            @Override
            public void onSuccess(String s) {
                mResultTextView.setText(PcsConf.PERSON_STORAGE_DIR_NAME + " deleted");
            }

            @Override
            public void onFailure(String s, int errCode, String errMsg) {
                mResultTextView.setText("errCode:" + errCode
                        + ", errMsg:" + errMsg);
            }
        });
    }
	//�ϴ��ļ�
	protected void uploadFile() {

        mCloudStorage.uploadFile(PcsConf.LOCAL_FILE_NAME,
				PcsConf.PERSON_STORAGE_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " upload......:"
									+ bytes * 100 / total + "%");
						}

					}

				}, new FileUploadListener() {

					@Override
					public void onSuccess(String source,
							FileInfoResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append(source)
								.append(':')
								.append(result.getPath())
								.append('\n')
								.append("size: ")
								.append(result.getSize())
								.append('\n')
								.append("modified time: ")
								.append(new Date(result.getModifyTime()*1000));
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}

					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
    }
	
	
	protected void stopUploadFile() {
		mCloudStorage.stopTransferring(PcsConf.LOCAL_FILE_NAME,
				PcsConf.PERSON_STORAGE_FILE_NAME);
	}

	protected void downloadFile() {
		mCloudStorage.downloadFile(PcsConf.PERSON_STORAGE_FILE_NAME,
				PcsConf.LOCAL_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " download......:"
									+ bytes * 100 / total + "%");
						}
					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " downloaded as "
									+ newTargetName + " in the local.");
						}
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}
					}

				});
	}

	protected void downloadStreamFile() {
		mCloudStorage.downloadFileFromStream(
				PcsConf.PERSON_STORAGE_FILE_NAME, PcsConf.LOCAL_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " download......:"
									+ bytes * 100 / total + "%");
						}

					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " downloaded as "
									+ newTargetName + " in the local.");
						}

					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
	}

	protected void stopDownloadFile() {
		mCloudStorage.stopTransferring(PcsConf.PERSON_STORAGE_FILE_NAME,
				PcsConf.LOCAL_FILE_NAME);
	}
	
	
	protected void deleteFile() {
		mCloudStorage.deleteFile(PcsConf.PERSON_STORAGE_FILE_NAME,
				new FileOperationListener() {

					@Override
					public void onSuccess(String source) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " is deleted");
						}
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
	}
	
	protected void imageList() {
		mCloudStorage.imageStream(
				new FileListListener() {

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
									.append(new Date(info.getModifyTime())
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

	protected void videoList() {
		mCloudStorage.videoStream(
				new FileListListener() {

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
									.append(new Date(info.getModifyTime())
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

	protected void audioList() {
		mCloudStorage.audioStream(
				new FileListListener() {

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
									.append(new Date(info.getModifyTime())
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

	protected void docList() {
		mCloudStorage.docStream(
				new FileListListener() {

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
									.append(new Date(info.getModifyTime())
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
	
	protected void thumbnail() {
		mCloudStorage.thumbnail(PcsConf.PERSON_STORAGE_FILE_NAME, 10,
				10, 10,
				new ThumbnailListener() {

					@Override
					public void onSuccess(Bitmap bitmap) {
						if (null != mResultTextView) {
							mResultTextView.setText(bitmap.toString());
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

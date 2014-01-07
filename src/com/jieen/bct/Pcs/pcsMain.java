package com.jieen.bct.Pcs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

	private ListView lv;
	private pcsFileInfo pcsFileItem;
	ArrayList<FileInfoResult> myList;
	private FileInfoResult fir;
	private int pcsFileCount = 0;
	protected String tag = "pcsMain";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Frontia.init(this.getApplicationContext(), PcsConf.APIKEY);
		ArrayList<String> list = new ArrayList<String>();
		list.add("basic");
		list.add("netdisk");
		mCloudStorage = Frontia.getPersonalStorage();	//获取PCS实例
		authorization = Frontia.getAuthorization();		//获取授权实例
		authorization.authorize(this,MediaType.BAIDU.toString(),list,new AuthorizationListener() {
			//授权成功
			public void onSuccess(FrontiaUser arg0) {
				Frontia.setCurrentAccount(arg0);
				setupViews();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				TextView view = new TextView(pcsMain.this);
				view.setText("登录百度账号失败，错误为:"+arg0+""+arg1+"，只有登录百度账号才能使用个人文件存储功能，请返回重新尝试登录");
				setContentView(view);
			}
			
			@Override
			public void onCancel() {
				TextView view = new TextView(pcsMain.this);
				view.setText("只有登录百度账号才能使用个人文件存储功能，请返回重新尝试登录");
				setContentView(view);
			}
		});
	}
	protected void setupViews() {
		setContentView(R.layout.pcsmain);
		lv = (ListView) findViewById(R.id.lvFileItems);
		getPcsFileInfo();
	}
	private class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return myList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FileInfoResult fir = myList.get(position);
			View view = View.inflate(pcsMain.this, R.layout.filelistitem, null);
			TextView tvfilename = (TextView) view.findViewById(R.id.tv_FileName);
			TextView tvmodtime = (TextView) view.findViewById(R.id.tv_ModifyTime);
			tvfilename.setText(fir.getPath());
			tvmodtime.setText(new Date(fir.getModifyTime()*1000).toString());
			return view;
		}
		
	}
	
	private void setupViews_test() {
		setContentView(R.layout.pcsmain_test);

		Button createDirButton = (Button) findViewById(R.id.createDir);
		createDirButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				createDir();		//创建目录
			}
		});
		
		Button listButton = (Button) findViewById(R.id.personalList);
		listButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				list();				//列出文件
			}

		});
		
		Button quotaButton = (Button) findViewById(R.id.personalQuota);
		quotaButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				quota();		//查询空间
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
	//创建目录实现方法
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
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
					}
				});
	}
	
	//列出文件方法
		protected void getPcsFileInfo() {
			mCloudStorage.list(PcsConf.PERSON_STORAGE_DIR_NAME, null,
					null, new FileListListener() {
				
				public void onSuccess(List<FileInfoResult> result) {
							pcsFileCount = result.size();
							myList = new ArrayList<FileInfoResult>();
							for (FileInfoResult info : result) {
								Log.i(tag, "info: "+info.getPath());
								 myList.add(info);
							}
							Toast.makeText(getApplication(), "Get File List Success", 0).show();
							lv.setAdapter(new MyAdapter());
						}

						@Override
						public void onFailure(int errCode, String errMsg) {
							Toast.makeText(getApplication(), "List File error", 0).show();
						}
					});
		}
	//列出文件方法
	protected void list() {
		mCloudStorage.list(PcsConf.PERSON_STORAGE_DIR_NAME, null,
				null, new FileListListener() {

			public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						pcsFileCount = result.size();
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
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
					}
				});
	}
	//查询使用空间方法
	protected void quota() {

		mCloudStorage.quota(
				new QuotaListener() {

					@Override
					public void onSuccess(QuotaResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append("total: ").append(result.getTotal())
								.append('\n').append("used: ")
								.append(result.getUsed()).toString();
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
					}
				});
	}
	//删除目录
	private void deleteDir(){
        mCloudStorage.deleteFile(PcsConf.PERSON_STORAGE_DIR_NAME, new FileOperationListener() {
            @Override
            public void onSuccess(String s) {
                
            }

            @Override
            public void onFailure(String s, int errCode, String errMsg) {
                
            }
        });
    }
	//上传文件
	protected void uploadFile() {

        mCloudStorage.uploadFile(PcsConf.LOCAL_FILE_NAME,
				PcsConf.PERSON_STORAGE_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						

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
						

					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						

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
						
					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
					}
				}

				);
	}

	protected void downloadStreamFile() {
		mCloudStorage.downloadFileFromStream(
				PcsConf.PERSON_STORAGE_FILE_NAME, PcsConf.LOCAL_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						

					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {

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
						
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						
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
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
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
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
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
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
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
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
					}
				});
	}
	
	protected void thumbnail() {
		mCloudStorage.thumbnail(PcsConf.PERSON_STORAGE_FILE_NAME, 10,
				10, 10,
				new ThumbnailListener() {

					@Override
					public void onSuccess(Bitmap bitmap) {
						
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						
					}
				});
	}
	
}

package com.jieen.bct.Pcs;

public class pcsFileInfo {
	private int filesize;
	private String fileName;
	private String lastModifyTime;
	private int[] iconId;
	
	public int getFilesize() {
		return filesize;
	}
	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public int[] getIconId() {
		return iconId;
	}
	public void setIconId(int[] iconId) {
		this.iconId = iconId;
	}
	
}

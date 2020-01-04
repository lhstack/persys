package com.lhstack.upload.resut;

public class UploadResult {
	private String fileName;

	private String path;

	private long size;

	public UploadResult() {
		super();
	}

	public UploadResult(String fileName, String path, long size) {
		super();
		this.fileName = fileName;
		this.path = path;
		this.size = size;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "UploadResult [fileName=" + fileName + ", path=" + path + ", size=" + size + "]";
	}

}

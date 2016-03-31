package org.iii.ideas.catering_service.rest.bo;

public class FileBO {

	private String uuid="";
	private String downloadPath="";
	private String sourceType="";
	private String targetId="";
	private String mimeType="";
	private String extType="";
	private String filePath="";
	private String encodeFileName="";
	private String OriginalFileName="";
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getEncodeFileName() {
		return encodeFileName;
	}
	public void setEncodeFileName(String encodeFileName) {
		this.encodeFileName = encodeFileName;
	}
	public String getExtType() {
		return extType;
	}
	public void setExtType(String extType) {
		this.extType = extType;
	}
	public String getOriginalFileName() {
		return OriginalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		OriginalFileName = originalFileName;
	}

	
}

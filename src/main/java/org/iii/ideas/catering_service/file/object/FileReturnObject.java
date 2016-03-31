package org.iii.ideas.catering_service.file.object;

public class FileReturnObject {
	private int retStatus = 0;
	private String retMsg = "";
	private int testInt = 0;
	private String fullFileName ="";

	public FileReturnObject() {
		this.testInt = this.testInt + 1;
	}

	public int getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(int retStatus) {
		this.retStatus = retStatus;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public void setTestInt(int i) {
		this.testInt = i;
	}

	public int getTestInt() {
		return this.testInt;
	}
	/** add by Ellis 2014/11/28 **/
	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

}

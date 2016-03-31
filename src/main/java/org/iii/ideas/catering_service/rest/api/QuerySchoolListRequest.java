package org.iii.ideas.catering_service.rest.api;

public class QuerySchoolListRequest {
	private int cid;
	private int page;
	private int perpage;
	private String queryschoolname;
	private String schoolId;
	private String queryMode;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}

	public String getQueryschoolname() {
		return queryschoolname;
	}

	public void setQueryschoolname(String queryschoolname) {
		this.queryschoolname = queryschoolname;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}
}

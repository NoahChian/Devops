package org.iii.ideas.catering_service.dao;

/**
 * Navi entity. @author MyEclipse Persistence Tools
 */

public class Navi implements java.io.Serializable {

	// Fields

	private Long naviId;
	private String name;
	private String url;
	private Long parents;
	private Integer order;

	// Constructors

	/** default constructor */
	public Navi() {
	}

	/** minimal constructor */
	public Navi(Long naviId) {
		this.naviId = naviId;
	}

	/** full constructor */
	public Navi(Long naviId, String name, String url, Long parents,
			Integer order) {
		this.naviId = naviId;
		this.name = name;
		this.url = url;
		this.parents = parents;
		this.order = order;
	}

	// Property accessors

	public Long getNaviId() {
		return this.naviId;
	}

	public void setNaviId(Long naviId) {
		this.naviId = naviId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getParents() {
		return this.parents;
	}

	public void setParents(Long parents) {
		this.parents = parents;
	}

	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
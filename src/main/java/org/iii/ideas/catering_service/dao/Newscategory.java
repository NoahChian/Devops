package org.iii.ideas.catering_service.dao;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class Newscategory implements java.io.Serializable {
	
	private Integer id;
	private String name;
	private String labelColor;
	
	public static final String ID = "id";	//String
	public static final String NAME = "name";	//String
	public static final String LABEL_COLOR = "labelColor";	//String
	
	/** default constructor */
	public Newscategory() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabelColor() {
		return labelColor;
	}

	public void setLabelColor(String labelColor) {
		this.labelColor = labelColor;
	}
}

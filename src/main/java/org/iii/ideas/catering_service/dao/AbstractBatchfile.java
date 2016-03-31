package org.iii.ideas.catering_service.dao;

/**
 * AbstractBatchfile entity provides the base persistence definition of the
 * Batchfile entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractBatchfile implements java.io.Serializable {

	// Fields

	private Integer batchFileId;
	private Integer ingredientBatchId;
	private String inspectFilePath;

	// Constructors

	/** default constructor */
	public AbstractBatchfile() {
	}

	/** minimal constructor */
	public AbstractBatchfile(Integer batchFileId) {
		this.batchFileId = batchFileId;
	}

	/** full constructor */
	public AbstractBatchfile(Integer batchFileId, Integer ingredientBatchId,
			String inspectFilePath) {
		this.batchFileId = batchFileId;
		this.ingredientBatchId = ingredientBatchId;
		this.inspectFilePath = inspectFilePath;
	}

	// Property accessors

	public Integer getBatchFileId() {
		return this.batchFileId;
	}

	public void setBatchFileId(Integer batchFileId) {
		this.batchFileId = batchFileId;
	}

	public Integer getIngredientBatchId() {
		return this.ingredientBatchId;
	}

	public void setIngredientBatchId(Integer ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}

	public String getInspectFilePath() {
		return this.inspectFilePath;
	}

	public void setInspectFilePath(String inspectFilePath) {
		this.inspectFilePath = inspectFilePath;
	}

}
package org.iii.ideas.catering_service.dao;

/**
 * Batchfile entity. @author MyEclipse Persistence Tools
 */
public class Batchfile extends AbstractBatchfile implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Batchfile() {
	}

	/** minimal constructor */
	public Batchfile(Integer batchFileId) {
		super(batchFileId);
	}

	/** full constructor */
	public Batchfile(Integer batchFileId, Integer ingredientBatchId,
			String inspectFilePath) {
		super(batchFileId, ingredientBatchId, inspectFilePath);
	}

}

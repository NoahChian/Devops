package org.iii.ideas.catering_service.dao;

public class BatchfileObject {
	private Batchfile batchfile;
	private Batchdata batchdata;
	private Ingredientbatchdata ingredinetbatchdata;
	public Batchfile getBatchfile() {
		return batchfile;
	}
	public void setBatchfile(Batchfile batchfile) {
		this.batchfile = batchfile;
	}
	public Batchdata getBatchdata() {
		return batchdata;
	}
	public void setBatchdata(Batchdata batchdata) {
		this.batchdata = batchdata;
	}
	public Ingredientbatchdata getIngredinetbatchdata() {
		return ingredinetbatchdata;
	}
	public void setIngredinetbatchdata(Ingredientbatchdata ingredinetbatchdata) {
		this.ingredinetbatchdata = ingredinetbatchdata;
	}
}

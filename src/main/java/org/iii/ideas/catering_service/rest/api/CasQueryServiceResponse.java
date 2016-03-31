package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.IngredientCertificate;

public class CasQueryServiceResponse extends AbstractApiResponse {
	private IngredientCertificateObject ingredientCertificateObject;

	public IngredientCertificateObject getIngredientCertificateObject() {
		return ingredientCertificateObject;
	}

	public void setIngredientCertificateObject(IngredientCertificateObject ingredientCertificateObject) {
		this.ingredientCertificateObject = ingredientCertificateObject;
	}
}

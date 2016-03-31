package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.ViewKitchenUnionRestaurant;

public class QueryKitchenRestaurantListResponse   extends AbstractApiResponse{
	private List <ViewKitchenUnionRestaurant> vk = new ArrayList<ViewKitchenUnionRestaurant>();

	public List <ViewKitchenUnionRestaurant> getKitchenRestaurantList() {
		return vk;
	}

	public void setKitchenRestaurantList(List <ViewKitchenUnionRestaurant> vk) {
		this.vk = vk;
	}

}


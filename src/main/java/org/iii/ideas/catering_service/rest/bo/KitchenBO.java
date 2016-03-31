package org.iii.ideas.catering_service.rest.bo;

public class KitchenBO {
		private String kitchenName;
		private Integer kitchenId;
		private String companyId;
		private String kitchenType;	
		private Integer supplierQuantity;
		
		public void setKitchenName(String kitchenName){
			this.kitchenName=kitchenName;
		}
		public String getKitchenName(){
			return this.kitchenName;
		}
		
		public void setKitchenId(Integer kitchenId){
			this.kitchenId=kitchenId;
		}
		
		public Integer getKitchenId(){
			return this.kitchenId;
		}
		
		public String getCompanyId(){
			return this.companyId;
		}
		
		public void setCompanyId(String companyId){
			this.companyId=companyId;
			
		}
		
		public String getKitchenType(){
			return this.kitchenType;
		}
		
		public void setKitchenType(String kitchenType){
			this.kitchenType=kitchenType;
			
		}	
		public Integer getSupplierQuantity(){
			return this.supplierQuantity;
		}
		public void setSupplierQuantity(Integer quantity){
			this.supplierQuantity=quantity;
			
		}
}

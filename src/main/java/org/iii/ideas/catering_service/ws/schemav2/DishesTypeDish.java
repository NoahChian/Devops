/**
 * DishesTypeDish.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.iii.ideas.catering_service.ws.schemav2;

public class DishesTypeDish  implements java.io.Serializable {
    private java.lang.String dishType;

    private DishInfo dishInfo;

    private java.math.BigDecimal displayOrder;

    private java.math.BigDecimal dishQuantity;

    public DishesTypeDish() {
    }

    public DishesTypeDish(
           java.lang.String dishType,
           DishInfo dishInfo,
           java.math.BigDecimal displayOrder,
           java.math.BigDecimal dishQuantity) {
           this.dishType = dishType;
           this.dishInfo = dishInfo;
           this.displayOrder = displayOrder;
           this.dishQuantity = dishQuantity;
    }


    /**
     * Gets the dishType value for this DishesTypeDish.
     * 
     * @return dishType
     */
    public java.lang.String getDishType() {
        return dishType;
    }


    /**
     * Sets the dishType value for this DishesTypeDish.
     * 
     * @param dishType
     */
    public void setDishType(java.lang.String dishType) {
        this.dishType = dishType;
    }


    /**
     * Gets the dishInfo value for this DishesTypeDish.
     * 
     * @return dishInfo
     */
    public DishInfo getDishInfo() {
        return dishInfo;
    }


    /**
     * Sets the dishInfo value for this DishesTypeDish.
     * 
     * @param dishInfo
     */
    public void setDishInfo(DishInfo dishInfo) {
        this.dishInfo = dishInfo;
    }


    /**
     * Gets the displayOrder value for this DishesTypeDish.
     * 
     * @return displayOrder
     */
    public java.math.BigDecimal getDisplayOrder() {
        return displayOrder;
    }


    /**
     * Sets the displayOrder value for this DishesTypeDish.
     * 
     * @param displayOrder
     */
    public void setDisplayOrder(java.math.BigDecimal displayOrder) {
        this.displayOrder = displayOrder;
    }


    /**
     * Gets the dishQuantity value for this DishesTypeDish.
     * 
     * @return dishQuantity
     */
    public java.math.BigDecimal getDishQuantity() {
        return dishQuantity;
    }


    /**
     * Sets the dishQuantity value for this DishesTypeDish.
     * 
     * @param dishQuantity
     */
    public void setDishQuantity(java.math.BigDecimal dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DishesTypeDish)) return false;
        DishesTypeDish other = (DishesTypeDish) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dishType==null && other.getDishType()==null) || 
             (this.dishType!=null &&
              this.dishType.equals(other.getDishType()))) &&
            ((this.dishInfo==null && other.getDishInfo()==null) || 
             (this.dishInfo!=null &&
              this.dishInfo.equals(other.getDishInfo()))) &&
            ((this.displayOrder==null && other.getDisplayOrder()==null) || 
             (this.displayOrder!=null &&
              this.displayOrder.equals(other.getDisplayOrder()))) &&
            ((this.dishQuantity==null && other.getDishQuantity()==null) || 
             (this.dishQuantity!=null &&
              this.dishQuantity.equals(other.getDishQuantity())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDishType() != null) {
            _hashCode += getDishType().hashCode();
        }
        if (getDishInfo() != null) {
            _hashCode += getDishInfo().hashCode();
        }
        if (getDisplayOrder() != null) {
            _hashCode += getDisplayOrder().hashCode();
        }
        if (getDishQuantity() != null) {
            _hashCode += getDishQuantity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DishesTypeDish.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", ">DishesType>Dish"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dishType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "DishType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dishInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "DishInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "DishInfo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "DisplayOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dishQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "DishQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

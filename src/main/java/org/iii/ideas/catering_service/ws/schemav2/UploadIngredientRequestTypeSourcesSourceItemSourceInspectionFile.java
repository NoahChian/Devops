/**
 * UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.iii.ideas.catering_service.ws.schemav2;

public class UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile  implements java.io.Serializable {
    private java.lang.String sourceInspectionFileName;

    private java.lang.String sourceIngredientsInspectionLab;

    private java.lang.String sourceIngredientsInspectionStatus;

    public UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile() {
    }

    public UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile(
           java.lang.String sourceInspectionFileName,
           java.lang.String sourceIngredientsInspectionLab,
           java.lang.String sourceIngredientsInspectionStatus) {
           this.sourceInspectionFileName = sourceInspectionFileName;
           this.sourceIngredientsInspectionLab = sourceIngredientsInspectionLab;
           this.sourceIngredientsInspectionStatus = sourceIngredientsInspectionStatus;
    }


    /**
     * Gets the sourceInspectionFileName value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @return sourceInspectionFileName
     */
    public java.lang.String getSourceInspectionFileName() {
        return sourceInspectionFileName;
    }


    /**
     * Sets the sourceInspectionFileName value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @param sourceInspectionFileName
     */
    public void setSourceInspectionFileName(java.lang.String sourceInspectionFileName) {
        this.sourceInspectionFileName = sourceInspectionFileName;
    }


    /**
     * Gets the sourceIngredientsInspectionLab value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @return sourceIngredientsInspectionLab
     */
    public java.lang.String getSourceIngredientsInspectionLab() {
        return sourceIngredientsInspectionLab;
    }


    /**
     * Sets the sourceIngredientsInspectionLab value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @param sourceIngredientsInspectionLab
     */
    public void setSourceIngredientsInspectionLab(java.lang.String sourceIngredientsInspectionLab) {
        this.sourceIngredientsInspectionLab = sourceIngredientsInspectionLab;
    }


    /**
     * Gets the sourceIngredientsInspectionStatus value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @return sourceIngredientsInspectionStatus
     */
    public java.lang.String getSourceIngredientsInspectionStatus() {
        return sourceIngredientsInspectionStatus;
    }


    /**
     * Sets the sourceIngredientsInspectionStatus value for this UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.
     * 
     * @param sourceIngredientsInspectionStatus
     */
    public void setSourceIngredientsInspectionStatus(java.lang.String sourceIngredientsInspectionStatus) {
        this.sourceIngredientsInspectionStatus = sourceIngredientsInspectionStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile)) return false;
        UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile other = (UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sourceInspectionFileName==null && other.getSourceInspectionFileName()==null) || 
             (this.sourceInspectionFileName!=null &&
              this.sourceInspectionFileName.equals(other.getSourceInspectionFileName()))) &&
            ((this.sourceIngredientsInspectionLab==null && other.getSourceIngredientsInspectionLab()==null) || 
             (this.sourceIngredientsInspectionLab!=null &&
              this.sourceIngredientsInspectionLab.equals(other.getSourceIngredientsInspectionLab()))) &&
            ((this.sourceIngredientsInspectionStatus==null && other.getSourceIngredientsInspectionStatus()==null) || 
             (this.sourceIngredientsInspectionStatus!=null &&
              this.sourceIngredientsInspectionStatus.equals(other.getSourceIngredientsInspectionStatus())));
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
        if (getSourceInspectionFileName() != null) {
            _hashCode += getSourceInspectionFileName().hashCode();
        }
        if (getSourceIngredientsInspectionLab() != null) {
            _hashCode += getSourceIngredientsInspectionLab().hashCode();
        }
        if (getSourceIngredientsInspectionStatus() != null) {
            _hashCode += getSourceIngredientsInspectionStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadIngredientRequestTypeSourcesSourceItemSourceInspectionFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", ">>>UploadIngredientRequestType>Sources>SourceItem>SourceInspectionFile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceInspectionFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "SourceInspectionFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceIngredientsInspectionLab");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "SourceIngredientsInspectionLab"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceIngredientsInspectionStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://twfoodtrace.org.tw/FcloudDataschemas", "SourceIngredientsInspectionStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

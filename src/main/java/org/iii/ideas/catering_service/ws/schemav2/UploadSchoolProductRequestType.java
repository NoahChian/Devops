//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.06 at 10:52:20 AM CST 
//


package org.iii.ideas.catering_service.ws.schemav2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UploadSchoolProductRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadSchoolProductRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UploaderAccount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SendTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="SchoolId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProductCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OnShelfDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OffShelfDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadSchoolProductRequestType", propOrder = {
    "action",
    "messageId",
    "uploaderAccount",
    "token",
    "sendTime",
    "schoolId",
    "productCode",
    "onShelfDate",
    "offShelfDate"
})
public class UploadSchoolProductRequestType {

    @XmlElement(name = "Action", required = true)
    protected String action;
    @XmlElement(name = "MessageId", required = true)
    protected String messageId;
    @XmlElement(name = "UploaderAccount", required = true)
    protected String uploaderAccount;
    @XmlElement(name = "Token", required = true)
    protected String token;
    @XmlElement(name = "SendTime")
    protected long sendTime;
    @XmlElement(name = "SchoolId", required = true)
    protected String schoolId;
    @XmlElement(name = "ProductCode", required = true)
    protected String productCode;
    @XmlElement(name = "OnShelfDate", required = true)
    protected String onShelfDate;
    @XmlElement(name = "OffShelfDate", required = true)
    protected String offShelfDate;

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the uploaderAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploaderAccount() {
        return uploaderAccount;
    }

    /**
     * Sets the value of the uploaderAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploaderAccount(String value) {
        this.uploaderAccount = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the sendTime property.
     * 
     */
    public long getSendTime() {
        return sendTime;
    }

    /**
     * Sets the value of the sendTime property.
     * 
     */
    public void setSendTime(long value) {
        this.sendTime = value;
    }

    /**
     * Gets the value of the schoolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * Sets the value of the schoolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchoolId(String value) {
        this.schoolId = value;
    }

    /**
     * Gets the value of the productCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the onShelfDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnShelfDate() {
        return onShelfDate;
    }

    /**
     * Sets the value of the onShelfDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnShelfDate(String value) {
        this.onShelfDate = value;
    }

    /**
     * Gets the value of the offShelfDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffShelfDate() {
        return offShelfDate;
    }

    /**
     * Sets the value of the offShelfDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffShelfDate(String value) {
        this.offShelfDate = value;
    }

}

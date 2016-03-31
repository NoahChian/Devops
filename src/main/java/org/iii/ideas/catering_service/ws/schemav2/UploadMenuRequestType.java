//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.19 at 03:36:55 PM CST 
//


package org.iii.ideas.catering_service.ws.schemav2;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UploadMenuRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadMenuRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UploaderAccount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SendTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CompanyId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ServiceDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SchoolId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Staple1" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo"/>
 *         &lt;element name="Staple2" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="MainCourse1" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo"/>
 *         &lt;element name="MainCourse2" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="MainCourse3" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="MainCourse4" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish1" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish2" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish3" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish4" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish5" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="SideDish6" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="Vegetable" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="Soup" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="Extra1" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="Extra2" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishInfo" minOccurs="0"/>
 *         &lt;element name="DishList" type="{http://twfoodtrace.org.tw/FcloudDataschemas}DishesType" minOccurs="0"/>
 *         &lt;element name="Grains" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="MeatBeans" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Vegetables" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Oils" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Fruits" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Milk" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Calories" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadMenuRequestType", propOrder = {
    "action",
    "messageId",
    "uploaderAccount",
    "token",
    "sendTime",
    "companyId",
    "serviceDate",
    "menuType",
    "schoolId",
    "staple1",
    "staple2",
    "mainCourse1",
    "mainCourse2",
    "mainCourse3",
    "mainCourse4",
    "sideDish1",
    "sideDish2",
    "sideDish3",
    "sideDish4",
    "sideDish5",
    "sideDish6",
    "vegetable",
    "soup",
    "extra1",
    "extra2",
    "dishList",
    "grains",
    "meatBeans",
    "vegetables",
    "oils",
    "fruits",
    "milk",
    "calories"
})
@XmlRootElement(name="UploadMenuRequestType")
public class UploadMenuRequestType {

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
    @XmlElement(name = "CompanyId", required = true)
    protected String companyId;
    @XmlElement(name = "ServiceDate", required = true)
    protected String serviceDate;
    @XmlElement(name = "MenuType", required = true)
    protected String menuType;
    @XmlElement(name = "SchoolId", required = true)
    protected String schoolId;
    @XmlElement(name = "Staple1", required = true)
    protected DishInfo staple1;
    @XmlElement(name = "Staple2")
    protected DishInfo staple2;
    @XmlElement(name = "MainCourse1", required = true)
    protected DishInfo mainCourse1;
    @XmlElement(name = "MainCourse2")
    protected DishInfo mainCourse2;
    @XmlElement(name = "MainCourse3")
    protected DishInfo mainCourse3;
    @XmlElement(name = "MainCourse4")
    protected DishInfo mainCourse4;
    @XmlElement(name = "SideDish1")
    protected DishInfo sideDish1;
    @XmlElement(name = "SideDish2")
    protected DishInfo sideDish2;
    @XmlElement(name = "SideDish3")
    protected DishInfo sideDish3;
    @XmlElement(name = "SideDish4")
    protected DishInfo sideDish4;
    @XmlElement(name = "SideDish5")
    protected DishInfo sideDish5;
    @XmlElement(name = "SideDish6")
    protected DishInfo sideDish6;
    @XmlElement(name = "Vegetable")
    protected DishInfo vegetable;
    @XmlElement(name = "Soup")
    protected DishInfo soup;
    @XmlElement(name = "Extra1")
    protected DishInfo extra1;
    @XmlElement(name = "Extra2")
    protected DishInfo extra2;
    @XmlElement(name = "DishList")
    protected DishesType dishList;
    @XmlElement(name = "Grains", required = true)
    protected BigDecimal grains;
    @XmlElement(name = "MeatBeans", required = true)
    protected BigDecimal meatBeans;
    @XmlElement(name = "Vegetables", required = true)
    protected BigDecimal vegetables;
    @XmlElement(name = "Oils", required = true)
    protected BigDecimal oils;
    @XmlElement(name = "Fruits", required = true)
    protected BigDecimal fruits;
    @XmlElement(name = "Milk", required = true)
    protected BigDecimal milk;
    @XmlElement(name = "Calories", required = true)
    protected BigDecimal calories;

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
     * Gets the value of the companyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Sets the value of the companyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyId(String value) {
        this.companyId = value;
    }

    /**
     * Gets the value of the serviceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the value of the serviceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDate(String value) {
        this.serviceDate = value;
    }

    public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
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
     * Gets the value of the staple1 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getStaple1() {
        return staple1;
    }

    /**
     * Sets the value of the staple1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setStaple1(DishInfo value) {
        this.staple1 = value;
    }

    /**
     * Gets the value of the staple2 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getStaple2() {
        return staple2;
    }

    /**
     * Sets the value of the staple2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setStaple2(DishInfo value) {
        this.staple2 = value;
    }

    /**
     * Gets the value of the mainCourse1 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getMainCourse1() {
        return mainCourse1;
    }

    /**
     * Sets the value of the mainCourse1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setMainCourse1(DishInfo value) {
        this.mainCourse1 = value;
    }

    /**
     * Gets the value of the mainCourse2 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getMainCourse2() {
        return mainCourse2;
    }

    /**
     * Sets the value of the mainCourse2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setMainCourse2(DishInfo value) {
        this.mainCourse2 = value;
    }

    /**
     * Gets the value of the mainCourse3 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getMainCourse3() {
        return mainCourse3;
    }

    /**
     * Sets the value of the mainCourse3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setMainCourse3(DishInfo value) {
        this.mainCourse3 = value;
    }

    /**
     * Gets the value of the mainCourse4 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getMainCourse4() {
        return mainCourse4;
    }

    /**
     * Sets the value of the mainCourse4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setMainCourse4(DishInfo value) {
        this.mainCourse4 = value;
    }

    /**
     * Gets the value of the sideDish1 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish1() {
        return sideDish1;
    }

    /**
     * Sets the value of the sideDish1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish1(DishInfo value) {
        this.sideDish1 = value;
    }

    /**
     * Gets the value of the sideDish2 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish2() {
        return sideDish2;
    }

    /**
     * Sets the value of the sideDish2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish2(DishInfo value) {
        this.sideDish2 = value;
    }

    /**
     * Gets the value of the sideDish3 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish3() {
        return sideDish3;
    }

    /**
     * Sets the value of the sideDish3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish3(DishInfo value) {
        this.sideDish3 = value;
    }

    /**
     * Gets the value of the sideDish4 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish4() {
        return sideDish4;
    }

    /**
     * Sets the value of the sideDish4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish4(DishInfo value) {
        this.sideDish4 = value;
    }

    /**
     * Gets the value of the sideDish5 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish5() {
        return sideDish5;
    }

    /**
     * Sets the value of the sideDish5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish5(DishInfo value) {
        this.sideDish5 = value;
    }

    /**
     * Gets the value of the sideDish6 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSideDish6() {
        return sideDish6;
    }

    /**
     * Sets the value of the sideDish6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSideDish6(DishInfo value) {
        this.sideDish6 = value;
    }

    /**
     * Gets the value of the vegetable property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getVegetable() {
        return vegetable;
    }

    /**
     * Sets the value of the vegetable property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setVegetable(DishInfo value) {
        this.vegetable = value;
    }

    /**
     * Gets the value of the soup property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getSoup() {
        return soup;
    }

    /**
     * Sets the value of the soup property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setSoup(DishInfo value) {
        this.soup = value;
    }

    /**
     * Gets the value of the extra1 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getExtra1() {
        return extra1;
    }

    /**
     * Sets the value of the extra1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setExtra1(DishInfo value) {
        this.extra1 = value;
    }

    /**
     * Gets the value of the extra2 property.
     * 
     * @return
     *     possible object is
     *     {@link DishInfo }
     *     
     */
    public DishInfo getExtra2() {
        return extra2;
    }

    /**
     * Sets the value of the extra2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishInfo }
     *     
     */
    public void setExtra2(DishInfo value) {
        this.extra2 = value;
    }

    /**
     * Gets the value of the dishList property.
     * 
     * @return
     *     possible object is
     *     {@link DishesType }
     *     
     */
    public DishesType getDishList() {
        return dishList;
    }

    /**
     * Sets the value of the dishList property.
     * 
     * @param value
     *     allowed object is
     *     {@link DishesType }
     *     
     */
    public void setDishList(DishesType value) {
        this.dishList = value;
    }

    /**
     * Gets the value of the grains property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGrains() {
        return grains;
    }

    /**
     * Sets the value of the grains property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGrains(BigDecimal value) {
        this.grains = value;
    }

    /**
     * Gets the value of the meatBeans property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMeatBeans() {
        return meatBeans;
    }

    /**
     * Sets the value of the meatBeans property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMeatBeans(BigDecimal value) {
        this.meatBeans = value;
    }

    /**
     * Gets the value of the vegetables property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVegetables() {
        return vegetables;
    }

    /**
     * Sets the value of the vegetables property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVegetables(BigDecimal value) {
        this.vegetables = value;
    }

    /**
     * Gets the value of the oils property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOils() {
        return oils;
    }

    /**
     * Sets the value of the oils property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOils(BigDecimal value) {
        this.oils = value;
    }

    /**
     * Gets the value of the fruits property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFruits() {
        return fruits;
    }

    /**
     * Sets the value of the fruits property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFruits(BigDecimal value) {
        this.fruits = value;
    }

    /**
     * Gets the value of the milk property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMilk() {
        return milk;
    }

    /**
     * Sets the value of the milk property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMilk(BigDecimal value) {
        this.milk = value;
    }

    /**
     * Gets the value of the calories property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCalories() {
        return calories;
    }

    /**
     * Sets the value of the calories property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCalories(BigDecimal value) {
        this.calories = value;
    }

}
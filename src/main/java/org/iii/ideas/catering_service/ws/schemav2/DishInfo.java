//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.19 at 03:36:55 PM CST 
//


package org.iii.ideas.catering_service.ws.schemav2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for DishInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DishInfo">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="imagefilename" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="showname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DishInfo", propOrder = {
    "value"
})
public class DishInfo {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "imagefilename")
    protected String imagefilename;
    @XmlAttribute(name = "showname")
    protected String showname;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the imagefilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagefilename() {
        return imagefilename;
    }

    /**
     * Sets the value of the imagefilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagefilename(String value) {
        this.imagefilename = value;
    }

    /**
     * Gets the value of the showname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowname() {
        return showname;
    }

    /**
     * Sets the value of the showname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowname(String value) {
        this.showname = value;
    }

}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 04:36:39 PM CET 
//


package jrc.it.annotation.reader.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of ASAR calibration pulse records.
 * 
 * <p>Java class for asarCalPulseListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="asarCalPulseListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="asarCalPulse" type="{}asarCalPulseType" maxOccurs="32"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asarCalPulseListType", propOrder = {
    "asarCalPulse"
})
public class AsarCalPulseListType {

    @XmlElement(required = true)
    protected List<AsarCalPulseType> asarCalPulse;
    @XmlAttribute(name = "count", required = true)
    protected long count;

    /**
     * Gets the value of the asarCalPulse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asarCalPulse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsarCalPulse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AsarCalPulseType }
     * 
     * 
     */
    public List<AsarCalPulseType> getAsarCalPulse() {
        if (asarCalPulse == null) {
            asarCalPulse = new ArrayList<AsarCalPulseType>();
        }
        return this.asarCalPulse;
    }

    /**
     * Gets the value of the count property.
     * 
     */
    public long getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(long value) {
        this.count = value;
    }

}

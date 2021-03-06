//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 04:36:39 PM CET 
//


package jrc.it.annotation.reader.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Annotation record for Sentinel-1 level 1 antenna pattern product annotations.
 * 
 * <p>Java class for l1AntennaPatternType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="l1AntennaPatternType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="antennaPatternList" type="{}antennaPatternListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "l1AntennaPatternType", propOrder = {
    "antennaPatternList"
})
public class L1AntennaPatternType {

    @XmlElement(required = true)
    protected AntennaPatternListType antennaPatternList;

    /**
     * Gets the value of the antennaPatternList property.
     * 
     * @return
     *     possible object is
     *     {@link AntennaPatternListType }
     *     
     */
    public AntennaPatternListType getAntennaPatternList() {
        return antennaPatternList;
    }

    /**
     * Sets the value of the antennaPatternList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AntennaPatternListType }
     *     
     */
    public void setAntennaPatternList(AntennaPatternListType value) {
        this.antennaPatternList = value;
    }

}

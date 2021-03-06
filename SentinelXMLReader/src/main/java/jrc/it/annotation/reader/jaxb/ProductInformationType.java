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
 * Annotation record for general product information.
 * 
 * <p>Java class for productInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="productInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pass" type="{}passDirectionType"/>
 *         &lt;element name="timelinessCategory" type="{}string"/>
 *         &lt;element name="platformHeading" type="{}double"/>
 *         &lt;element name="projection" type="{}projectionType"/>
 *         &lt;element name="rangeSamplingRate" type="{}double"/>
 *         &lt;element name="radarFrequency" type="{}double"/>
 *         &lt;element name="azimuthSteeringRate" type="{}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productInformationType", propOrder = {
    "pass",
    "timelinessCategory",
    "platformHeading",
    "projection",
    "rangeSamplingRate",
    "radarFrequency",
    "azimuthSteeringRate"
})
public class ProductInformationType {

    @XmlElement(required = true)
    protected String pass;
    @XmlElement(required = true)
    protected String timelinessCategory;
    @XmlElement(required = true)
    protected Double platformHeading;
    @XmlElement(required = true)
    protected ProjectionType projection;
    @XmlElement(required = true)
    protected Double rangeSamplingRate;
    @XmlElement(required = true)
    protected Double radarFrequency;
    @XmlElement(required = true)
    protected Double azimuthSteeringRate;

    /**
     * Gets the value of the pass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPass() {
        return pass;
    }

    /**
     * Sets the value of the pass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPass(String value) {
        this.pass = value;
    }

    /**
     * Gets the value of the timelinessCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimelinessCategory() {
        return timelinessCategory;
    }

    /**
     * Sets the value of the timelinessCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimelinessCategory(String value) {
        this.timelinessCategory = value;
    }

    /**
     * Gets the value of the platformHeading property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPlatformHeading() {
        return platformHeading;
    }

    /**
     * Sets the value of the platformHeading property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPlatformHeading(Double value) {
        this.platformHeading = value;
    }

    /**
     * Gets the value of the projection property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectionType }
     *     
     */
    public ProjectionType getProjection() {
        return projection;
    }

    /**
     * Sets the value of the projection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectionType }
     *     
     */
    public void setProjection(ProjectionType value) {
        this.projection = value;
    }

    /**
     * Gets the value of the rangeSamplingRate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRangeSamplingRate() {
        return rangeSamplingRate;
    }

    /**
     * Sets the value of the rangeSamplingRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRangeSamplingRate(Double value) {
        this.rangeSamplingRate = value;
    }

    /**
     * Gets the value of the radarFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRadarFrequency() {
        return radarFrequency;
    }

    /**
     * Sets the value of the radarFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRadarFrequency(Double value) {
        this.radarFrequency = value;
    }

    /**
     * Gets the value of the azimuthSteeringRate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAzimuthSteeringRate() {
        return azimuthSteeringRate;
    }

    /**
     * Sets the value of the azimuthSteeringRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAzimuthSteeringRate(Double value) {
        this.azimuthSteeringRate = value;
    }

}

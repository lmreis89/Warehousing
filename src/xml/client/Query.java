//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.06 at 07:08:07 PM WEST 
//


package xml.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cube">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="cubeID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="selectedFacts" type="{}selectedFacts"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="dimensions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="dimension" type="{}dimension"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cube",
    "dimensions"
})
@XmlRootElement(name = "query")
public class Query {

    @XmlElement(required = true)
    protected Query.Cube cube;
    @XmlElement(required = true)
    protected Query.Dimensions dimensions;

    /**
     * Gets the value of the cube property.
     * 
     * @return
     *     possible object is
     *     {@link Query.Cube }
     *     
     */
    public Query.Cube getCube() {
        return cube;
    }

    /**
     * Sets the value of the cube property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query.Cube }
     *     
     */
    public void setCube(Query.Cube value) {
        this.cube = value;
    }

    /**
     * Gets the value of the dimensions property.
     * 
     * @return
     *     possible object is
     *     {@link Query.Dimensions }
     *     
     */
    public Query.Dimensions getDimensions() {
        return dimensions;
    }

    /**
     * Sets the value of the dimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query.Dimensions }
     *     
     */
    public void setDimensions(Query.Dimensions value) {
        this.dimensions = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="cubeID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="selectedFacts" type="{}selectedFacts"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cubeID",
        "selectedFacts"
    })
    public static class Cube {

        @XmlElement(required = true)
        protected String cubeID;
        @XmlElement(required = true)
        protected SelectedFacts selectedFacts;

        /**
         * Gets the value of the cubeID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCubeID() {
            return cubeID;
        }

        /**
         * Sets the value of the cubeID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCubeID(String value) {
            this.cubeID = value;
        }

        /**
         * Gets the value of the selectedFacts property.
         * 
         * @return
         *     possible object is
         *     {@link SelectedFacts }
         *     
         */
        public SelectedFacts getSelectedFacts() {
            return selectedFacts;
        }

        /**
         * Sets the value of the selectedFacts property.
         * 
         * @param value
         *     allowed object is
         *     {@link SelectedFacts }
         *     
         */
        public void setSelectedFacts(SelectedFacts value) {
            this.selectedFacts = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="dimension" type="{}dimension"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dimension"
    })
    public static class Dimensions {

        protected List<Dimension> dimension;

        /**
         * Gets the value of the dimension property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dimension property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDimension().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Dimension }
         * 
         * 
         */
        public List<Dimension> getDimension() {
            if (dimension == null) {
                dimension = new ArrayList<Dimension>();
            }
            return this.dimension;
        }

    }

}

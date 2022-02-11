
package org.jeesl.model.json.module.ts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="source")
public class JsonSource
    implements Serializable
{
    private final static long serialVersionUID = 1L;
    @JsonProperty("id")
    protected Long id;
   @JsonProperty("code")
    protected String code;
   @JsonProperty("group")
    protected String group;
   @JsonProperty("label")
    protected String label;
   @JsonProperty("visible")
    protected Boolean visible;
   @JsonProperty("image")
    protected String image;
   @JsonProperty("style")
    protected String style;
   @JsonProperty("position")
    protected Integer position;



    /*
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getId() {
        return id;
    }

    /*
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setId(Long value) {
        this.id = value;
    }

    @JsonIgnore public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
    }

    /*
     * Gets the value of the code property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCode() {
        return code;
    }

    /*
     * Sets the value of the code property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCode(String value) {
        this.code = value;
    }

    @JsonIgnore public boolean isSetCode() {
        return (this.code!= null);
    }

    /*
     * Gets the value of the group property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGroup() {
        return group;
    }

    /*
     * Sets the value of the group property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGroup(String value) {
        this.group = value;
    }

    @JsonIgnore public boolean isSetGroup() {
        return (this.group!= null);
    }

    /*
     * Gets the value of the label property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLabel() {
        return label;
    }

    /*
     * Sets the value of the label property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLabel(String value) {
        this.label = value;
    }

    @JsonIgnore public boolean isSetLabel() {
        return (this.label!= null);
    }

    /*
     * Gets the value of the visible property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isVisible() {
        return visible;
    }

    /*
     * Sets the value of the visible property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setVisible(boolean value) {
        this.visible = value;
    }

    @JsonIgnore public boolean isSetVisible() {
        return (this.visible!= null);
    }

    public void unsetVisible() {
        this.visible = null;
    }

    /*
     * Gets the value of the image property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getImage() {
        return image;
    }

    /*
     * Sets the value of the image property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setImage(String value) {
        this.image = value;
    }

    @JsonIgnore public boolean isSetImage() {
        return (this.image!= null);
    }

    /*
     * Gets the value of the style property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStyle() {
        return style;
    }

    /*
     * Sets the value of the style property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStyle(String value) {
        this.style = value;
    }

    @JsonIgnore public boolean isSetStyle() {
        return (this.style!= null);
    }

    /*
     * Gets the value of the position property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPosition() {
        return position;
    }

    /*
     * Sets the value of the position property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPosition(Integer value) {
        this.position = value;
    }

    @JsonIgnore public boolean isSetPosition() {
        return (this.position!= null);
    }

    public void unsetPosition() {
        this.position = null;
    }

}

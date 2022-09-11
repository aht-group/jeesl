
package net.sf.ahtutils.xml.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for importType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="importType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Object"/&gt;
 *     &lt;enumeration value="List"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "importType")
@XmlEnum
public enum ImportType {

    @XmlEnumValue("Object")
    OBJECT("Object"),
    @XmlEnumValue("List")
    LIST("List");
    private final String value;

    ImportType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ImportType fromValue(String v) {
        for (ImportType c: ImportType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

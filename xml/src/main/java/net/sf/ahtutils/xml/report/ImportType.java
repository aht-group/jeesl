
package net.sf.ahtutils.xml.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;



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

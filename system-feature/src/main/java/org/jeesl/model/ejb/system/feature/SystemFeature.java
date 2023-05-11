package org.jeesl.model.ejb.system.feature;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.system.JeeslSystemFeature;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="SystemFeature")
@EjbErNode(name="Feature")
public class SystemFeature implements JeeslSystemFeature
{
	public static final long serialVersionUID=1;

	public enum Code{onlineHelp}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	@NotNull
	private String name;
	@Override public String getName(){return name;}
	@Override public void setName(String name){this.name = name;}

	@Basic @Column(columnDefinition = "text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
			sb.append("[").append(id).append("]");
			sb.append(" ").append(name);
		return sb.toString();
	}
	
	public boolean equals(Object object)
	{
        return (object instanceof SystemFeature) ? id == ((SystemFeature) object).getId() : (object == this);
    }
}
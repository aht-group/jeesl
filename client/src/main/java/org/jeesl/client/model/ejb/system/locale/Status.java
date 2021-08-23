package org.jeesl.client.model.ejb.system.locale;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;

import net.sf.ahtutils.model.qualifier.EjbErNode;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@DiscriminatorValue("generic")
@Table(name = "UtilsStatus", uniqueConstraints=@UniqueConstraint(columnNames = {"type","code"}))
@EjbErNode(name="Status",category="status",subset="status")
public class Status implements JeeslStatus<Lang,Description,Status>,EjbRemoveable,Serializable
{
	private static final long serialVersionUID = 1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	public void setId(long id) {this.id = id;}
	public long getId() {return id;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	protected Map<String, Lang> name;
	public Map<String, Lang> getName(){if(name==null){name = new Hashtable<String, Lang>();}return name;}
	public void setName(Map<String, Lang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	protected Map<String, Description> description;
	public Map<String, Description> getDescription() {if(description==null){description = new Hashtable<String, Description>();}return description;}
	public void setDescription(Map<String, Description> description) {this.description = description;}
		
	protected String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	private String symbol;
	@Override public String getSymbol(){return symbol;}
	@Override public void setSymbol(String symbol){this.symbol = symbol;}

	protected boolean visible;
	public boolean isVisible() {return visible;}
	public void setVisible(boolean visible) {this.visible = visible;}
	
	protected String image;
	public String getImage() {return image;}
	public void setImage(String image) {this.image = image;}
	
	protected String imageAlt;
	@Override public String getImageAlt() {return imageAlt;}
	@Override public void setImageAlt(String imageAlt) {this.imageAlt=imageAlt;}
	
	protected String style;
	public String getStyle() {return style;}
	public void setStyle(String style) {this.style = style;}

	protected int position;
	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(" code="+code);
		return sb.toString();
	}
	
	@ManyToOne
	protected Status parent;
	@SuppressWarnings("unchecked")
	public <P extends EjbWithCode> P getParent() {return (P)parent;}
	public <P extends EjbWithCode> void setParent(P parent) {this.parent=(Status)parent;}
	
	@Override public boolean equals(Object object){return (object instanceof Status) ? id == ((Status) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,59).append(id).toHashCode();}
}
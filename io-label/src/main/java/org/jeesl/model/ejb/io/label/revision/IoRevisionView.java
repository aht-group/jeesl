package org.jeesl.model.ejb.io.label.revision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoRevisionView", uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="View",category="revision",subset="revision")
public class IoRevisionView implements Serializable,EjbRemoveable,EjbPersistable,
								JeeslRevisionView<IoLang,IoDescription,IoRevisionViewMapping>
{
	public static final long serialVersionUID=1;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	protected String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="view")
	@OrderBy("position")
	private List<IoRevisionViewMapping> maps;
	@Override public List<IoRevisionViewMapping> getMaps() {if(maps==null){maps=new ArrayList<IoRevisionViewMapping>();}return maps;}
	@Override public void setMaps(List<IoRevisionViewMapping> maps) {this.maps=maps;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoRevisionViewJtLang",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoRevisionViewJtDescription",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}


	@Override public boolean equals(Object object){return (object instanceof IoRevisionView) ? id == ((IoRevisionView) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
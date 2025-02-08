package org.jeesl.model.ejb.io.label.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.label.er.IoLabelDiagram;
import org.jeesl.model.ejb.io.label.revision.IoRevisionEntityMapping;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoLabelEntity", uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="Entity",category="revision",subset="revision")
public class IoLabelEntity implements JeeslRevisionEntity<IoLang,IoDescription,IoLabelCategory,IoRevisionEntityMapping,IoLabelAttribute,IoLabelDiagram>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return "category";}
	@NotNull @ManyToOne
	private IoLabelCategory category;
	@Override public IoLabelCategory getCategory() {return category;}
	@Override public void setCategory(IoLabelCategory category) {this.category = category;}

	@ManyToOne
	private IoLabelDiagram diagram;
	@Override public IoLabelDiagram getDiagram() {return diagram;}
	@Override public void setDiagram(IoLabelDiagram diagram) {this.diagram = diagram;}

	@NotNull private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private String jscn;
	@Override public String getJscn() {return jscn;}
	@Override public void setJscn(String jscn) {this.jscn = jscn;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	private Boolean timeseries;
	@Override public Boolean getTimeseries() {return timeseries;}
	@Override public void setTimeseries(Boolean timeseries) {this.timeseries = timeseries;}

	private Boolean documentation;
	@Override public Boolean getDocumentation() {return documentation;}
	@Override public void setDocumentation(Boolean documentation) {this.documentation = documentation;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="IoLabelEntityJtLang",joinColumns={@JoinColumn(name="entity_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="IoLabelEntityJtDescription",joinColumns={@JoinColumn(name="entity_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="IoLabelEntityJtAttribute",joinColumns={@JoinColumn(name="entity_id")},inverseJoinColumns={@JoinColumn(name="attribute_id")})
	@OrderBy("position")
	private List<IoLabelAttribute> attributes;
	@Override public List<IoLabelAttribute> getAttributes() {if(Objects.isNull(attributes)) {attributes = new ArrayList<>();}return attributes;}
	@Override public void setAttributes(List<IoLabelAttribute> attributes) {this.attributes = attributes;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
	@OrderBy("position")
	private List<IoRevisionEntityMapping> maps;
	@Override public List<IoRevisionEntityMapping> getMaps() {if(maps==null){maps=new ArrayList<IoRevisionEntityMapping>();}return maps;}
	@Override public void setMaps(List<IoRevisionEntityMapping> maps) {this.maps=maps;}

	@Basic @Column(columnDefinition = "text")
	private String developerInfo;
	@Override public String getDeveloperInfo() {return developerInfo;}
	@Override public void setDeveloperInfo(String developerInfo) {this.developerInfo=developerInfo;}


	@Override public boolean equals(Object object){return (object instanceof IoLabelEntity) ? id == ((IoLabelEntity) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
package org.jeesl.model.ejb.io.label.entity;

import java.util.Map;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.label.er.IoLabelEntityRelation;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoLabelAttribute")
@EjbErNode(name="Attribute",category="revision",subset="revision",level=2)
public class IoLabelAttribute implements JeeslRevisionAttribute<IoLang,IoDescription,IoLabelEntity,IoLabelEntityRelation,IoLabelAttributeType>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@NotNull @ManyToOne
	private IoLabelAttributeType type;
	@Override public IoLabelAttributeType getType() {return type;}
	@Override public void setType(IoLabelAttributeType type) {this.type = type;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoLabelAttributeJtLang",joinColumns={@JoinColumn(name="attribute_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoLabelAttributeJtDescription",joinColumns={@JoinColumn(name="attribute_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private String xpath;
	@Override public String getXpath() {return xpath;}
	@Override public void setXpath(String xpath) {this.xpath = xpath;}

	private boolean showWeb;
	@Override public boolean isShowWeb() {return showWeb;}
	@Override public void setShowWeb(boolean showWeb) {this.showWeb = showWeb;}

	private boolean showPrint;
	@Override public boolean isShowPrint() {return showPrint;}
	@Override public void setShowPrint(boolean showPrint) {this.showPrint = showPrint;}

	private boolean showName;
	@Override public boolean isShowName() {return showName;}
	@Override public void setShowName(boolean showName) {this.showName = showName;}

	private boolean showEnclosure;
	@Override public boolean isShowEnclosure() {return showEnclosure;}
	@Override public void setShowEnclosure(boolean showEnclosure) {this.showEnclosure = showEnclosure;}

	private Boolean ui;
	@Override public Boolean getUi() {return ui;}
	@Override public void setUi(Boolean ui) {this.ui=ui;}

	private Boolean bean;
	@Override public Boolean getBean() {return bean;}
	@Override public void setBean(Boolean bean) {this.bean=bean;}

	private Boolean manualUser;
	@Override public Boolean getManualUser() {return manualUser;}
	@Override public void setManualUser(Boolean manualUser) {this.manualUser = manualUser;}

	private Boolean manualAdmin;
	@Override public Boolean getManualAdmin() {return manualAdmin;}
	@Override public void setManualAdmin(Boolean manualAdmin) {this.manualAdmin = manualAdmin;}

	private Boolean construction;
	@Override public Boolean getConstruction() {return construction;}
	@Override public void setConstruction(Boolean construction) {this.construction = construction;}

	@Basic @Column(columnDefinition="text")
	private String developerInfo;
	@Override public String getDeveloperInfo() {return developerInfo;}
	@Override public void setDeveloperInfo(String developerInfo) {this.developerInfo=developerInfo;}

	@ManyToOne
	private IoLabelEntity entity;
	@Override public IoLabelEntity getEntity() {return entity;}
	@Override public void setEntity(IoLabelEntity entity) {this.entity = entity;}

	@ManyToOne
	private IoLabelEntityRelation relation;
	@Override public IoLabelEntityRelation getRelation() {return relation;}
	@Override public void setRelation(IoLabelEntityRelation relation) {this.relation = relation;}

	
	private Boolean relationOwner;
	@Override public Boolean getRelationOwner() {return relationOwner;}
	@Override public void setRelationOwner(Boolean relationOwner) {this.relationOwner = relationOwner;}

	private Boolean statusTableDoc;
	@Override public Boolean getStatusTableDoc() {return statusTableDoc;}
	@Override public void setStatusTableDoc(Boolean statusTableDoc) {this.statusTableDoc = statusTableDoc;}

	private Boolean docOptionsInline;
	@Override public Boolean getDocOptionsInline() {return docOptionsInline;}
	@Override public void setDocOptionsInline(Boolean docOptionsInline) {this.docOptionsInline = docOptionsInline;}


	@Override public boolean equals(Object object){return (object instanceof IoLabelAttribute) ? id == ((IoLabelAttribute) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
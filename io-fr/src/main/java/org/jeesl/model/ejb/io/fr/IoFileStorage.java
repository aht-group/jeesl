package org.jeesl.model.ejb.io.fr;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoFileStorage",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
public class IoFileStorage implements JeeslFileStorage<IoLang,IoDescription,IoSsiSystem,IoFileStorageType,IoFileStorageEngine>
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

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@NotNull @ManyToOne
	private IoFileStorageEngine engine;
	@Override public IoFileStorageEngine getEngine() {return engine;}
	@Override public void setEngine(IoFileStorageEngine engine) {this.engine = engine;}

	@NotNull @ManyToOne
	private IoFileStorageType type;
	@Override public IoFileStorageType getType() {return type;}
	@Override public void setType(IoFileStorageType type) {this.type = type;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoFileStorageJtLang",joinColumns={@JoinColumn(name="storage_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoFileStorageJtDescription",joinColumns={@JoinColumn(name="storage_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@Basic @Column(columnDefinition="text")
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}

	private Long fileSizeLimit;
	@Override public Long getFileSizeLimit() {return fileSizeLimit;}
	@Override public void setFileSizeLimit(Long fileSizeLimit) {this.fileSizeLimit = fileSizeLimit;}

	private Boolean keepRemoved;
	@Override public Boolean getKeepRemoved() {return keepRemoved;}
	@Override public void setKeepRemoved(Boolean keepRemoved) {this.keepRemoved = keepRemoved;}
	

	@Override public boolean equals(Object object){return (object instanceof IoFileStorage) ? id == ((IoFileStorage) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}
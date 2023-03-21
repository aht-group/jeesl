package org.jeesl.model.ejb.system.job.work;

import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="SystemJobRobot",uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@EjbErNode(name="Robot")
public class SystemJobRobot implements JeeslJobRobot<IoLang,IoDescription>
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	 
	private boolean visible;
	public boolean isVisible() {return visible;}
	public void setVisible(boolean visible) {this.visible = visible;}
	
	protected int position;
	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}
			 
	private Date record;	 
	public Date getRecord() {return record;}
	public void setRecord(Date record) {this.record = record;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemJobRobotJtLang",joinColumns={@JoinColumn(name="robot_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemJobRobotJtDescription",joinColumns={@JoinColumn(name="robot_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobRobot) ? id == ((SystemJobRobot) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}
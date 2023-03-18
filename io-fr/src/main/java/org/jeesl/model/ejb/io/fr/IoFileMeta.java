package org.jeesl.model.ejb.io.fr;

import java.util.Date;
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

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoFileMeta")
public class IoFileMeta implements JeeslFileMeta<IoDescription,IoFileContainer,IoFileType,IoFileStatus>
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
	
	@Override public String resolveParentAttribute() {return JeeslFileMeta.Attributes.container.toString();}
	@NotNull @ManyToOne
	private IoFileContainer container;
	@Override public IoFileContainer getContainer() {return container;}
	@Override public void setContainer(IoFileContainer container) {this.container = container;}

	@NotNull @Basic @Column(name="name",columnDefinition="text")
	private String fileName;
	@Override public String getFileName() {return fileName;}
	@Override public void setFileName(String fileName) {this.fileName = fileName;}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoFileMetaJtDescription",joinColumns={@JoinColumn(name="meta_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}
	
	@NotNull
	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}
	
	private long size;
	@Override public long getSize() {return size;}
	@Override public void setSize(long size) {this.size = size;}
	
	private String md5Hash;
	@Override public String getMd5Hash() {return md5Hash;}
	@Override public void setMd5Hash(String md5Hash) {this.md5Hash = md5Hash;}

	@NotNull @ManyToOne
	private IoFileType type;
	@Override public IoFileType getType() {return type;}
	@Override public void setType(IoFileType type) {this.type = type;}

	private String category;
	@Override public String getCategory() {return category;}
	@Override public void setCategory(String category) {this.category = category;}
	
	@ManyToOne
	private IoFileStatus status;
	@Override public IoFileStatus getStatus() {return status;}
	@Override public void setStatus(IoFileStatus status) {this.status = status;}

	private Date statusCheck;
	@Override public Date getStatusCheck() {return statusCheck;}
	@Override public void setStatusCheck(Date statusCheck) {this.statusCheck = statusCheck;}


	@Override public boolean equals(Object object){return (object instanceof IoFileMeta) ? id == ((IoFileMeta) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" type:");if(ObjectUtils.allNotNull(type,type.getCode())) {sb.append(type.getCode());} else {sb.append("--");}
		sb.append(" code:").append(code);
		return sb.toString();
	}
}
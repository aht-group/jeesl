package org.jeesl.model.ejb.system.job.cache;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.system.job.template.SystemJobTemplate;

@Entity
@Table(name="SystemJobCache",uniqueConstraints={@UniqueConstraint(columnNames={"template_id","code"})})
@EjbErNode(name="Cache")
public class SystemJobCache implements JeeslJobCache<SystemJobTemplate,IoFileContainer>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToOne
	private SystemJobTemplate template;
	@Override public SystemJobTemplate getTemplate() {return template;}
	@Override public void setTemplate(SystemJobTemplate template) {this.template = template;}

	@Basic @Column(columnDefinition="text")
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}

	private Date validUntil;
	@Override public Date getValidUntil() {return validUntil;}
	@Override public void setValidUntil(Date validUntil) {this.validUntil = validUntil;}

	@Basic
	private byte[] data;
    @Override public byte[] getData() {return data;}
    @Override public void setData(byte[] data) {this.data = data;}
   
    private long size;
    @Override public long getSize() {return size;}
    @Override public void setSize(long size) {this.size = size;}


	@Override public boolean equals(Object object){return (object instanceof SystemJobCache) ? id == ((SystemJobCache) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}
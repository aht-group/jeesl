package org.jeesl.model.ejb.io.ssi.data;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;

@Entity
@Table(name="IoSsiData",uniqueConstraints={@UniqueConstraint(columnNames={"context_id","code"}),@UniqueConstraint(columnNames={"context_id","localId"})})
@EjbErNode(name="Data",category="ssi",subset="systemSsi")
//@TypeDef(name = "json",typeClass = JsonType.class)
public class IoSsiData implements JeeslIoSsiData<IoSsiContext,IoSsiStatus,IoSsiError,SystemJobStatus>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoSsiData.Attributes.mapping.toString();}
	@NotNull @ManyToOne
	@JoinColumn(name="context_id")
	private IoSsiContext mapping;
	@Override public IoSsiContext getMapping() {return mapping;}
	@Override public void setMapping(IoSsiContext mapping) {this.mapping = mapping;}

	@NotNull @ManyToOne
	@JoinColumn(name="status_id")
	private IoSsiStatus link;
	public IoSsiStatus getLink() {return link;}
	public void setLink(IoSsiStatus link) {this.link = link;}

	@Basic @Column(columnDefinition="text")
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@Basic @Column(columnDefinition="text")
//	@Type(type = "json") @Column(columnDefinition = "jsonb")
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}

	private Long localId;
	@Override public Long getLocalId() {return localId;}
	@Override public void setLocalId(Long localId) {this.localId = localId;}

	private Long targetId;
	@Override public Long getTargetId() {return targetId;}
	@Override public void setTargetId(Long targetId) {this.targetId = targetId;}

	private Long refA;
	@Override public Long getRefA() {return refA;}
	@Override public void setRefA(Long refA) {this.refA = refA;}

	private Long refB;
	@Override public Long getRefB() {return refB;}
	@Override public void setRefB(Long refB) {this.refB = refB;}

	private Long refC;
	@Override public Long getRefC() {return refC;}
	@Override public void setRefC(Long refC) {this.refC = refC;}

	@Basic @Column(columnDefinition = "text")
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoSsiData_error"))
	private IoSsiError error;
	public IoSsiError getError() {return error;}
	public void setError(IoSsiError error) {this.error = error;}

	private LocalDateTime jsonCreatedAt;
	@Override public LocalDateTime getJsonCreatedAt() {return jsonCreatedAt;}
	@Override public void setJsonCreatedAt(LocalDateTime jsonCreatedAt) {this.jsonCreatedAt = jsonCreatedAt;}

	private LocalDateTime jsonUpdatedAt;
	@Override public LocalDateTime getJsonUpdatedAt() {return jsonUpdatedAt;}
	@Override public void setJsonUpdatedAt(LocalDateTime jsonUpdatedAt) {this.jsonUpdatedAt = jsonUpdatedAt;}

	private Date ejbCreatedAt;
	@Override public Date getEjbCreatedAt() {return ejbCreatedAt;}
	@Override public void setEjbCreatedAt(Date ejbCreatedAt) {this.ejbCreatedAt = ejbCreatedAt;}

	private Date ejbUpdatedAt;
	@Override public Date getEjbUpdatedAt() {return ejbUpdatedAt;}
	@Override public void setEjbUpdatedAt(Date ejbUpdatedAt) {this.ejbUpdatedAt = ejbUpdatedAt;}

	@ManyToOne
	private SystemJobStatus job1;
	@Override public SystemJobStatus getJob1() {return job1;}
	@Override public void setJob1(SystemJobStatus job1) {this.job1 = job1;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiData) ? id == ((IoSsiData) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" code:").append(code);
		return sb.toString();
	}
}
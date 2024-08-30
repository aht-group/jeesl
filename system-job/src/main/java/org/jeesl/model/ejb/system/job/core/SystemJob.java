package org.jeesl.model.ejb.system.job.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.core.JeeslJob;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.job.template.SystemJobTemplate;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="SystemJob")
@EjbErNode(name="Job")
public class SystemJob implements JeeslJob<SystemJobTemplate,SystemJobPriority,SystemJobStatus,SecurityUser>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslJob.Attributes.template.toString();}
	@ManyToOne
	private SystemJobTemplate template;
	@Override public SystemJobTemplate getTemplate() {return template;}
	@Override public void setTemplate(SystemJobTemplate template) {this.template = template;}

	@Basic @Column(columnDefinition="text")
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@ManyToOne @NotNull
	private SystemJobStatus status;
	@Override public SystemJobStatus getStatus() {return status;}
	@Override public void setStatus(SystemJobStatus status) {this.status = status;}

	@ManyToOne @NotNull
	private SystemJobPriority priority;
	@Override public SystemJobPriority getPriority() {return priority;}
	@Override public void setPriority(SystemJobPriority priority) {this.priority = priority;}

	@NotNull
	private Date recordCreation;
	@Override public Date getRecordCreation() {return recordCreation;}
	@Override public void setRecordCreation(Date recordCreation) {this.recordCreation = recordCreation;}

	private Date recordStart;
	@Override public Date getRecordStart() {return recordStart;}
	@Override public void setRecordStart(Date recordStart) {this.recordStart = recordStart;}

	private Date recordComplete;
	@Override public Date getRecordComplete() {return recordComplete;}
	@Override public void setRecordComplete(Date recordComplete) {this.recordComplete = recordComplete;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name=name;}

	@ManyToOne
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}

	private Integer attempts;
	@Override public Integer getAttempts() {return attempts;}
	@Override public void setAttempts(Integer attempts) {this.attempts = attempts;}
	
	private Integer processingCounter;
	@Override public Integer getProcessingCounter() {return processingCounter;}
	@Override public void setProcessingCounter(Integer processingCounter) {this.processingCounter = processingCounter;}

//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="job")
//	private List<SystemJobFeedback> feedbacks;
//	@Override public List<SystemJobFeedback> getFeedbacks() {if(feedbacks==null){feedbacks=new ArrayList<>();};return feedbacks;}
//	@Override public void setFeedbacks(List<SystemJobFeedback> feedbacks) {this.feedbacks = feedbacks;}
	
	@Basic @Column(columnDefinition="text")
	private String jsonFilter;
	@Override public String getJsonFilter() {return jsonFilter;}
	@Override public void setJsonFilter(String jsonFilter) {this.jsonFilter = jsonFilter;}


	@Override public boolean equals(Object object){return (object instanceof SystemJob) ? id == ((SystemJob) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
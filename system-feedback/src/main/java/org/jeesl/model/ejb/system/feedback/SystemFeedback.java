package org.jeesl.model.ejb.system.feedback;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="SystemFeedback")
@EjbErNode(name="Feedback")
public class SystemFeedback implements JeeslJobFeedback<SystemFeedbackType,SecurityUser>
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@ManyToOne
	private SystemFeedbackType type;
	@Override public SystemFeedbackType getType() {return type;}
	@Override public void setType(SystemFeedbackType type) {this.type = type;}

	@ManyToOne
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}
	
	
	@Override public boolean equals(Object object){return (object instanceof SystemFeedback) ? id == ((SystemFeedback) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}
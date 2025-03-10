package org.jeesl.model.ejb.module.cal.sub;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.sub.JeesCalSubscription;

@Entity
@Table(name="CalSubscription")
public class CalSubscription implements JeesCalSubscription<CalSubscriptionCategory>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalSubscription_category"))
	private CalSubscriptionCategory category;
	@Override public CalSubscriptionCategory getCategory() {return category;}
	@Override public void setCategory(CalSubscriptionCategory category) {this.category = category;}
	
	@Override public boolean equals(Object object){return (object instanceof CalSubscription) ? id == ((CalSubscription) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
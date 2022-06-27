package org.jeesl.client.model.ejb;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFrom;

@Entity
@Table(name = "UtilsValidFrom")
public class ValidFrom implements EjbWithValidFrom,Serializable
{
	public static final long serialVersionUID=1;
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>Fields<<<<<<<<<<<<<<<<<<<<<<<<<<<	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private Date validFrom;
	
	// >>>>>>>>>>>>>>>>>>>>>Getters and Setters<<<<<<<<<<<<<<<<<<<
	
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	public Date getValidFrom() {return validFrom;}
	public void setValidFrom(Date validFrom) {this.validFrom=validFrom;}
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>Methods<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
			sb.append(id);
			sb.append(" ").append(validFrom);
		return sb.toString();
	}
	
}
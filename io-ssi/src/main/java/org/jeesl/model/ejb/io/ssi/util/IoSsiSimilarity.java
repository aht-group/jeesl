package org.jeesl.model.ejb.io.ssi.util;

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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.util.JeeslIoSsiSimilarity;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;

@Entity
@Table(name="IoSsiSimilarity")
@EjbErNode(name="Data",category="ssi",subset="systemSsi")
public class IoSsiSimilarity implements JeeslIoSsiSimilarity<IoSsiSystem,SystemJobStatus>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoSsiSimilarity_systema"))
	private IoSsiSystem systemA;
	@Override public IoSsiSystem getSystemA() {return systemA;}
	@Override public void setSystemA(IoSsiSystem systemA) {this.systemA = systemA;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoSsiSimilarity_systemb"))
	private IoSsiSystem systemB;
	@Override public IoSsiSystem getSystemB() {return systemB;}
	@Override public void setSystemB(IoSsiSystem systemB) {this.systemB = systemB;}

	@Basic @Column(columnDefinition="text")
	private String textA;
	@Override public String getTextA() {return textA;}
	@Override public void setTextA(String textA) {this.textA = textA;}
	
	@Basic @Column(columnDefinition="text")
	private String textB;
	@Override public String getTextB() {return textB;}
	@Override public void setTextB(String textB) {this.textB = textB;}
	
	private double levenshtein;
	@Override public double getLevenshtein() {return levenshtein;}
	@Override public void setLevenshtein(double levenshtein) {this.levenshtein = levenshtein;}

	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoSsiSimilarity_feedback"))
	private SystemJobStatus feedback;
	@Override public SystemJobStatus getFeedback() {return feedback;}
	@Override public void setFeedback(SystemJobStatus feedback) {this.feedback = feedback;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiSimilarity) ? id == ((IoSsiSimilarity) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}
package org.jeesl.model.ejb.system.feedback;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemJobFeedbackType")
@EjbErNode(name="Feedback")
public class SystemJobFeedbackType extends IoStatus implements JeeslJobFeedbackType<IoLang,IoDescription,SystemJobFeedbackType,IoGraphic>
{
	public static final long serialVersionUID=1;
	
//	@Override public List<String> getFixedCodes()
//	{
//		List<String> fixed = new ArrayList<String>();
//		for(JeeslGraphicType.Code c : JeeslGraphicType.Code.values()){fixed.add(c.toString());}
//		return fixed;
//	}
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobFeedbackType) ? id == ((SystemJobFeedbackType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}
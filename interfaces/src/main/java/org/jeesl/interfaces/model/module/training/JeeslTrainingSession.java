package org.jeesl.interfaces.model.module.training;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.with.code.EjbWithCode;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.with.position.EjbWithPositionVisible;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface JeeslTrainingSession<L extends JeeslLang,D extends JeeslDescription,
										MODULE extends JeeslTrainingModule<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										SESSION extends JeeslTrainingSession<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										PAGE extends JeeslTrainingPage<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										TRAINING extends JeeslTraining<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										DAY extends JeeslTrainingDay<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										SLOT extends JeeslTrainingSlot<L,D,MODULE,SESSION,PAGE,TRAINING,DAY,SLOT,TYPE>,
										TYPE extends JeeslStatus<TYPE,L,D>
>
		extends EjbWithId,EjbSaveable,EjbRemoveable,
				EjbWithCode,EjbWithPositionVisible,
				EjbWithLang<L>,EjbWithDescription<D>
{	

}
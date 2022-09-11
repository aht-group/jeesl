package org.jeesl.controller.processor.io.label.revision;

import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.xml.audit.Change;
import net.sf.ahtutils.xml.audit.Revision;
import net.sf.ahtutils.xml.audit.Scope;
import net.sf.exlp.util.xml.JaxbUtil;

public class AuditScopeProcessor
{
	public AuditScopeProcessor(){}

    public List<Scope> group(List<Change> changes)
    {
        List<Scope> scopes = new ArrayList<Scope>();
        for (Change change : changes)
        {
        	JaxbUtil.trace(change);
			modificationWithChanges(changes, change.getScope(), "Change");
			if(scopes.size() == 0 || !compareScopes(scopes.get(scopes.size()-1), change.getScope())){scopes.add(change.getScope());}
        }
        deleteScopes(changes);
        return scopes;
    }
    
    public static boolean hasChanges(Scope scope)
    {
    	return (scope.isSetChange());
    }

    private void modificationWithChanges(List<Change> changes, Scope me, String elementToAdd)
    {
        for (Change ch: changes)
        {
            // add change to scope
            if(elementToAdd.equals("Change"))
            {
                if (compareScopes(ch.getScope(), me)){me.getChange().add(ch);}
            }
            // add scope to change
            if(elementToAdd.equals("Scope"))
            {
                for(Change c : me.getChange()){if(compareChanges(ch, c)) {ch.setScope(me);}}
            }
        }
    }

    private void modificationWithRevisions(List<Revision> revisions, Scope me, String elementToAdd)
    {
        for(Revision r : revisions)
        {
            // add revision to scope
            if (elementToAdd.equals("Revision"))
            {
                for(Scope sc : (r.getScope())){if(compareScopes(sc, me)) {me.setRevision(r);}}
            }
            // add scope to revision
            if(elementToAdd.equals("Scope")){if(compareRevisions(r, me.getRevision())){r.getScope().add(me);}}
        }
    }

    private boolean compareScopes(Scope sc1, Scope me)
    {
        String scope1 = sc1.getId() + "/" + sc1.getClazz();
		String scope2 = me.getId() + "/" + me.getClazz();
        return scope2.equals(scope1);
    }

    private boolean compareChanges(Change ch1, Change ch2)
    {
        String key = ch1.getAid() + "/" + ch1.getText();
        String key2 = ch2.getAid() + "/" + ch2.getText();
        return key2.equals(key);
    }

    private boolean compareRevisions(Revision r1, Revision r2)
    {
        String key = r1.getRev() + "/" + r1.getDate();
        String key2 = r2.getRev() + "/" + r2.getDate();
        return key2.equals(key);
    }

    private <E> void deleteScopes(List<E> elements)
    {
        for (E e : elements)
        {
            if(e instanceof Change){((Change)e).setScope(null);}
            if(e instanceof Revision){((Revision) e).unsetScope();}
        }
    }

    public List<Change> flat(List<Revision> revisions)
    {
    	List<Change> changes = new ArrayList<Change>();
        for(Revision rev : revisions)
        {
            for(Scope sc : rev.getScope())
            {
				for(Change ch : sc.getChange())
				{
					changes.add(ch);
				}
				modificationWithChanges(changes, sc, "Scope");
				modificationWithRevisions(revisions, sc, "Revision");
				sc.unsetChange();
            }
        }
        deleteScopes(revisions);
    	return changes;
    }
}
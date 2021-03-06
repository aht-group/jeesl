package org.jeesl.connectors.tools;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApplicationConfig extends Application
{
    private static final Set<Class<?>> CLASSES;

    static
    {
        HashSet<Class<?>> tmp = new HashSet<Class<?>>();
        tmp.add(WeapRequestService.class);
        CLASSES = Collections.unmodifiableSet(tmp);
    }

    @Override
    public Set<Class<?>> getClasses(){

       return  CLASSES;
    }    


}

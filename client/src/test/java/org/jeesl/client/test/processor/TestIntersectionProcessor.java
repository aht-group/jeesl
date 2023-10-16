package org.jeesl.client.test.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.client.model.ejb.system.locale.Status;
import org.jeesl.client.test.AbstractJeeslClientTest;
import org.jeesl.controller.processor.IntersectionProcessor;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestIntersectionProcessor extends AbstractJeeslClientTest
{
	final static Logger logger = LoggerFactory.getLogger(TestIntersectionProcessor.class);
	
	private static EjbStatusFactory<Lang,Description,Status> factory;
	
	private List<String> a,b,c;
	private List<List<String>> ab,abc;
	
	private List<Status> x,y;
	private List<List<Status>> xy;
	
	private List<Integer> k,m;
	private List<List<Integer>> km;
	
	@BeforeAll
	public static void initClass()
	{
		factory = EjbStatusFactory.instance(Status.class,Lang.class,Description.class);
	}
	
	@BeforeEach
	public void init()
	{		
		a = Arrays.asList("a b c d e f"      .split(" "));
		b = Arrays.asList(      "d e f g".split(" "));
		c = Arrays.asList("b d e".split(" "));

		ab = new ArrayList<List<String>>();ab.add(a);ab.add(b);
		abc = new ArrayList<List<String>>();abc.add(a);abc.add(b);abc.add(c);

		factory = EjbStatusFactory.instance(Status.class,Lang.class,Description.class); //within initClass() i got an NPE on .id(long l)
		x = new ArrayList<Status>();
		x.add(factory.id(1));x.add(factory.id(2));x.add(factory.id(3));
		y = new ArrayList<Status>();
		y.add(factory.id(3));y.add(factory.id(4));y.add(factory.id(5));
		xy = new ArrayList<List<Status>>();xy.add(x);xy.add(y);

		k = Arrays.asList(12,19,205,2401,325871);
		m = Arrays.asList(45,365,12,4787,15479912);
		km = new ArrayList<List<Integer>>();km.add(k);km.add(m);
	}
	
	@Test public void pre()
    {	
		Assertions.assertEquals(6, a.size());
		Assertions.assertEquals(4, b.size());
		Assertions.assertEquals(3, c.size());
		Assertions.assertEquals(3, x.size());
		Assertions.assertEquals(3, y.size());
		Assertions.assertEquals(5, k.size());
		Assertions.assertEquals(5, m.size());
    }
 
	@Test public void basicAnd()
    {	
		List<String> expected = Arrays.asList("d e f".split(" "));
    	List<String> actual = IntersectionProcessor.and(a,b);
    	Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }
    
    @Test public void basicOr()
    {	
    	List<String> result = IntersectionProcessor.or(a,b);
    	Assertions.assertEquals(7, result.size());
    }
    
    @Test public void stringSingleWorkaround()
    {
		List<String> actualA = IntersectionProcessor.query("a AND a",ab);
		List<String> actualB = IntersectionProcessor.query("b AND b",ab);
		Assertions.assertArrayEquals(a.toArray(new String[a.size()]), actualA.toArray(new String[actualA.size()]));
		Assertions.assertArrayEquals(b.toArray(new String[b.size()]), actualB.toArray(new String[actualB.size()]));
    }
    
    @Test public void stringSingle()
    {
		List<String> actualA = IntersectionProcessor.query("a",ab);
		List<String> actualB = IntersectionProcessor.query("b",ab);
		Assertions.assertArrayEquals(a.toArray(new String[a.size()]), actualA.toArray(new String[actualA.size()]));
		Assertions.assertArrayEquals(b.toArray(new String[b.size()]), actualB.toArray(new String[actualB.size()]));
    }
    
    @Test public void stringAnd()
    {
    	List<String> expected = Arrays.asList("d e f".split(" "));
		List<String> actual = IntersectionProcessor.query("a AND b",ab);
		Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }
    
    @Test public void stringOr()
    {
    	List<String> expected = Arrays.asList("a b c d e f g".split(" "));
		List<String> actual = IntersectionProcessor.query("a OR b",ab);
		Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }
	
    @Test public void integerAnd()
    {
		List<Integer> result = IntersectionProcessor.query("a && b",km);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(new Integer(12), result.get(0));
    }

	@Test public void simpleCombination()
	{
		List<String> expected = Arrays.asList("f b d e".split(" "));
		List<String> actual = IntersectionProcessor.query("a AND (b OR c)",abc);
		Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
	}
	
	@Test public void plain3()
	{		
		List<String> expected = Arrays.asList("d e".split(" "));
		List<String> actual = IntersectionProcessor.query("a AND b AND c",abc);
		Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
	}
	
	@Test public void plain3Workaround()
	{
		List<String> expected = Arrays.asList("d e".split(" "));
		List<String> actual = IntersectionProcessor.query("a AND (b AND c)",abc);
		logger.debug(actual.toString());
		Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
	}
	
	@Test public void idAnd()
    {
		List<Status> expected = new ArrayList<Status>();
		expected.add(factory.id(3));
		
		List<Status> actual = IntersectionProcessor.query("a AND b", xy);
		Assertions.assertEquals(1, actual.size());
		Assertions.assertEquals(expected.get(0), actual.get(0));
    }
}
package org.hibernateexamples.performance;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.cfg.Configuration;
import org.hibernateexamples.AbstractHibernateTests;
import org.junit.Test;

public class InsertionPerformanceTests extends AbstractHibernateTests {

	private static int NUMBER_OF_ROWS = 10000;
	
	@Test
	public void testNaieveInsertion() {
		System.out.println("testNaieveInsertion");
		long now = System.currentTimeMillis();
		Collection<SimpleClass> simpleClasses = new ArrayList<SimpleClass>();
		for (int i=0; i<NUMBER_OF_ROWS; i++) {
			SimpleClass sc = initSimpleClass();
			simpleClasses.add(sc);
		}
		
		for (SimpleClass sc: simpleClasses) {
			this.session.save(sc);
		}
		
		System.out.println("Flushing ... ");
		this.session.flush();
		System.out.println("Flushed.  Total time: " + (System.currentTimeMillis() - now));
	}
	
	@Test
	public void testApparantlyEfficientButNotReallyInsertion() {
		System.out.println("testApparantlyEfficientButNotReallyInsertion");
		long now = System.currentTimeMillis();
		for (int i=0; i<NUMBER_OF_ROWS; i++) {
			SimpleClass sc = initSimpleClass();
			this.session.save(sc);
		}
		
		System.out.println("Flushing ... ");
		this.session.flush();
		System.out.println("Flushed.  Total time: " + (System.currentTimeMillis() - now));
	}

	@Test
	public void testEfficientInsertion() {
		System.out.println("testEfficientInsertion");
		long now = System.currentTimeMillis();
		for (int i=0; i<NUMBER_OF_ROWS; i++) {
			SimpleClass sc = initSimpleClass();
			this.session.save(sc);
			
			if (i % 20 == 0) {
				session.flush();
				session.clear();
			}
		}
		
		this.session.flush();
		System.out.println("Flushed.  Total time: " + (System.currentTimeMillis() - now));
	}

	
	@Override
	protected void addResources(Configuration config) {
		String packagePrefix = determinePackageName();
		config.addResource(packagePrefix + "SimpleClass.hbm.xml");
	}


	private SimpleClass initSimpleClass() {
		SimpleClass sc = new SimpleClass();
		sc.setFirstCol("first col");
		sc.setSecondCol("second col");
		sc.setThirdCol("third col");
		sc.setFourthCol("fourth col");
		sc.setFifthCol("fifth col");
		sc.setSixthCol("sixth col");
		return sc;
		
	}
}

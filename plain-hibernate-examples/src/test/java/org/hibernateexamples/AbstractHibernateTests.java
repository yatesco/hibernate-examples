/**
 * Copyright 2008 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.hibernateexamples;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

/**
 * Common base class for all Hibernate tests.
 * 
 * <p>We cannot simply run each test method in a separate transaction because
 * each test class has a different DB structure, and DDL isn't executed transactionally.
 * 
 * <p>Instead, this class basically constructs the sessionFactory once per class instance,
 * however because it needs to access the current class's working directory (to determine
 * the package), it cannot be in a <code>BeforeClass</code> method which have to be
 * static methods.  
 * 
 * <p>Special.  Real special.
 * 
 * @author coliny
 *
 */
public abstract class AbstractHibernateTests {

	protected static SessionFactory sessionFactory;
	protected Session session;
	protected Transaction transaction;

	/**
	 * Setup the hibernate context....this actually recreates the database everytime....
	 */
	@Before
	public void setUpHibernate() {
		if (AbstractHibernateTests.sessionFactory == null) {
			Configuration config = new Configuration();
	        addResources(config);        
	        config.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
	        config.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
	        config.setProperty("hibernate.show_sql", "false");
	        config.setProperty("hibernate.format_sql", "true");
	        config.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:/c/tmp/hsqldb.db");
	        config.setProperty("hibernate.connection.username", "sa");
	        config.setProperty("hibernate.jdbc.batchsize", "50");
	        AbstractHibernateTests.sessionFactory = config.buildSessionFactory();
	        
	        SchemaUpdate schemaUpdate = new SchemaUpdate(config);
	        schemaUpdate.execute(true, true);
		}
		
        this.session = this.sessionFactory.openSession();
        this.transaction = this.session.beginTransaction();
	}
	
	@After
	public void cleanUp() {
		if (this.transaction != null) {
			this.transaction.rollback();
			this.session.clear();
		}
	}
	
	@AfterClass
	public static void resetSessionFactory() {
		sessionFactory = null;
	}

	/**
	 * Default behaviour is to add a mapping file called <code>Child.hbm.xml</code>
	 * and <code>Parent.hbm.xml</code>.
	 * 
	 * <p>Subclasses may override this.
	 * @param config
	 */
	protected void addResources(Configuration config) {
		String packagePrefix = determinePackageName();
		config.addResource(packagePrefix + "Child.hbm.xml");
        config.addResource(packagePrefix + "Parent.hbm.xml");
	}

	/**
	 * Convenience method that will convert the current fully qualified
	 * class name and package into a form suitable for importing as
	 * Spring resources.
	 * 
	 * <p>If the current class is <code>org.hibernateexamples.ClassName</code>
	 * then this will return <code>org/hibernateexamples/</code>
	 * @return
	 */
	@SuppressWarnings("unused")
	protected String determinePackageName() {
		String className = getClass().getName();
		// strip out the last class
		int indexOfClassName = className.lastIndexOf(".");
		if (indexOfClassName >= 0) {
			className = className.substring(0, indexOfClassName);
		}
		String packageName = className.replace(".", "/");
		return packageName + "/";
	}	
}

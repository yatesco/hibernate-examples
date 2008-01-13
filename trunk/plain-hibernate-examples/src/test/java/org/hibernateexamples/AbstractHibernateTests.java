package org.hibernateexamples;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.Before;

/**
 * Common base class for all Hibernate tests.
 * 
 * <p>This uses Spring's very convenient {@link AbstractTransactionalSpringContextTests}
 * to wrap each method in a transaction. 
 * 
 * @author coliny
 *
 */
public abstract class AbstractHibernateTests {

	protected SessionFactory sessionFactory;

	/**
	 * Setup the hibernate context....this actually recreates the database everytime....
	 */
	@Before
	public void setUpHibernate() {
		Configuration config = new Configuration();
		String packagePrefix = determinePackageName();
        config.addResource(packagePrefix + "Child.hbm.xml");
        config.addResource(packagePrefix + "Parent.hbm.xml");        
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
// not working        config.setProperty("hibernate.hbm2ddl.auto", "create–drop");
        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.connection.url", "jdbc:hsqldb:.");
        config.setProperty("hibernate.connection.username", "sa");
        this.sessionFactory = config.buildSessionFactory();
        
        SchemaUpdate schemaUpdate = new SchemaUpdate(config);
        schemaUpdate.execute(true, true);
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
	private String determinePackageName() {
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

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
package org.hibernateexamples.onetoone.foreignkey;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernateexamples.collections.set.AbstractCollectionTests;
import org.hibernateexamples.onetoone.Passport;
import org.hibernateexamples.onetoone.Person;
import org.junit.Test;

/**
 * Test that both the passport and person have the same primary key
 * 
 * @author coliny
 *
 */
public class OneToOneTests extends AbstractCollectionTests {

	@Test
	public void testItWorks() {
		Session session = this.sessionFactory.openSession();
		
		Person person = new Person();
		Passport passport = new Passport();		
		person.setPassport(passport);
		passport.setPerson(person);
		
		/**
		 * Because we have no cascade (we could, but haven't) we *must*
		 * save the end that has the assigned id first.
		 */
		session.save(passport);
		session.save(person);
		session.clear();
		
		person = (Person) session.load(Person.class, person.getId());
		passport = (Passport) session.load(Passport.class, passport.getId());
		assertNotSame("should have different keys", person.getId(), passport.getId());
	}

	@Override
	protected void addResources(Configuration config) {
		config.addResource(determinePackageName() + "Passport.hbm.xml");
		config.addResource(determinePackageName() + "Person.hbm.xml");

	}
}

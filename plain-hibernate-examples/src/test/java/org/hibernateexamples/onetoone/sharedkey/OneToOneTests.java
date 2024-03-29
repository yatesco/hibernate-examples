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
package org.hibernateexamples.onetoone.sharedkey;

import static org.junit.Assert.assertEquals;

import org.hibernate.cfg.Configuration;
import org.hibernateexamples.collections.set.AbstractCollectionTests;
import org.hibernateexamples.onetoone.Passport;
import org.hibernateexamples.onetoone.Person;
import org.junit.Test;

/**
 * Test that both the passport and person have the a 
 * foreign key
 * 
 * @author coliny
 *
 */
public class OneToOneTests extends AbstractCollectionTests {

	@Test
	public void testItWorks() {
		Person person = new Person();
		Passport passport = new Passport();
		// update both sides....
		person.setPassport(passport);
		passport.setPerson(person);
		
		this.session.save(person);
		this.session.save(passport);
		this.session.clear();
		
		person = (Person) session.load(Person.class, person.getId());
		passport = (Passport) session.load(Passport.class, passport.getId());
		assertEquals("should have same primary key", person.getId(), passport.getId());
	}

	@Override
	protected void addResources(Configuration config) {
		config.addResource(determinePackageName() + "Passport.hbm.xml");
		config.addResource(determinePackageName() + "Person.hbm.xml");

	}
}

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
package org.hibernateexamples.collections.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernateexamples.AbstractHibernateTests;
import org.junit.Before;

/**
 * Convenience test class for testing Hibernate collection
 * mappings.
 * 
 * @author coliny
 *
 */
public class AbstractCollectionTests extends AbstractHibernateTests {

	protected Parent parent;
	protected Child childA;
	protected Child childB;
	
	@Before
	public void setUpChildren() {
		this.parent = new Parent();
		this.childA = new Child();
		this.childB = new Child();
	}
	
	/**
	 * Verify that both the {@link #parent} and
	 * {@link #childA} and {@link #childB} know each other.
	 */
	protected void verifyKidsAndParentKnowEachOther() {
		// verify the parent has kids
		Collection<Child> kids = parent.getChildren();
		assertEquals("parent should have some kids", 2, kids.size());
		assertTrue("parent has childA", kids.contains(childA));
		assertTrue("parent has childB", kids.contains(childB));
		
		// verify kids have the right parent
		assertEquals("childA has wrong parent", parent, childA.getParent());
		assertEquals("childB has wrong parent", parent, childB.getParent());
	}

	/**
	 * Reload the {@link #parent}, {@link #childA} and {@link #childB}.
	 * 
	 * This will clear the specified {@link Session session} so
	 * that Hibernate will load the parent and kids from the DB.  
	 * 
	 * @param session
	 */
	protected void reloadParentAndChildren(Session session) {
		session.clear();
		parent = (Parent) session.get(Parent.class, parent.getId());
		childA = (Child) session.get(Child.class, childA.getId());
		childB = (Child) session.get(Child.class, childB.getId());
	}
	
}

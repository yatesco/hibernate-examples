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
package org.hibernateexamples.cascade.none;

import org.hibernate.TransientObjectException;
import org.hibernateexamples.AbstractHibernateTests;
import org.hibernateexamples.collections.set.Child;
import org.hibernateexamples.collections.set.Parent;
import org.junit.Test;

/**
 * Demonstrate the cascade behaviour of Hibernate when there are no cascades
 * 
 * <p>Parent 1..m Child but cascade=none.
 * 
 * @author coliny
 *
 */
public class CascadeTests extends AbstractHibernateTests {
	
	/**
	 * Parent 1..m Child but cascade=none.
	 * 
	 * <p>Saving a parent will result in the {@link TransientObjectException}
	 * because we haven't told Hibernate to cascade the save down to the (new)
	 * children.
	 */
	@Test(expected=TransientObjectException.class)
	public void testSavingParent() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.addChild(child);
		
		// because there are no cascades from parent->child, we will get a TOE.
		this.session.save(parent);
		this.session.flush();
	}
	
	/**
	 * If we explicitly save the children first, then this will work fine.
	 */
	@Test
	public void testSavingChildFirst() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.addChild(child);
		
		this.session.save(parent);
		this.session.save(child);
		this.session.flush();
	}
	
	/**
	 * If we reference an existing child, then that is also fine.
	 */
	@Test
	public void testReferencingExistingChild() {
		Child existingChild = new Child();
		this.session.save(existingChild);
		this.session.flush();
		this.session.evict(existingChild);
		
		Parent parent = new Parent();
		parent.addChild(existingChild);
		
		this.session.save(parent);
		this.session.flush();
	}
}

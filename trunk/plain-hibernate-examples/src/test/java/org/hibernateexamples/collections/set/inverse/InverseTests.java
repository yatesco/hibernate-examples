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
package org.hibernateexamples.collections.set.inverse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.hibernateexamples.collections.set.AbstractCollectionTests;
import org.hibernateexamples.collections.set.Child;
import org.hibernateexamples.collections.set.Parent;
import org.junit.Test;

/**
 * Test the inverse behaviour when using a set.
 * 
 * @author coliny
 *
 */
public class InverseTests extends AbstractCollectionTests {

	/**
	 * If only the parent has children, but those children
	 * do not point to the parent, hibernate will save all
	 * of them (depending on cascade), however the foreign key
	 * will be null.
	 */
	@Test
	public void parentToChildren() {
		Parent parent = new Parent();
		Child childA = new Child();
		Child childB = new Child(); 		
		parent.addChildWithoutSettingParent(childA);
		parent.addChildWithoutSettingParent(childB);
		
		this.session.save(parent);
		this.session.flush();

		// clear the flush, and reload the objects
		this.session.clear();
		parent = (Parent) this.session.load(Parent.class, parent.getId());
		childA = (Child) this.session.load(Child.class, childA.getId());
		childB = (Child) this.session.load(Child.class, childB.getId());		
		
		// verify the kids are orphaned
		assertFalse("parent should not, er, be a parent", parent.hasChildren());
		
		// verify the kids are orphans
		assertNull("childA should be orphaned", childA.getParent());
		assertNull("childB should be orphaned", childB.getParent());		
	}

	/**
	 * If the children have the parent, the
	 * parent doesn't have the children, the relationship should
	 * be saved
	 */
	@Test
	public void childToParent() {
		this.session.save(parent);
		
		childA.setParent(parent);
		childB.setParent(parent);
		
		// save the children.  No need to save the parent.
		this.session.save(childA);
		this.session.save(childB);		
		this.session.flush();
		
		reloadParentAndChildren(this.session);
		
		verifyKidsAndParentKnowEachOther();
	}
	
	/**
	 * Test what happens when both children and parent know each other.
	 * 
	 * <p>We expect the same behaviour of {@link #testInverseWhenChildHaveParent()}
	 */
	@Test
	public void happyFamily() {
		parent.addChild(childA);
		parent.addChild(childB);
		
		// save the parent, cascade will reach the kids
		this.session.save(parent);
		this.session.flush();
		
		reloadParentAndChildren(this.session);
		
		verifyKidsAndParentKnowEachOther();
	}
	
	/**
	 * If the parent has the children, but the children
	 * think they have other parents, what happens?  Kids win.
	 */
	@Test
	public void confusedKids() {
		Parent parentA = new Parent();
		Parent parentB = new Parent();
		Child childA = new Child();
		Child childB = new Child();		
		parentA.addChild(childA);
		parentA.addChild(childB);
		
		// leave the child in parentA, but set childA to point to parentB
		childB.setParent(parentB);
		
		// save the parent, cascade will reach the kids
		this.session.save(parentA);
		this.session.save(parentB);		
		this.session.flush();
		
		// clear the flush, and reload the objects
		this.session.clear();
		parentA = (Parent) this.session.load(Parent.class, parentA.getId());
		parentB = (Parent) this.session.load(Parent.class, parentB.getId());		
		childA = (Child) this.session.load(Child.class, childA.getId());
		childB = (Child) this.session.load(Child.class, childB.getId());		
		
		/**
		 * Because the inverse is true, the fact that childB has parentB
		 * wins over the fact that parentA has childA.
		 * 
		 * We expect:
		 *  parentA -> childA
		 *  parentB -> childB
		 */
		Collection<Child> kidsA = parentA.getChildren();
		assertEquals("parentA should one kid", 1, kidsA.size());
		assertTrue("parentA should have childA", kidsA.contains(childA));

		Collection<Child> kidsB = parentB.getChildren();
		assertEquals("parentB should one kid", 1, kidsB.size());
		assertTrue("parentB should have childB", kidsB.contains(childB));

		// verify kids have the expected parent
		assertEquals("childA has wrong parent", parentA, childA.getParent());
		assertEquals("childB has wrong parent", parentB, childB.getParent());		
	}
}

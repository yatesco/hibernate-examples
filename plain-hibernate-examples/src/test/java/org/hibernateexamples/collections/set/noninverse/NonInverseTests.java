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
package org.hibernateexamples.collections.set.noninverse;

import static org.junit.Assert.assertFalse;

import org.hibernateexamples.collections.set.AbstractCollectionTests;
import org.hibernateexamples.collections.set.Parent;
import org.junit.Test;

/**
 * Test the (default) non-inverse behaviour when using a set.
 * 
 * In a nut shell, the parent->child association wins.  Bizarrely,
 * if the child->parent is set, but the parent->child isn't, the relationship
 * is still set....
 * 
 * @author coliny
 *
 */
public class NonInverseTests extends AbstractCollectionTests {

	/**
	 * If only the parent has children, but those children
	 * do not point to the parent, hibernate will save all
	 * of them (depending on cascade) and the foreign key
	 * will be set.
	 */
	@Test
	public void parentToChild() {
		parent.addChildWithoutSettingParent(childA);
		parent.addChildWithoutSettingParent(childB);
		
		this.session.save(parent);
		this.session.flush();
		
		// clear the flush, and reload the objects
		reloadParentAndChildren(this.session);		
		
		verifyKidsAndParentKnowEachOther();
	}

	/**
	 * Because inverse is not true, even if we don't add the child to 
	 * the parent, it will still be saved!!!!
	 */
	@Test
	public void childToParentIsEnoughEvenThoughItIsInconsistent() {
		this.session.save(parent);
		this.session.flush();
		
		childA.setParent(parent);
		childB.setParent(parent);
		
		// save the children.  No need to save the parent.
		this.session.save(childA);
		this.session.save(childB);		
		this.session.flush();
		
		reloadParentAndChildren(this.session);		
		
		/**
		 * Despite the fact that inverse is not true, and we haven't added 
		 * the children to the parent, the relationship is still persisted!!!
		 */
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
	 * think they have other parents, what happens?
	 * 
	 * The Parent wins.
	 */
	@Test
	public void confusedKids() {
		Parent parentB = new Parent();
		parent.addChild(childA);
		parent.addChild(childB);
		
		// leave the child in parentA, but set childA to point to parentB
		childB.setParent(parentB);
		
		// save the parent, cascade will reach the kids
		this.session.save(parent);
		this.session.save(parentB);		
		this.session.flush();
		
		// clear the flush, and reload the objects
		reloadParentAndChildren(this.session);
		parentB = (Parent) this.session.load(Parent.class, parentB.getId());		
		
		/**
		 * Because the inverse is false, the fact that childB has parentB
		 * is ignored, and loses to the fact that parentA has childA
		 */
		verifyKidsAndParentKnowEachOther();
		// make sure parentB hasn't pinched any kids...
		assertFalse("parentB should be childless", parentB.hasChildren());
	}
}

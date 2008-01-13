package org.hibernateexamples.collections.set.noninverse;

import static org.junit.Assert.assertFalse;

import org.hibernate.Session;
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
		Session session = this.sessionFactory.openSession();
		parent.addChildWithoutSettingParent(childA);
		parent.addChildWithoutSettingParent(childB);
		
		session.save(parent);
		session.flush();
		
		// clear the flush, and reload the objects
		reloadParentAndChildren(session);		
		
		verifyKidsAndParentKnowEachOther();
	}

	/**
	 * Because inverse is not true, even if we don't add the child to 
	 * the parent, it will still be saved!!!!
	 */
	@Test
	public void childToParentIsEnoughEvenThoughItIsInconsistent() {
		Session session = this.sessionFactory.openSession();
		session.save(parent);
		session.flush();
		
		childA.setParent(parent);
		childB.setParent(parent);
		
		// save the children.  No need to save the parent.
		session.save(childA);
		session.save(childB);		
		session.flush();
		
		reloadParentAndChildren(session);		
		
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
		Session session = this.sessionFactory.openSession();
		parent.addChild(childA);
		parent.addChild(childB);
		
		// save the parent, cascade will reach the kids
		session.save(parent);
		session.flush();
		
		reloadParentAndChildren(session);		
		
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
		Session session = this.sessionFactory.openSession();
		Parent parentB = new Parent();
		parent.addChild(childA);
		parent.addChild(childB);
		
		// leave the child in parentA, but set childA to point to parentB
		childB.setParent(parentB);
		
		// save the parent, cascade will reach the kids
		session.save(parent);
		session.save(parentB);		
		session.flush();
		
		// clear the flush, and reload the objects
		reloadParentAndChildren(session);
		parentB = (Parent) session.load(Parent.class, parentB.getId());		
		
		/**
		 * Because the inverse is false, the fact that childB has parentB
		 * is ignored, and loses to the fact that parentA has childA
		 */
		verifyKidsAndParentKnowEachOther();
		// make sure parentB hasn't pinched any kids...
		assertFalse("parentB should be childless", parentB.hasChildren());
	}
}

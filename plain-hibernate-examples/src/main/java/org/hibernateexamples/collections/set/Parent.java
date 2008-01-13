package org.hibernateexamples.collections.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernateexamples.Entity;

/**
 * Simple parent which demonstrates a {@link Set set} of
 * {@link Child children}.
 * 
 * <p>Follows best practice of not exposing the children
 * for mutation, rather an {@link #addChild(Child) add method}
 * is exposed.  
 * 
 * @author coliny
 *
 */
public class Parent extends Entity {

	private Set<Child> children = new HashSet<Child>();

	/**
	 * Add a {@link Child child} to this parent.
	 * 
	 * <p>This follows best practice and updates both ends
	 * of the bi-directional relationship.
	 * 
	 * <p>After calling this method the specified <code>child</code>
	 * will be added to this parent's {@link #getChildren() children} and
	 * the <code>child's</code> {@link Child#getParent() parent} will refer
	 * to <code>this</code>.
	 * 
	 * @param child the <code>Child</code> to add to this parent.
	 * 				Should not be <code>null</code>.
	 */
	public void addChild(Child child) {
		this.children.add(child);
		child.setParent(this);
	}
	
	/**
	 * Convenient method that adds the specified {@link Child child}
	 * without setting the child->parent relationship.
	 * 
	 * @param child the <code>Child</code> to add to this parent.
	 * 				Should not be <code>null</code>.
	 */
	public void addChildWithoutSettingParent(Child child) {
		this.children.add(child);
	}
	
	/**
	 * Remove the specified {@link Child child} from this parent.
	 * 
	 * <p>This follows best practice and updates both ends
	 * of the bi-directional relationship.
	 * 
	 * <p>After calling this method the specified <code>child</code>
	 * will be remove from this parent's {@link #getChildren() children} and
	 * the <code>child's</code> {@link Child#getParent() parent} will 
	 * be <code>null</code>.
	 * 
	 * <p>Maybe this should actually check that the child is actually
	 * one the {@link #children parent's children} before 
	 * {@link Child#setParent(Parent) setting the parent} to <code>null</code>.
	 * 
	 * @param child the <code>Child</code> to add to this parent.
	 * 				Should not be <code>null</code>.
	 */
	public boolean removeChild(Child child) {
		return this.children.remove(child);
	}
	
	/**
	 * Return the children.  Changes to the result of this method
	 * will have no effect.
	 * 
	 * @return a copy of the {@link Child children} for this object.
	 */
	public Collection<Child> getChildren() {
		return new HashSet<Child>(this.children);
	}

	public boolean hasChildren() {
		return !this.children.isEmpty();
	}
	
}

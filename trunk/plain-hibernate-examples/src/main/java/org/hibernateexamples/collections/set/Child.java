package org.hibernateexamples.collections.set;

import org.hibernateexamples.Entity;

public class Child extends Entity {

	private Parent parent;

	public Child(Parent parent) {
		this.parent = parent;
	}
	
	public Child() {
		
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}
}

package org.hibernateexamples;

/**
 * Common base class for all Entities.
 * 
 * <p>Note: this exposes a getter/setter for the id, which is questionable,
 * (favour field level access), but I have left it here for simplicity
 * of the mapping file.
 * 
 * @author coliny
 *
 */
public abstract class Entity {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}

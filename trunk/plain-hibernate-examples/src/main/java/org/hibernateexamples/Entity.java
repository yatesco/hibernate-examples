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

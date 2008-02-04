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
package org.hibernateexamples.examples.searching.nested;

import org.hibernateexamples.Entity;

public class Audit extends Entity {
	
	private int auditIndex;

	private Audit() {
		// hibernate requires this when aliasing in Criteria queries.
	}

	public Audit(int i) {
		this.auditIndex = i;
	}

	public int getAuditIndex() {
		return auditIndex;
	}

	public void setAuditIndex(int index) {
		this.auditIndex = index;
	}
}

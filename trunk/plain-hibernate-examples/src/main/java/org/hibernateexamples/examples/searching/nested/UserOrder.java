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

import java.util.ArrayList;
import java.util.List;

import org.hibernateexamples.Entity;

public class UserOrder extends Entity {

	private List<LineItem> lineItems = new ArrayList<LineItem>();

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	@Override
	public String toString() {
		LineItem item = this.lineItems.get(0);
		Audit audit = item.getAudits().get(0);
		return "Audit: " + audit.getAuditIndex();
	}
}

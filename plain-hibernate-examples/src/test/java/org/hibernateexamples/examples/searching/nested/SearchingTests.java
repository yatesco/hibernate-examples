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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernateexamples.AbstractHibernateTests;
import org.junit.Before;
import org.junit.Test;

/**
 * These tests are very broken, do not use them.  They are temporarily checked in
 * so I can chat through with a (remote) colleague.
 * @author coliny
 *
 */
public class SearchingTests extends AbstractHibernateTests {

	private UserOrder orderA;
	private UserOrder orderB;
	private org.hibernate.classic.Session session;

	@Before
	public void setUp() {
		// order with two line items, the first lineItem has the higher audit.
		orderA = new UserOrder();
		LineItem lineItemA2 = new LineItem();
		Audit auditA2 = new Audit(3);
		lineItemA2.getAudits().add(auditA2);
		orderA.getLineItems().add(lineItemA2);

		LineItem lineItemA1 = new LineItem();
		Audit auditA1 = new Audit(1);
		lineItemA1.getAudits().add(auditA1);
		orderA.getLineItems().add(lineItemA1);
		
		// order with one line item, audit is middle index.
		orderB = new UserOrder();
		LineItem lineItemB1 = new LineItem();
		Audit auditB1 = new Audit(2);
		lineItemB1.getAudits().add(auditB1);
		orderB.getLineItems().add(lineItemB1);
		
		session = this.sessionFactory.openSession();
		
		// save them in the incorrect order
		session.save(orderB);
		session.save(orderA);
		session.flush();
	}
	
	/**
	 * Order 1..* LineItem.  LineItem 1..* Audits.
	 * 
	 * Audit has an index, so we want to search all Orders ordered by Audit.index.
	 */
	@Test
	public void testWithHql() {		
		String hql = "from UserOrder o " +
				"left join o.lineItems as item " +
				"left join item.audits as audit " +
				"order by audit.auditIndex asc";

		// it is an object[] because we are doing multiple joins
		List<Object[]> orders = session.createQuery(hql).list();
		assertEquals("first row should be first order", orderA, orders.get(0)[0]);
		assertEquals("second row should be second order", orderB, orders.get(1)[0]);
		assertEquals("second row should be first order again", orderA, orders.get(2)[0]);
	}
	
	@Test
	public void testWithCriteria() {
		Session session = this.sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserOrder.class)
			.createAlias("lineItems", "item")
			.createAlias("item.audits", "audit")
			.addOrder(Order.asc("audit.auditIndex"));
		
		List<UserOrder> orders = criteria.list();
		System.out.println(orders);
//		assertEquals("first row should be first order", orderA, orders.get(0));
//		assertEquals("second row should be second order", orderB, orders.get(1));
//		assertEquals("second row should be first order again", orderA, orders.get(2));
	}

	@Override
	protected void addResources(Configuration config) {
		String packageName = determinePackageName();
		config.addResource(packageName + "Audit.hbm.xml");
		config.addResource(packageName + "LineItem.hbm.xml");
		config.addResource(packageName + "UserOrder.hbm.xml");
	}
}

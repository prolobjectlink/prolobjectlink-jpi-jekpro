/*-
 * #%L
 * prolobjectlink-jpi-jekpro
 * %%
 * Copyright (C) 2012 - 2019 Prolobjectlink Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.github.prolobjectlink.prolog.jekpro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologTerm;

public class PrologQueryIteratorTest extends PrologBaseTest {

	private Iterator<Collection<PrologTerm>> i;

	@Before
	public final void setUp() throws Exception {

		PrologEngine engine = provider.newEngine();

		// employee relationship
		engine.assertz("employee( mcardon, 1, 5 )");
		engine.assertz("employee( treeman, 2, 3 )");
		engine.assertz("employee( chapman, 1, 2 )");
		engine.assertz("employee( claessen, 4, 1 )");
		engine.assertz("employee( petersen, 5, 8 )");
		engine.assertz("employee( cohn, 1, 7 )");
		engine.assertz("employee( duffy, 1, 9 )");

		// department relationship
		engine.assertz("department( 1, board )");
		engine.assertz("department( 2, human_resources )");
		engine.assertz("department( 3, production )");
		engine.assertz("department( 4, technical_services )");
		engine.assertz("department( 5, administration )");

		// salary relationship
		engine.assertz("salary( 1, 1000 )");
		engine.assertz("salary( 2, 1500 )");
		engine.assertz("salary( 3, 2000 )");
		engine.assertz("salary( 4, 2500 )");
		engine.assertz("salary( 5, 3000 )");
		engine.assertz("salary( 6, 3500 )");
		engine.assertz("salary( 7, 4000 )");
		engine.assertz("salary( 8, 4500 )");
		engine.assertz("salary( 9, 5000 )");

		i = engine.query("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)").iterator();

	}

	@After
	public final void tearDown() throws Exception {
	}

	@Test
	public final void testRemove() {
		i.remove();
//		assertEquals(Arrays.asList(human_resources, twoThousand, three, treeman, two),
//				new ArrayList<PrologTerm>(i.next()));

//		assertEquals(Arrays.asList(treeman, two, three, human_resources, twoThousand),
//				new ArrayList<PrologTerm>(i.next()));

//		assertEquals(Arrays.asList(two, treeman, twoThousand, human_resources, three),
//				new ArrayList<PrologTerm>(i.next()));

		assertEquals(Arrays.asList(twoThousand, two, human_resources, three, treeman),
				new ArrayList<PrologTerm>(i.next()));

	}

	@Test
	public final void testHasNext() {
		assertTrue(i.hasNext());
	}

	@Test
	public final void testNext() {

		// Jekejeke Prolog have order problem in result
//		assertEquals(Arrays.asList(board, threeThousand, five, mcardon, one), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(human_resources, twoThousand, three, treeman, two),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(board, thousandFiveHundred, two, chapman, one), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(technical_services, thousand, one, claessen, four),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(administration, fourThousandFiveHundred, eight, petersen, five),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(board, fourThousand, seven, cohn, one), new ArrayList<PrologTerm>(i.next()));
		// assertThrows(NoSuchElementException.class, i.next());
//		assertFalse(i.hasNext());

		// Setting the test to Jekejeke Prolog order
//		assertEquals(Arrays.asList(mcardon, one, five, board, threeThousand), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(treeman, two, three, human_resources, twoThousand),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(chapman, one, two, board, thousandFiveHundred), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(claessen, four, one, technical_services, thousand),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(petersen, five, eight, administration, fourThousandFiveHundred),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(cohn, one, seven, board, fourThousand), new ArrayList<PrologTerm>(i.next()));
//		// assertThrows(NoSuchElementException.class, i.next());
//		assertFalse(i.hasNext());

		// Setting the test to Jekejeke Prolog order
//		assertEquals(Arrays.asList(one, mcardon, threeThousand, board, five), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(two, treeman, twoThousand, human_resources, three),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(one, chapman, thousandFiveHundred, board, two), new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(four, claessen, thousand, technical_services, one),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(five, petersen, fourThousandFiveHundred, administration, eight),
//				new ArrayList<PrologTerm>(i.next()));
//		assertEquals(Arrays.asList(one, cohn, fourThousand, board, seven), new ArrayList<PrologTerm>(i.next()));
		// assertThrows(NoSuchElementException.class, i.next());
		// assertFalse(i.hasNext());

		assertEquals(Arrays.asList(threeThousand, one, board, five, mcardon), new ArrayList<PrologTerm>(i.next()));
		assertEquals(Arrays.asList(twoThousand, two, human_resources, three, treeman),
				new ArrayList<PrologTerm>(i.next()));
		assertEquals(Arrays.asList(thousandFiveHundred, one, board, two, chapman), new ArrayList<PrologTerm>(i.next()));
		assertEquals(Arrays.asList(thousand, four, technical_services, one, claessen),
				new ArrayList<PrologTerm>(i.next()));
		assertEquals(Arrays.asList(fourThousandFiveHundred, five, administration, eight, petersen),
				new ArrayList<PrologTerm>(i.next()));
		assertEquals(Arrays.asList(fourThousand, one, board, seven, cohn), new ArrayList<PrologTerm>(i.next()));
		// assertThrows(NoSuchElementException.class, i.next());
		// assertFalse(i.hasNext());

	}

}

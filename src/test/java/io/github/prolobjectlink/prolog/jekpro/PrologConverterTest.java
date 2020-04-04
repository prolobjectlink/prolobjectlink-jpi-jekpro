/*
 * #%L
 * prolobjectlink-jpi-jekpro
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;
import jekpro.tools.term.TermVar;

public class PrologConverterTest extends PrologBaseTest {

	private AbstractTerm[][] termTable = new AbstractTerm[7][5];
	private Map<String, AbstractTerm> termMap = new HashMap<String, AbstractTerm>();
	private PrologConverter<AbstractTerm> converter = provider.getConverter();

	@Before
	public void setUp() throws Exception {

		solution[0][0] = mcardon;
		solution[0][1] = one;
		solution[0][2] = five;
		solution[0][3] = board;
		solution[0][4] = threeThousand;

		solution[1][0] = treeman;
		solution[1][1] = two;
		solution[1][2] = three;
		solution[1][3] = human_resources;
		solution[1][4] = twoThousand;

		solution[2][0] = chapman;
		solution[2][1] = one;
		solution[2][2] = two;
		solution[2][3] = board;
		solution[2][4] = thousandFiveHundred;

		solution[3][0] = claessen;
		solution[3][1] = four;
		solution[3][2] = one;
		solution[3][3] = technical_services;
		solution[3][4] = thousand;

		solution[4][0] = petersen;
		solution[4][1] = five;
		solution[4][2] = eight;
		solution[4][3] = administration;
		solution[4][4] = fourThousandFiveHundred;

		solution[5][0] = cohn;
		solution[5][1] = one;
		solution[5][2] = seven;
		solution[5][3] = board;
		solution[5][4] = fourThousand;

		solution[6][0] = duffy;
		solution[6][1] = one;
		solution[6][2] = nine;
		solution[6][3] = board;
		solution[6][4] = fiveThousand;

		//

		termTable[0][0] = new TermAtomic("mcardon");
		termTable[0][1] = new TermAtomic(1);
		termTable[0][2] = new TermAtomic(5);
		termTable[0][3] = new TermAtomic("board");
		termTable[0][4] = new TermAtomic(3000);

		termTable[1][0] = new TermAtomic("treeman");
		termTable[1][1] = new TermAtomic(2);
		termTable[1][2] = new TermAtomic(3);
		termTable[1][3] = new TermAtomic("human_resources");
		termTable[1][4] = new TermAtomic(2000);

		termTable[2][0] = new TermAtomic("chapman");
		termTable[2][1] = new TermAtomic(1);
		termTable[2][2] = new TermAtomic(2);
		termTable[2][3] = new TermAtomic("board");
		termTable[2][4] = new TermAtomic(1500);

		termTable[3][0] = new TermAtomic("claessen");
		termTable[3][1] = new TermAtomic(4);
		termTable[3][2] = new TermAtomic(1);
		termTable[3][3] = new TermAtomic("technical_services");
		termTable[3][4] = new TermAtomic(1000);

		termTable[4][0] = new TermAtomic("petersen");
		termTable[4][1] = new TermAtomic(5);
		termTable[4][2] = new TermAtomic(8);
		termTable[4][3] = new TermAtomic("administration");
		termTable[4][4] = new TermAtomic(4500);

		termTable[5][0] = new TermAtomic("cohn");
		termTable[5][1] = new TermAtomic(1);
		termTable[5][2] = new TermAtomic(7);
		termTable[5][3] = new TermAtomic("board");
		termTable[5][4] = new TermAtomic(4000);

		termTable[6][0] = new TermAtomic("duffy");
		termTable[6][1] = new TermAtomic(1);
		termTable[6][2] = new TermAtomic(9);
		termTable[6][3] = new TermAtomic("board");
		termTable[6][4] = new TermAtomic(5000);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testToTermT() {
		assertEquals(six, converter.toTerm(new TermAtomic(6)));
		assertEquals(x, converter.toTerm(new TermVar()));
		assertEquals(cat, converter.toTerm(new TermAtomic("cat")));
		assertEquals(pi, converter.toTerm(new TermAtomic(Math.PI)));
		assertEquals(euler, converter.toTerm(new TermAtomic(Math.E)));
		assertEquals(provider.prologEmpty(), converter.toTerm(new TermAtomic("[]")));
		assertEquals(provider.newLong(1000000000), converter.toTerm(new TermAtomic(1000000000)));
		assertEquals(provider.newStructure(salary, one, thousand), converter
				.toTerm(new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) })));
		assertEquals(provider.newList(expecteds0),
				converter.toTerm(new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1),
						new TermAtomic(5), new TermAtomic("board"), new TermAtomic(3000) })));
	}

	@Test
	public final void testToTermTArray() {
		assertArrayEquals(expecteds0, converter.toTermArray(new AbstractTerm[] { new TermAtomic("mcardon"),
				new TermAtomic(1), new TermAtomic(5), new TermAtomic("board"), new TermAtomic(3000) }));
	}

	@Test
	public final void testToTermTArrayArray() {
		assertArrayEquals(solution, converter.toTermMatrix(termTable));
	}

	@Test
	public final void testToTermMapOfStringT() {

		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);

		//

		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("pam"));
		termMap.put("Y", new TermAtomic("bob"));

		assertEquals(solutionMap, converter.toTermMap(termMap));

	}

	@Test
	public final void testToTermTClassOfK() {

		// from concrete term
		assertEquals(six, converter.toTerm(new TermAtomic(6), PrologInteger.class));
		assertEquals(x, converter.toTerm(new TermVar(), PrologVariable.class));
		assertEquals(cat, converter.toTerm(new TermAtomic("cat"), PrologAtom.class));
		assertEquals(pi, converter.toTerm(new TermAtomic(Math.PI), PrologDouble.class));
		assertEquals(euler, converter.toTerm(new TermAtomic(Math.E), PrologFloat.class));
		assertEquals(provider.prologEmpty(), converter.toTerm(new TermAtomic("[]"), PrologTerm.class));
		assertEquals(provider.newLong(1000000000), converter.toTerm(new TermAtomic(1000000000), PrologLong.class));
		assertEquals(provider.newStructure(salary, one, thousand),
				converter.toTerm(
						new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) }),
						PrologStructure.class));
		assertEquals(provider.newList(expecteds0),
				converter.toTerm(new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1),
						new TermAtomic(5), new TermAtomic("board"), new TermAtomic(3000) }), PrologList.class));

		// from ancestor term class
		assertEquals(six, converter.toTerm(new TermAtomic(6), PrologTerm.class));
		assertEquals(x, converter.toTerm(new TermVar(), PrologTerm.class));
		assertEquals(cat, converter.toTerm(new TermAtomic("cat"), PrologTerm.class));
		assertEquals(pi, converter.toTerm(new TermAtomic(Math.PI), PrologTerm.class));
		assertEquals(euler, converter.toTerm(new TermAtomic(Math.E), PrologTerm.class));
		assertEquals(provider.prologEmpty(), converter.toTerm(new TermAtomic("[]"), PrologTerm.class));
		assertEquals(provider.newLong(1000000000), converter.toTerm(new TermAtomic(1000000000), PrologTerm.class));
		assertEquals(provider.newStructure(salary, one, thousand),
				converter.toTerm(
						new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) }),
						PrologTerm.class));
		assertEquals(provider.newList(expecteds0),
				converter.toTerm(new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1),
						new TermAtomic(5), new TermAtomic("board"), new TermAtomic(3000) }), PrologTerm.class));

	}

	@Test
	public final void testToTermTArrayClassOfK() {
		assertArrayEquals(expecteds0,
				converter.toTermArray(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1),
						new TermAtomic(5), new TermAtomic("board"), new TermAtomic(3000) }, PrologTerm[].class));
	}

	@Test
	public final void testToTermTArrayArrayClassOfK() {
		assertArrayEquals(solution, converter.toTermMatrix(termTable, PrologTerm[][].class));
	}

	@Test
	public final void testToTermMapOfStringTClassOfK() {

		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);

		//

		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("pam"));
		termMap.put("Y", new TermAtomic("bob"));

		assertEquals(solutionMap, converter.toTermMap(termMap, PrologTerm.class));

	}

	@Test
	public final void testToTermMapOfStringTArrayClassOfK() {

		famillyAllSolutionMap = new Map[6];
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);
		famillyAllSolutionMap[0] = solutionMap;
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", bob);
		famillyAllSolutionMap[1] = solutionMap;

		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", liz);
		famillyAllSolutionMap[2] = solutionMap;
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", ann);
		famillyAllSolutionMap[3] = solutionMap;

		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", pat);
		famillyAllSolutionMap[4] = solutionMap;
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pat);
		solutionMap.put("Y", jim);
		famillyAllSolutionMap[5] = solutionMap;

		//

		Map<String, AbstractTerm>[] termMapArray = new Map[6];
		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("pam"));
		termMap.put("Y", new TermAtomic("bob"));
		termMapArray[0] = termMap;
		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("tom"));
		termMap.put("Y", new TermAtomic("bob"));
		termMapArray[1] = termMap;

		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("tom"));
		termMap.put("Y", new TermAtomic("liz"));
		termMapArray[2] = termMap;
		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("bob"));
		termMap.put("Y", new TermAtomic("ann"));
		termMapArray[3] = termMap;

		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("bob"));
		termMap.put("Y", new TermAtomic("pat"));
		termMapArray[4] = termMap;
		termMap = new HashMap<String, AbstractTerm>();
		termMap.put("X", new TermAtomic("pat"));
		termMap.put("Y", new TermAtomic("jim"));
		termMapArray[5] = termMap;

		assertArrayEquals(famillyAllSolutionMap, converter.toTermMapArray(termMapArray, PrologTerm.class));

	}

	@Test
	public final void testFromTermPrologTerm() {

		assertEquals(new TermAtomic(6), converter.fromTerm(six));
		assertEquals(new TermVar(), converter.fromTerm(x));
		assertEquals(new TermAtomic("cat"), converter.fromTerm(cat));
		assertEquals(new TermAtomic(Math.PI), converter.fromTerm(pi));
		assertEquals(new TermAtomic(Math.E), converter.fromTerm(euler));
		assertEquals(new TermAtomic("[]"), converter.fromTerm(provider.prologEmpty()));
		assertEquals(new TermAtomic(1000000000), converter.fromTerm(provider.newLong(1000000000)));
		assertEquals(new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) }),
				converter.fromTerm(provider.newStructure(salary, one, thousand)));
		assertEquals(
				new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1), new TermAtomic(5),
						new TermAtomic("board"), new TermAtomic(3000) }),
				converter.fromTerm(provider.newList(expecteds0)));
	}

	@Test
	public final void testFromTermPrologTermArray() {
		assertArrayEquals(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1), new TermAtomic(5),
				new TermAtomic("board"), new TermAtomic(3000) }, converter.fromTermArray(expecteds0));
	}

	@Test
	public final void testFromTermPrologTermPrologTermArray() {
		assertEquals(
				new TermCompound(":-", new TermAtomic("mcardon"),
						new TermCompound(",", new TermAtomic("mcardon"), new TermCompound(",", new TermAtomic(1),
								new TermCompound(",", new TermAtomic(5),
										new TermCompound(",", new TermAtomic("board"), new TermAtomic(3000)))))),
				converter.fromTerm(mcardon, expecteds0));
		assertEquals(new TermAtomic("mcardon"), converter.fromTerm(mcardon, (PrologTerm[]) null));
		assertEquals(new TermAtomic("mcardon"), converter.fromTerm(mcardon, new PrologTerm[0]));
	}

	@Test
	public final void testFromTermPrologTermClassOfK() {

		// from concrete term
		assertEquals(new TermAtomic(6), converter.fromTerm(six, TermAtomic.class));
		assertEquals(new TermVar(), converter.fromTerm(x, TermVar.class));
		assertEquals(new TermAtomic("cat"), converter.fromTerm(cat, TermAtomic.class));
		assertEquals(new TermAtomic(Math.PI), converter.fromTerm(pi, TermAtomic.class));
		assertEquals(new TermAtomic(Math.E), converter.fromTerm(euler, TermAtomic.class));
		assertEquals(new TermAtomic("[]"), converter.fromTerm(provider.prologEmpty(), AbstractTerm.class));
		assertEquals(new TermAtomic(1000000000), converter.fromTerm(provider.newLong(1000000000), TermAtomic.class));
		assertEquals(new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) }),
				converter.fromTerm(provider.newStructure(salary, one, thousand), TermAtomic.class));
		assertEquals(
				new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1), new TermAtomic(5),
						new TermAtomic("board"), new TermAtomic(3000) }),
				converter.fromTerm(provider.newList(expecteds0), TermAtomic.class));

		// from ancestor term class
		assertEquals(new TermAtomic(6), converter.fromTerm(six, AbstractTerm.class));
		assertEquals(new TermVar(), converter.fromTerm(x, AbstractTerm.class));
		assertEquals(new TermAtomic("cat"), converter.fromTerm(cat, AbstractTerm.class));
		assertEquals(new TermAtomic(Math.PI), converter.fromTerm(pi, AbstractTerm.class));
		assertEquals(new TermAtomic(Math.E), converter.fromTerm(euler, AbstractTerm.class));
		assertEquals(new TermAtomic("[]"), converter.fromTerm(provider.prologEmpty(), AbstractTerm.class));
		assertEquals(new TermAtomic(1000000000), converter.fromTerm(provider.newLong(1000000000), AbstractTerm.class));
		assertEquals(new TermCompound(salary, new AbstractTerm[] { new TermAtomic(1), new TermAtomic(1000) }),
				converter.fromTerm(provider.newStructure(salary, one, thousand), AbstractTerm.class));
		assertEquals(
				new TermAtomic(new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1), new TermAtomic(5),
						new TermAtomic("board"), new TermAtomic(3000) }),
				converter.fromTerm(provider.newList(expecteds0), AbstractTerm.class));

	}

	@Test
	public final void testFromTermPrologTermArrayClassOfK() {
		assertArrayEquals(
				new AbstractTerm[] { new TermAtomic("mcardon"), new TermAtomic(1), new TermAtomic(5),
						new TermAtomic("board"), new TermAtomic(3000) },
				converter.fromTermArray(expecteds0, AbstractTerm[].class));
	}

	@Test
	public final void testFromTermPrologTermPrologTermArrayClassOfK() {
		assertEquals(
				new TermCompound(":-", new TermAtomic("mcardon"),
						new TermCompound(",", new TermAtomic("mcardon"), new TermCompound(",", new TermAtomic(1),
								new TermCompound(",", new TermAtomic(5),
										new TermCompound(",", new TermAtomic("board"), new TermAtomic(3000)))))),
				converter.fromTerm(mcardon, expecteds0, AbstractTerm.class));
	}

	@Test
	public final void testCreateProvider() {
		assertNotNull(converter.createProvider());
	}

	@Test
	public final void testToString() {
		assertEquals("JekejekePrologConverter", converter.toString());
	}

}

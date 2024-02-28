/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PersonTest {

	Person p;
	@BeforeEach
	public void createGenericPerson() {
		p = new Person("f", "l",
				"email@email.com", "NL32323232423");
	}
	@Test
	public void checkConstructor() {
		assertEquals("f", p.getFirstName());
		assertEquals("l", p.getLastName());
		assertEquals("email@email.com", p.getEmail());
		assertEquals(0.0, p.getTotalExpenses());
		assertEquals(new ArrayList<>(), p.getExpenseList());
		assertEquals("NL32323232423", p.getIBAN());
	}

	@Test
	public void checkFirstNameSetter() {
		assertEquals("f", p.getFirstName());
		p.setFirstName("John");
		assertEquals("John", p.getFirstName());
	}

	@Test
	public void checkLastNameSetter() {
		assertEquals("l", p.getLastName());
		p.setLastName("Doe");
		assertEquals("Doe", p.getLastName());
	}

	@Test
	public void checkAddExpense() {
		p.addExpense(3.0);
		assertEquals(3.0, p.getTotalExpenses());
		assertEquals(1, p.getExpenseList().size());
	}

	@Test
	public void checkRemoveExpense() {
		p.addExpense(3.0);
		p.addExpense(7.0);
        p.removeExpense(3.0);
		assertEquals(7.0, p.getTotalExpenses());
		assertEquals(1, p.getExpenseList().size());
	}

	@Test
	public void checkIBANSetter() {
		assertEquals("NL32323232423", p.getIBAN());
		p.setIBAN("NL32323232424");
		assertEquals("NL32323232424", p.getIBAN());
	}
	@Test
	public void equalsHashCode() {
		var a = new Person("a", "b",
				"email@email.com", "NL32323232423");
		var b =  new Person("a", "b",
				"email@email.com", "NL32323232423");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void notEqualsHashCode() {
		var a =  new Person("a", "b",
				"email@email.com", "NL32323232423");
		var b =  new Person("a", "c",
				"email@email.com", "NL32323232423");
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void hasToString() {
		var actual =  new Person("a", "b",
				"email@email.com", "NL32323232423").toString();
		assertTrue(actual.contains(Person.class.getSimpleName()));
		assertTrue(actual.contains("\n"));
		assertTrue(actual.contains("firstName"));
	}
}
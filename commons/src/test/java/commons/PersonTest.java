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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PersonTest {

	@Test
	public void checkConstructor() {
		var p = new Person("f", "l", 15,
				new ArrayList<>(List.of(7, 8)));
		assertEquals("f", p.getFirstName());
		assertEquals("l", p.getLastName());
		assertEquals(15, p.getTotalExpenses());
	}

	@Test
	public void equalsHashCode() {
		var a = new Person("a", "b", 15,
				new ArrayList<>(List.of(7, 8)));
		var b = new Person("a", "b", 15,
				new ArrayList<>(List.of(7, 8)));
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void notEqualsHashCode() {
		var a = new Person("a", "b", 15,
				new ArrayList<>(List.of(7, 8)));
		var b = new Person("a", "c", 15,
				new ArrayList<>(List.of(7, 8)));
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void hasToString() {
		var actual = new Person("a", "b", 15,
				new ArrayList<>(List.of(7, 8))).toString();
		assertTrue(actual.contains(Person.class.getSimpleName()));
		assertTrue(actual.contains("\n"));
		assertTrue(actual.contains("firstName"));
	}
}
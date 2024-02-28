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

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstName;
	private String lastName;
	private String IBAN;
	private String BIC;
	private double totalDebt;
	@OneToMany
	private List<Debt> debtList;
	private String email;
	private Currency preferredCurrency;
	@ManyToOne
	private Event event;
	@SuppressWarnings("unused")
	protected Person() {
		// for object mapper
	}
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public Person(String firstName, String lastName,
				  String email, String IBAN, String BIC, Event event) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.totalDebt = 0.0;
		this.debtList = new ArrayList<>();
		this.IBAN = IBAN;
		this.BIC = BIC;
		this.event = event;
		this.preferredCurrency = Currency.EUR;
	}
	public Person(String firstName, String lastName,
				  String email, String IBAN, String BIC) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.totalDebt = 0.0;
		this.debtList = new ArrayList<>();
		this.IBAN = IBAN;
		this.BIC = BIC;
		this.event = new Event();
		this.preferredCurrency = Currency.EUR;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getTotalDebt() {
		return totalDebt;
	}
	public void setTotalDebt(double totalDebt) {
		this.totalDebt = totalDebt;
	}
	public List<Debt> getDebtList() {
		return debtList;
	}
	public void setDebtList(List<Debt> debtList) {
		this.debtList = debtList;
	}
	public String getIBAN() {
		return IBAN;
	}
	public void setIBAN(String IBAN) {
		this.IBAN = IBAN;
	}
	public String getBIC() {
		return BIC;
	}
	public void setBIC(String BIC) {
		this.BIC = BIC;
	}
	public void addDebt(Debt debt) {
		totalDebt += debt.getAmount();
		debtList.add(debt);
		debt.setGiver(this);
	}
	public void removeDebt(Debt debt) {
		if (!debtList.contains(debt))
			return;
		totalDebt -= debt.getAmount();
		debtList.remove(debt);
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Currency getPreferredCurrency() {
		return preferredCurrency;
	}
	public void setPreferredCurrency(Currency currency) {
		this.preferredCurrency = currency;
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
	}


}
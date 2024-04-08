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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "PERSON")
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="PERSON_ID")
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PERSON_ID")
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private java.util.Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false, updatable = true)
	private java.util.Date updatedAt;

	/**
	 * Upon initial creation of the Object, the Date will be stored.
	 */
	@PrePersist
	public void onCreate() {
		this.createdAt = new java.util.Date();
		this.updatedAt = createdAt;
	}

	/**
	 * Upon update, updated the Object's update date. A lot of updates here :)
	 */
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = new java.util.Date();
	}
	@Column(name = "bic")
	private String BIC;
	@Column(name = "iban")
	private String IBAN;
	@Column(name = "email")
	private String email;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "preferredCurrency")
	private Currency preferredCurrency;
	@Column(name = "totalDebt")
	private double totalDebt;

	@ManyToOne
	@JoinColumn(name = "EVENT_ID")
	private Event event;

	@ManyToOne
	//@JsonBackReference
	private User user;

	@OneToMany
	//@JsonManagedReference
	private List<Debt> debtList;
	@SuppressWarnings("unused")
	public Person() {
		// for object mapper
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(String firstName, String lastName, String email, String IBAN, String BIC,
				  Currency preferredCurrency, double totalDebt, Event event, User user) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.totalDebt = totalDebt;
		this.debtList = new ArrayList<>();
		this.IBAN = IBAN;
		this.BIC = BIC;
		this.event = event;
		this.preferredCurrency = preferredCurrency;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	/**
	 * Method that returns creation time of the object.
	 * @return Util Date
	 */
	public java.util.Date getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Method that returns update time of the object.
	 * @return Util Date
	 */
	public java.util.Date getUpdatedAt() {
		return this.updatedAt;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void addDebt(Debt debt) {
		if (this.equals(debt.getGiver())) {
			totalDebt += debt.getAmount();
		}
		else {
			totalDebt -= debt.getAmount();
		}
		debtList.add(debt);
		debt.setGiver(this);
	}
	public void removeDebt(Debt debt) {
		if (!debtList.contains(debt))
			return;
		if (debt.getGiver().equals(this)) {
			totalDebt -= debt.getAmount();
		} else {
			totalDebt += debt.getAmount();
		}
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

	public void setId(long id) {
		this.id = id;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == this.getClass()) {
			Person other = (Person) obj;
			if (other.getId() == this.getId()) {
				return true;
			}
		}
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
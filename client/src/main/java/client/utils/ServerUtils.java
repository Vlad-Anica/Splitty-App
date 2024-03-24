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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI("http://localhost:8080/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public List<Quote> getQuotes() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
	}

	public Quote addQuote(Quote quote) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
	}

	public List<Person> getPersons() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/persons") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Person>>(){});
	}

	public Person getPersonById(long id) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/persons/" + id) //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<Person>(){});
	}
	public Person addPerson(Person person) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonPerson = objectMapper.writeValueAsString(person);
			System.out.println("Received Person object: " + jsonPerson);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/persons")//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonPerson, MediaType.APPLICATION_JSON), Person.class);

		} catch (JsonProcessingException e) {
            e.printStackTrace();
			return null;
        }
	}
   public Person updatePerson(Long id, Person updatedPerson) {
	   try {
		   ObjectMapper objectMapper = new ObjectMapper();
		   String jsonPerson = objectMapper.writeValueAsString(updatedPerson);
		   System.out.println("Received Person object: " + jsonPerson);

		   return ClientBuilder.newClient(new ClientConfig())//
				   .target(SERVER).path("api/persons/" + id)//
				   .request(APPLICATION_JSON)//
				   .accept(APPLICATION_JSON)//
				   .put(Entity.entity(jsonPerson, MediaType.APPLICATION_JSON), Person.class);

	   } catch (JsonProcessingException e) {
		   e.printStackTrace();
		   return null;
	   }
   }
	public List<Debt> getDebts() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/debts") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Debt>>(){});
	}

	public Debt addDebt(Debt debt) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDebt = objectMapper.writeValueAsString(debt);
			System.out.println("Received object: " + jsonDebt);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts")//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonDebt, MediaType.APPLICATION_JSON), Debt.class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
    public Debt addDebtToPerson(Long personId, Debt debt) {
		try {
			Person person = getPersonById(personId);
			person.getDebtList().add(debt);

			double totalDebt = person.getTotalDebt() + debt.getAmount();
			person.setTotalDebt(totalDebt);
            updatePerson(personId, person);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDebt = objectMapper.writeValueAsString(debt);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts")//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonDebt, MediaType.APPLICATION_JSON), Debt.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

	}
	public Debt settleDebt(long id) {
		try {
			Debt debt = ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts/" + id)//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.get(new GenericType<Debt>() {
					});

			debt.setSettled(true);

			Person giver = getPersonById(debt.getGiver().getId());
			giver.getDebtList().remove(debt);

			double totalDebt = giver.getTotalDebt() - debt.getAmount();
			giver.setTotalDebt(totalDebt >= 0 ? totalDebt : 0);
			updatePerson(giver.getId(), giver);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDebt = objectMapper.writeValueAsString(debt);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts/" + id)//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.put(Entity.entity(jsonDebt, MediaType.APPLICATION_JSON), Debt.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Debt payDebtPartially(long id, double amount) {

		try {
			Debt debt = ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts/" + id)//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.get(new GenericType<Debt>() {
					});

			double totalAmount = debt.getAmount() - amount;
			debt.setAmount(totalAmount >= 0 ? totalAmount : 0);

			if (debt.getAmount() <= 0)
				return settleDebt(id);

			Person giver = getPersonById(debt.getGiver().getId());
			double totalDebt = giver.getTotalDebt() - amount;
			giver.setTotalDebt(totalDebt);

			updatePerson(giver.getId(), giver);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDebt = objectMapper.writeValueAsString(debt);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts/" + id)//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.put(Entity.entity(jsonDebt, MediaType.APPLICATION_JSON), Debt.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Event> getEvents(Long userId) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("user/events")
				.queryParam("userId", userId)//
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>(){});
	}

	/**
	 * sends a request to the server to check if the password is correct
	 * @param password the password
	 * @return if the password is correct
	 */
	public boolean checkAdminPassword(String password) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("api/admin/checkPassword")
				.request(APPLICATION_JSON)
				.post(Entity.entity(password, APPLICATION_JSON), Boolean.class);
	}

	public Tag addTag(Tag tag) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonTag = objectMapper.writeValueAsString(tag);
			System.out.println("Received object: " + jsonTag);

			return ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts")//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonTag, MediaType.APPLICATION_JSON), Tag.class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Expense> getExpensesForUser(long userId) {

		return ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("user/" + userId + "/expenses")//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.get(new GenericType<List<Expense>>(){});
	}
	public User addUser(String firstName, String lastName, String email, Currency preferredCurency){

		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("/user/")
				.queryParam("firstName", firstName)
				.queryParam("lastName", lastName)
				.queryParam("email", email)
				.queryParam("currency", preferredCurency)
				.request(APPLICATION_JSON)
				.post(Entity.entity(new User(firstName,lastName,email,preferredCurency), APPLICATION_JSON), User.class);
	}
}
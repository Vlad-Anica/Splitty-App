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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import commons.Event;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

public class ServerUtils {

	private static String SERVER = "http://localhost:8080/";

	private static String WEBSOCKETSERVER = "ws://localhost:8080/websocket";

	private static final int NO_CONTENT_STATUS = 204;


	public void setSERVER(String server) {
		SERVER = server;
	}
	public String getSERVER() {
		return SERVER;
	}

	public String getWEBSOCKETSERVER() {
		return WEBSOCKETSERVER;
	}
	public void setWEBSOCKETSERVER(String ws) {
		WEBSOCKETSERVER = ws;
	}
	public boolean isOnline(String SERVER_IP_ADDRESS, Integer PORT) {
		boolean b = true;
		try{
			InetSocketAddress sa = new InetSocketAddress(SERVER_IP_ADDRESS, PORT);
			Socket ss = new Socket();
			ss.connect(sa, 10);
			ss.close();
		}catch(Exception e) {
			b = false;
		}
		return b;
	}

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

	public Event updateEvent(Long id, Event newEvent) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonEvent = objectMapper.writeValueAsString(newEvent);
			System.out.println("Received Event object: " + jsonEvent);
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("api/events/" + id)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(jsonEvent, MediaType.APPLICATION_JSON), Event.class);
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

   public void deletePerson(Long id) {

	   ClientBuilder.newClient(new ClientConfig())
			   .target(SERVER).path("api/persons/" + id)
			   .request(APPLICATION_JSON)
			   .accept(APPLICATION_JSON)
			   .delete();
   }
	public List<Debt> getDebts() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/debts") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Debt>>(){});
	}
	public List<Debt> getDebtsByUserId(long id) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/debts/user/"+id) //
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
			person.addDebt(debt);
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
			giver.removeDebt(debt);
			Person receiver = getPersonById(debt.getReceiver().getId());
			receiver.removeDebt(debt);
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

		//try {
			Debt debt = ClientBuilder.newClient(new ClientConfig())//
					.target(SERVER).path("api/debts/" + id)//
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.get(new GenericType<Debt>() {
					});

			Event event = debt.getEvent();
			Person giver = getPersonById(debt.getGiver().getId());
			Person receiver = getPersonById(debt.getReceiver().getId());
			giver.removeDebt(debt);
			receiver.removeDebt(debt);
			event.removeDebt(debt);

			double totalAmount = debt.getAmount() - amount;
			debt.setAmount(totalAmount >= 0 ? totalAmount : 0);

			if (debt.getAmount() > 0) {
				giver.addDebt(debt);
				receiver.addDebt(debt);
				event.addDebt(debt);
			}

			updatePerson(giver.getId(), giver);
			updatePerson(receiver.getId(), receiver);
			updateEvent(event.getId(), event);
			return debt;

//			ObjectMapper objectMapper = new ObjectMapper();
//			String jsonDebt = objectMapper.writeValueAsString(debt);
////
//			return ClientBuilder.newClient(new ClientConfig())//
//					.target(SERVER).path("api/debts/" + id)//
//					.request(APPLICATION_JSON)//
//					.accept(APPLICATION_JSON)//
//					.put(Entity.entity(jsonDebt, MediaType.APPLICATION_JSON), Debt.class);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	public List<Event> getEvents(Long userId) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("user/" + userId + "/events")
				.queryParam("userId", userId)//
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>(){});
	}
	public List<Event> getEventsOrderedByLastModificationDate() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/admin/eventsOrderedByLastModificationDate")
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>(){});
	}

	public List<Event> getEventsOrderedByCreationDate() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/admin/eventsOrderedByCreationDate")
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>(){});
	}

	public List<Event> getEventsOrderedByName() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/admin/eventsOrderedByName")
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>(){});
	}

	/**
	 * gets all events
	 */
	public List<Event> getAllEvents() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/events")
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
					.target(SERVER).path("api/tags")//
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

	public Expense getExpenseById(long expenseId){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("/api/expenses/" + expenseId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<Expense>(){});
	}


	public User addUser(String firstName, String lastName, String email, Currency preferredCurency){
		System.out.println("Server: " + getSERVER());

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

	public User saveUser(User user){
		System.out.println("Server: " + getSERVER());

		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("/user/add")
				.request(APPLICATION_JSON)
				.post(Entity.entity(user, APPLICATION_JSON), User.class);
	}

	/**
	 * Returns an Event Object associated with the iD if possible. TO-DO FINISH
	 * @param eventID long, representing the Event's ID
	 * @return Event associated with the ID
	 */
	public Event getEvent(Long eventID) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER)
					.path("api/events/" + eventID)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<Event>(){});
		} catch (BadRequestException e) {
			return null;
		}

	}

	public Event getEventByInviteCode(String inviteCode) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("api/events/inviteCode/" + inviteCode)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<Event>(){});
	}

	public User getUserById(Long id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("user/" + id.toString())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<User>(){});
	}

	public Event updateEvent(Event newEvent) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonEvent = objectMapper.writeValueAsString(newEvent);
			System.out.println("Received Event object: " + jsonEvent);

			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("api/events/persist")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(jsonEvent, MediaType.APPLICATION_JSON), Event.class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Event createEvent(Event event) {

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonEvent = objectMapper.writeValueAsString(event);
			System.out.println("Received object: " + jsonEvent);
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("/api/events")
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonEvent, MediaType.APPLICATION_JSON), Event.class);

		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/***
	 * Adds an expense to the database
	 * @param expense Expense to be added to DB
	 * @return the added expense
	 */
	public Expense createExpense(Expense expense) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonExpense = objectMapper.writeValueAsString(expense);
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("/api/expenses")
					.request(APPLICATION_JSON)//
					.accept(APPLICATION_JSON)//
					.post(Entity.entity(jsonExpense, MediaType.APPLICATION_JSON), Expense.class);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
			return null;
        }
    }

	/***
	 *
	 * @param newExpense expense to be updated
	 * @return new expense
	 */
	public Expense updateExpense(Long id, Expense newExpense) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonExpense = objectMapper.writeValueAsString(newExpense);
			System.out.println("Received Expense object: " + jsonExpense);

			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("api/expenses/" + id)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(jsonExpense, MediaType.APPLICATION_JSON), Expense.class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteExpense(Long id) {

		ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/expenses/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}
	/***
	 *
	 * @param eventId the id of the event to find in the DB
	 * @param expense expense to be added to event
	 * @return updated event
	 * @throws JsonProcessingException
	 */
	public Event addExpenseToEvent(long eventId, Expense expense) throws JsonProcessingException {

		try {
			Event event = getEvent(eventId);
			event.addExpense(expense);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonEvent = objectMapper.writeValueAsString(expense);
			System.out.println("Received Event object: " + jsonEvent);
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER).path("api/events/" + eventId + "/newExpense")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(jsonEvent, MediaType.APPLICATION_JSON), Event.class);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage() + " " + e.getCause());
			return null;
		}
	}

	private static StompSession session;

	private StompSession connect(String URL) {

		var client = new StandardWebSocketClient();
		var stomp = new WebSocketStompClient(client);
		stomp.setMessageConverter(new MappingJackson2MessageConverter());
		try {
            return stomp.connectAsync(URL, new StompSessionHandlerAdapter() {
			}).get();
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
        }
        return null;
	}
	public void startConnection(){
		System.out.println(WEBSOCKETSERVER);
		session = connect(WEBSOCKETSERVER);
	}

	public <T> void registerForAddition(String destination, Class<T> type, Consumer<T> consumer) {
		session.subscribe(destination, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return type;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((T) payload);
			}
		});
	}

	public <T> void registerForUpdate(String destination, Class <T> type, Consumer<T> consumer) {
		session.subscribe(destination, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return type;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((T) payload);
			}
		});
	}
	public void send(String destination, Object o) {
		session.send(destination, o);
}
	private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
	public void registerForUpdates(Consumer<Event> consumer) {
		EXEC.submit(() -> {
			while (!Thread.interrupted()) {
				Response res = ClientBuilder.newClient(new ClientConfig()) //
						.target(SERVER).path("/api/events/updates") //
						.request(APPLICATION_JSON) //
						.accept(APPLICATION_JSON) //
						.get(Response.class);
				if (res.getStatus() == NO_CONTENT_STATUS) {
					continue;
				}
				Event event = (Event) res.readEntity(Event.class);
				consumer.accept(event);
			}
		});

	}

	public void stop() {
		EXEC.shutdownNow();
	}
	public void deleteEventById(Long eventId) {
		ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + eventId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}

	public void downloadJSONDump(Long eventId) throws URISyntaxException, IOException {

		Client client = ClientBuilder.newClient();
		Response res = client
				.target(SERVER).path("api/events/" + eventId + "/download")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get();
		if (res.getStatus() == Response.Status.OK.getStatusCode()) {
			Desktop.getDesktop().browse(new URI(SERVER + "api/events/" + eventId + "/download"));
		}
		else {
			System.out.println(res.getStatus());
		}

		res.close();
		client.close();
	}

	public void downloadLanguageTemplate() throws URISyntaxException, IOException {

		Client client = ClientBuilder.newClient();
		Response res = client.target(SERVER).path("api/downloads/languageTemplate")
				.request(TEXT_PLAIN)
				.accept(TEXT_PLAIN)
				.get();

		if (res.getStatus() == Response.Status.OK.getStatusCode()) {
			Desktop.getDesktop().browse(new URI(SERVER + "api/downloads/languageTemplate"));
		}
		else {
			System.out.println(res.getStatus());
		}

		res.close();
		client.close();
	}
	public List<Person> getPersonsByUserId(long id){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER)
				.path("user/person/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<>(){});

	}
}

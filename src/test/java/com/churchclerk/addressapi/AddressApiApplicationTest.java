/*
 */
package com.churchclerk.addressapi;


import com.churchclerk.addressapi.api.AddressApi;
import com.churchclerk.addressapi.model.Address;
import com.churchclerk.addressapi.storage.AddressEntity;
import com.churchclerk.securityapi.SecurityApi;
import com.churchclerk.securityapi.SecurityToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;

import java.net.Inet4Address;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressApiApplicationTest {

	private static final String 	TOKEN_PREFIX 	= "Bearer ";
	private static final String 	HEADER_AUTH 	= "Authorization";


	@LocalServerPort
	private int 		port;

	@Value("${jwt.secret}")
	private String		testSecret;

	@Autowired
	private AddressApi	api;

	@Autowired
	private TestRestTemplate	restTemplate;

	private SecurityToken		testToken;

	private HttpHeaders 		testHeaders;

	@BeforeEach
	public void setupMock() {

		try {
			if (createToken("test", Inet4Address.getLoopbackAddress().getHostAddress()) == false) {
				throw new RuntimeException("Error creating security token");
			}

			testHeaders = new HttpHeaders();
			testHeaders.add(HEADER_AUTH, TOKEN_PREFIX+testToken.getJwt());
			testHeaders.add("Content-Type", "application/json");
		}
		catch (Exception e) {
			throw new RuntimeException("Error creating security token", e);
		}
	}

	private boolean createToken(String id, String location) {
		testToken = new SecurityToken();

		testToken.setId(id);
		testToken.setLocation(location);
		testToken.setSecret(testSecret);

		return SecurityApi.process(testToken);
	}

	@Test
	@Order(0)
	public void contexLoads() throws Exception {
		Assertions.assertThat(api).isNotNull();
	}

	@Test
	@Order(1)
	public void testGetResources() throws Exception {

		getResourcesAndCheck(createUrl(), 0L);
	}

	private String createUrl() {
		return createUrl(null);
	}

	private String createUrl(UUID id) {
		StringBuffer	buffer = new StringBuffer("http://localhost:");

		buffer.append(port);
		buffer.append("/api/address");
		if (id != null) {
			buffer.append("/");
			buffer.append(id);
		}

		return buffer.toString();
	}

	private JsonObject getResourcesAndCheck(String url, long count) {

		HttpEntity<String>		entity 		= new HttpEntity<String>(testHeaders);
		ResponseEntity<String>	response	= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		JsonObject page = new Gson().fromJson(response.getBody(), JsonObject.class);

		Assertions.assertThat(page.get("numberOfElements").getAsLong()).isEqualTo(count);
		return page;
	}

	@Test
	@Order(2)
	public void testPostResource() throws Exception {

		Address	testdata = createAddress(1000);

		createResourceAndCheck(testdata);
	}

	private Address createAddress(int number) {
		Address address = new Address();

		address.setStreet(number + " Test Street");
		address.setCity("Test City");
		address.setState("Test State");
		address.setZip("" + number);
		address.setCountry("Test Country");
		address.setActive(true);

		return address;
	}

	/**
	 *
	 * @param expected
	 * @return posted resource
	 */
	private Address createResourceAndCheck(Address expected) {

		HttpEntity<Address>		entity 		= new HttpEntity<Address>(expected, testHeaders);
		ResponseEntity<Address>	response	= restTemplate.exchange(createUrl(), HttpMethod.POST, entity, Address.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Address	actual = response.getBody();

		Assertions.assertThat(actual).isNotNull();

		Assertions.assertThat(actual.getId()).isNotNull();
		Assertions.assertThat(actual.isActive()).isEqualTo(expected.isActive());
		Assertions.assertThat(actual.getCreatedDate()).isNotNull();
		Assertions.assertThat(actual.getCreatedBy()).isNotNull();
		Assertions.assertThat(actual.getUpdatedDate()).isNotNull();
		Assertions.assertThat(actual.getUpdatedBy()).isNotNull();

		Assertions.assertThat(actual.getStreet()).isEqualTo(expected.getStreet());
		Assertions.assertThat(actual.getCity()).isEqualTo(expected.getCity());
		Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
		Assertions.assertThat(actual.getZip()).isEqualTo(expected.getZip());
		Assertions.assertThat(actual.getCountry()).isEqualTo(expected.getCountry());

		return actual;
	}

	@Test
	@Order(3)
	public void testGetResource() throws Exception {

		Address	testdata 	= createAddress(1001);
		Address	expected	= createResourceAndCheck(testdata);

		HttpEntity<Address>		entity 		= new HttpEntity<Address>(testHeaders);
		ResponseEntity<Address>	response	= restTemplate.exchange(createUrl(expected.getId()), HttpMethod.GET, entity, Address.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Address	actual = response.getBody();

		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Order(4)
	public void testUpdateResource() throws Exception {

		Address	testdata 	= createAddress(1002);
		Address	expected	= createResourceAndCheck(testdata);

		expected.setActive(false);
		HttpEntity<Address>		entity 		= new HttpEntity<Address>(expected, testHeaders);
		ResponseEntity<Address>	response	= restTemplate.exchange(createUrl(expected.getId()), HttpMethod.PUT, entity, Address.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Address	actual = response.getBody();

		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getUpdatedDate()).isAfterOrEqualTo(expected.getUpdatedDate());

		expected.setUpdatedDate(actual.getUpdatedDate());
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Order(5)
	public void testDeleteResource() throws Exception {

		Address	testdata 	= createAddress(1003);
		Address	expected	= createResourceAndCheck(testdata);

		// delete
		HttpEntity<Address>		entity 		= new HttpEntity<Address>(testHeaders);
		ResponseEntity<Address>	response	= restTemplate.exchange(createUrl(expected.getId()), HttpMethod.DELETE, entity, Address.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// try getting the deleted resource
		HttpEntity<Address>		entity2 	= new HttpEntity<Address>(testHeaders);
		ResponseEntity<Address>	response2	= restTemplate.exchange(createUrl(expected.getId()), HttpMethod.DELETE, entity2, Address.class);

		Assertions.assertThat(response2).isNotNull();
		Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@Order(6)
	public void testGetResourcesPagination() throws Exception {

		createResourceAndCheck(createAddress(1004));
		createResourceAndCheck(createAddress(1005));

		getResourcesAndCheck(createPaginationUrl(0, 1), 1L);
		getResourcesAndCheck(createPaginationUrl(0, 2), 2L);
		getResourcesAndCheck(createPaginationUrl(1, 1), 1L);
		getResourcesAndCheck(createPaginationUrl(9, 5), 0L);
	}

	private String createPaginationUrl(int page, int size) {
		StringBuffer buffer = new StringBuffer(createUrl());

		buffer.append("?page=").append(page);
		buffer.append("&size=").append(size);

		return buffer.toString();
	}

	@Test
	@Order(7)
	public void testGetResourcesFilter() throws Exception {

		createResourceAndCheck(createAddress(1006));
		createResourceAndCheck(createAddress(1007));

		getResourcesAndCheck(createFilterUrl("street", "1006%"), 1L);
		getResourcesAndCheck(createFilterUrl("zip", "1007"), 1L);
		getResourcesAndCheck(createFilterUrl("state", "Test%"), 6L);
	}

	private String createFilterUrl(String field, String value) {
		StringBuffer buffer = new StringBuffer(createUrl());

		buffer.append("?");
		buffer.append(field);
		buffer.append("=");
		buffer.append(value);

		return buffer.toString();
	}

	@Test
	@Order(8)
	public void testGetResourcesSort() throws Exception {

		createResourceAndCheck(createAddress(1008));
		createResourceAndCheck(createAddress(1009));

		getResourcesAndCheck(createSortUrl("street"), 8L, Sort.Direction.ASC);
		getResourcesAndCheck(createSortUrl("-street"), 8L, Sort.Direction.DESC);
	}

	private String createSortUrl(String keys) {
		StringBuffer buffer = new StringBuffer(createUrl());

		buffer.append("?sortBy=");
		buffer.append(keys);

		return buffer.toString();
	}

	private void getResourcesAndCheck(String url, long count, final Sort.Direction dir) {
		JsonObject 	page 		= getResourcesAndCheck(url, count);
		JsonArray	content		= page.getAsJsonArray("content");
		String 		previous 	= null;

		Iterator<JsonElement> iter = content.iterator();

		while (iter.hasNext()) {
			String street = iter.next().getAsJsonObject().get("street").getAsString();

			if (previous != null) {
				if (dir.equals(Sort.Direction.ASC)) {
					Assertions.assertThat(street).isGreaterThanOrEqualTo(previous);
				}
				else {
					Assertions.assertThat(street).isLessThanOrEqualTo(previous);
				}
			}
			previous = street;
		}
	}
}

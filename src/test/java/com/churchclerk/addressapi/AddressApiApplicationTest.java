package com.churchclerk.addressapi;


import com.churchclerk.addressapi.api.AddressApi;
import com.churchclerk.addressapi.model.Address;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressApiApplicationTest {

	@Autowired
	private AddressApi api;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

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
		Address actual = restTemplate.postForObject(createUrl(), expected, Address.class);

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

		Address actual = restTemplate.getForObject(createUrl(expected.getId()), Address.class);

		Assertions.assertThat(actual).isNotNull();

		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Order(4)
	public void testUpdateResource() throws Exception {

		Address	testdata 	= createAddress(1002);
		Address	expected	= createResourceAndCheck(testdata);

		expected.setActive(false);
		restTemplate.put(createUrl(expected.getId()), expected);

		Address actual = restTemplate.getForObject(createUrl(expected.getId()), Address.class);

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

		restTemplate.delete(createUrl(expected.getId()));

		ResponseEntity<Address> actual = restTemplate.getForEntity(createUrl(expected.getId()), Address.class);

		Assertions.assertThat(actual.getStatusCode().value()).isEqualTo(500);
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

	private void getResourcesAndCheck(String url, long count) {
		String response = restTemplate.getForObject(url, String.class);

		Assertions.assertThat(response).isNotNull();

		JsonObject page = new Gson().fromJson(response, JsonObject.class);

		Assertions.assertThat(page.get("numberOfElements").getAsLong()).isEqualTo(count);
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
}

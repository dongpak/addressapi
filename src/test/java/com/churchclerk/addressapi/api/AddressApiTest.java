/*
 */
package com.churchclerk.addressapi.api;


import com.churchclerk.addressapi.model.Address;
import com.churchclerk.addressapi.service.AddressService;
import com.churchclerk.addressapi.storage.AddressEntity;
import com.churchclerk.addressapi.storage.AddressStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 *
 */
@SpringBootTest
@TestPropertySource(locations="classpath:application-mock.properties")
public class AddressApiTest {

	@InjectMocks
	private AddressApi		testObject;

	@Mock
	private AddressService	testService;

	private Date			testDate;
	private String			testId;
	private Address 		testResource;
	private AddressEntity	testEntity;


	@BeforeEach
	public void setupMock() {
		testDate		= new Date();
		testId			= UUID.randomUUID().toString();
		testResource	= new Address();
		testEntity		= new AddressEntity();
	}

	@Test
	@Order(0)
	public void contexLoads() throws Exception {
		Assertions.assertThat(testObject).isNotNull();
	}

	@Test
	public void testGetResources() throws Exception {
		ReflectionTestUtils.setField(testObject, "sortBy", "street");

		Mockito.when(testService.getResources(null, null)).thenReturn(null);

		Response response = testObject.getResources();

		Assertions.assertThat(response.getEntity()).isNull();
	}


	@Test
	public void testGetResource() throws Exception {

		ReflectionTestUtils.setField(testObject, "id", testId);

		Mockito.when(testService.getResource(testId)).thenReturn(null);

		Response response = testObject.getResource();

		Assertions.assertThat(response.getEntity()).isNull();
	}

	@Test
	public void testCreateResource() throws Exception {

		Mockito.when(testService.createResource(testResource)).thenReturn(testResource);

		Response response = testObject.createResource(testResource);

		Assertions.assertThat(response.getEntity()).isNotNull();
		Assertions.assertThat(response.getEntity()).isInstanceOf(Address.class);

		Address actual = (Address) response.getEntity();
		Assertions.assertThat(actual.getId()).isNotNull();
		Assertions.assertThat(actual.getId()).isEqualTo(UUID.fromString(actual.getId().toString()));
		Assertions.assertThat(actual.isActive()).isEqualTo(true);
		Assertions.assertThat(actual.getCreatedBy()).isEqualTo("SYS");
		Assertions.assertThat(actual.getCreatedDate()).isAfterOrEqualTo(testDate);
		Assertions.assertThat(actual.getUpdatedBy()).isEqualTo("SYS");
		Assertions.assertThat(actual.getUpdatedDate()).isAfterOrEqualTo(testDate);

	}

	@Test
	public void testUpdateResource() throws Exception {

		ReflectionTestUtils.setField(testObject, "id", testId);

		Mockito.when(testService.updateResource(testResource)).thenReturn(testResource);

		Response response = testObject.updateResource(testResource);

		Assertions.assertThat(response.getEntity()).isNotNull();
		Assertions.assertThat(response.getEntity()).isInstanceOf(Address.class);

		Address actual = (Address) response.getEntity();
		Assertions.assertThat(actual.getId()).isNotNull();
		Assertions.assertThat(actual.getId().toString()).isEqualTo(testId);
		Assertions.assertThat(actual.isActive()).isEqualTo(false);
		Assertions.assertThat(actual.getUpdatedBy()).isEqualTo("SYS");
		Assertions.assertThat(actual.getUpdatedDate()).isAfterOrEqualTo(testDate);
	}

	@Test
	public void testUpdateResourceNotExist() throws Exception {
		ReflectionTestUtils.setField(testObject, "id", testId);

		Mockito.when(testService.updateResource(testResource)).thenReturn(null);

		Response response = testObject.updateResource(testResource);

		Assertions.assertThat(response.getEntity()).isNull();
	}

	@Test
	public void testDeleteResource() throws Exception {
		ReflectionTestUtils.setField(testObject, "id", testId);

		Mockito.when(testService.deleteResource(testId)).thenReturn(testResource);

		Response response = testObject.deleteResource();

		Assertions.assertThat(response.getEntity()).isNotNull();
		Assertions.assertThat(response.getEntity()).isEqualTo(testResource);
	}
}

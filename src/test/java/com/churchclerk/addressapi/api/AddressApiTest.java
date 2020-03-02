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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@SpringBootTest
@TestPropertySource(locations="classpath:application-mock.properties")
public class AddressApiTest {

	@InjectMocks
	private AddressApi		testObject;

	@Mock
	private AddressService	service;



	private Address 		testData;
	private AddressEntity	testEntity;


	@BeforeEach
	public void setupMock() {
		testData 	= new Address();
		testEntity	= new AddressEntity();
	}

	@Test
	@Order(0)
	public void contexLoads() throws Exception {
		Assertions.assertThat(testObject).isNotNull();
	}

	@Test
	public void testGetResources() throws Exception {
		ReflectionTestUtils.setField(testObject, "sortBy", "street");

		Mockito.when(service.getResources(null, null)).thenReturn(null);

		Response actual = testObject.getResources();

		Assertions.assertThat(actual.getEntity()).isNull();
	}


	@Test
	public void testGetResource() throws Exception {
//		String	id = "TEST_ID";
//
//		Mockito.when(storage.findById(id)).thenReturn(Optional.of(testEntity));
//		Address actual = testObject.getResource(id);
//
//		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testCreateResource() throws Exception {

//		Mockito.when(storage.save(testEntity)).thenReturn(testEntity);
//
//		Address actual = testObject.createResource(testData);
//
//		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testUpdateResource() throws Exception {
//		testData.setId(UUID.randomUUID());
//
//		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.of(testEntity));
//		Mockito.when(storage.save(testEntity)).thenReturn(testEntity);
//
//		Address actual = testObject.updateResource(testData);
//
//		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testUpdateResourceNotExist() throws Exception {
//		testData.setId(UUID.randomUUID());
//
//		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.ofNullable(null));
//
//		Address actual = testObject.updateResource(testData);
//
//		Assertions.assertThat(actual).isEqualTo(testData);
	}

	@Test
	public void testDeleteResource() throws Exception {
//		testData.setId(UUID.randomUUID());
//
//		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.of(testEntity));
//		//Mockito.when(storage.deleteById(testData.getId().toString())).;
//
//		Address actual = testObject.deleteResource(testData.getId().toString());
//
//		Assertions.assertThat(actual).isEqualTo(testEntity);
	}
}

/*
 */
package com.churchclerk.addressapi.service;


import com.churchclerk.addressapi.model.Address;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@SpringBootTest
@TestPropertySource(locations="classpath:application-mock.properties")
public class AddressServiceTest {

	@InjectMocks
	private AddressService	testObject;

	@Mock
	private AddressStorage	storage;;

	private Address 		testData;
	private AddressEntity	testEntity;

	private ResourceSpec	resourceSpec = null;


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
		Pageable pageable = PageRequest.of(0, 10, createSort());

		resourceSpec = new ResourceSpec(testData);

		Mockito.when(storage.findAll(resourceSpec, pageable)).thenReturn(null);
		Page<? extends Address> actual = testObject.getResources(pageable, testData);

		Assertions.assertThat(actual).isNull();
	}

	private Sort createSort() {
		return createSort(null);
	}

	private Sort createSort(String sortBy) {
		List<Sort.Order> list = new ArrayList<Sort.Order>();

		if (sortBy != null) {
			for (String item : sortBy.split(",")) {
				Sort.Direction 	dir 	= Sort.Direction.ASC;
				String			field	= item;

				if (item.startsWith("-")) {
					dir		= Sort.Direction.DESC;
					field 	= item.substring(1);
				}
				else if (item.startsWith("+")) {
					field 	= item.substring(1);
				}

				list.add(new Sort.Order(dir, field));
			}
		}

		return Sort.by(list);
	}

	@Test
	public void testGetResource() throws Exception {
		String	id = "TEST_ID";

		Mockito.when(storage.findById(id)).thenReturn(Optional.of(testEntity));
		Address actual = testObject.getResource(id);

		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testCreateResource() throws Exception {

		Mockito.when(storage.save(testEntity)).thenReturn(testEntity);

		Address actual = testObject.createResource(testData);

		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testUpdateResource() throws Exception {
		testData.setId(UUID.randomUUID());

		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.of(testEntity));
		Mockito.when(storage.save(testEntity)).thenReturn(testEntity);

		Address actual = testObject.updateResource(testData);

		Assertions.assertThat(actual).isEqualTo(testEntity);
	}

	@Test
	public void testUpdateResourceNotExist() throws Exception {
		testData.setId(UUID.randomUUID());

		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.ofNullable(null));

		Address actual = testObject.updateResource(testData);

		Assertions.assertThat(actual).isEqualTo(testData);
	}

	@Test
	public void testDeleteResource() throws Exception {
		testData.setId(UUID.randomUUID());

		Mockito.when(storage.findById(testData.getId().toString())).thenReturn(Optional.of(testEntity));
		//Mockito.when(storage.deleteById(testData.getId().toString())).;

		Address actual = testObject.deleteResource(testData.getId().toString());

		Assertions.assertThat(actual).isEqualTo(testEntity);
	}
}

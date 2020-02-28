/**
 * 
 */
package com.churchclerk.addressapi.service;

import com.churchclerk.addressapi.model.Address;
import com.churchclerk.addressapi.storage.AddressEntity;
import com.churchclerk.addressapi.storage.AddressStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


/**
 * 
 * @author dongp
 *
 */
@Service
public class AddressService {

	private static Logger logger	= LoggerFactory.getLogger(AddressService.class);

	@Autowired
	private AddressStorage storage;

	/**
	 *
	 */
	public class ResourceSpec implements Specification<AddressEntity> {

		private Address criteria = null;

		/**
		 *
		 * @param criteria
		 */
		public ResourceSpec(Address criteria) {
			this.criteria = criteria;
		}

		@Override
		public Predicate toPredicate(Root<AddressEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			List<Predicate> predicates	= new ArrayList<Predicate>();

			addPredicate(criteriaBuilder, root, "street", criteria.getStreet(), predicates);
			addPredicate(criteriaBuilder, root, "city", criteria.getCity(), predicates);
			addPredicate(criteriaBuilder, root, "state", criteria.getState(), predicates);
			addPredicate(criteriaBuilder, root, "zip", criteria.getZip(), predicates);
			addPredicate(criteriaBuilder, root, "country", criteria.getCountry(), predicates);
			addPredicate(criteriaBuilder, root, "active", criteria.isActive(), predicates);

			if (predicates.isEmpty()) {
				return null;
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}

		private void addPredicate(CriteriaBuilder criteriaBuilder, Root<AddressEntity> root, String field, String value, List<Predicate> predicates) {
			Predicate predicate = null;

			if (value != null) {
				if (value.trim().isEmpty()) {
					predicate = criteriaBuilder.isEmpty(root.get(field));
				} else if (value.contains("%")) {
					predicate = criteriaBuilder.like(root.get(field), value);
				} else {
					predicate = criteriaBuilder.equal(root.get(field), value);
				}
			}

			if (predicate != null) {
				predicates.add(predicate);
			}
		}

		private void addPredicate(CriteriaBuilder criteriaBuilder, Root<AddressEntity> root, String field, Boolean value, List<Predicate> predicates) {
			Predicate predicate = null;

			if (value != null) {
				predicate = criteriaBuilder.equal(root.get(field), value);
			}

			if (predicate != null) {
				predicates.add(predicate);
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public Page<? extends Address> getResources(Pageable pageable, Address criteria) {
		List<Address>	list	= new ArrayList<Address>();

		Page<AddressEntity> page = storage.findAll(new ResourceSpec(criteria), pageable);

		return page;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Address getResource(String id) {

		Optional<AddressEntity> entity = storage.findById(id);
		return entity.get();
	}

	/**
	 *
	 * @param resource
	 * @return
	 */
	public Address createResource(Address resource) {
		AddressEntity entity = new AddressEntity();

		entity.copy(resource);

		storage.save(entity);
		return entity;
	}

	/**
	 *
	 * @param resource
	 * @return
	 */
	public Address updateResource(Address resource) {
		Optional<AddressEntity> optional = storage.findById(resource.getId().toString());

		if (optional.isPresent()) {
			AddressEntity entity = optional.get();

			entity.copy(resource);
			storage.save(entity);
		}

		return resource;
	}


	/**
	 *
	 * @param id
	 * @return
	 */
	public Address deleteResource(String id) {
		Optional<AddressEntity> optional = storage.findById(id);

		if (optional.isPresent()) {
			storage.deleteById(id);
		}

		return optional.get();
	}
}

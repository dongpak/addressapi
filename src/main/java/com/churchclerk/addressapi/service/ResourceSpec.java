package com.churchclerk.addressapi.service;

import com.churchclerk.addressapi.model.Address;
import com.churchclerk.addressapi.storage.AddressEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ResourceSpec implements Specification<AddressEntity> {

    private Address criteria = null;

    /**
     * @param criteria
     */
    public ResourceSpec(Address criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<AddressEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<Predicate>();

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

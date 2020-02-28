/**
 * 
 */
package com.churchclerk.addressapi.storage;

import com.churchclerk.addressapi.model.Address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;


/**
 * 
 * @author dongp
 *
 */
@Entity
@Table(name="address")
public class AddressEntity extends Address {

	@Id
	@Column(name="id")
	public String getUUID() {
		return super.getId().toString();
	}

	public void setUUID(String id) {
		super.setId(UUID.fromString(id));
	}

	@Column(name="active")
	@Override
	public boolean isActive() {
		return super.isActive();
	}

	@Column(name="street")
	@Override
	public String getStreet() {
		return super.getStreet();
	}

	@Column(name="city")
	@Override
	public String getCity() {
		return super.getCity();
	}

	@Column(name="state")
	@Override
	public String getState() {
		return super.getState();
	}

	@Column(name="zip")
	@Override
	public String getZip() {
		return super.getZip();
	}

	@Column(name="country")
	@Override
	public String getCountry() {
		return super.getCountry();
	}

	@Override
	public Date getCreatedDate() {
		return super.getCreatedDate();
	}

	@Override
	public String getCreatedBy() {
		return super.getCreatedBy();
	}

	@Override
	public Date getUpdatedDate() {
		return super.getUpdatedDate();
	}

	@Override
	public String getUpdatedBy() {
		return super.getUpdatedBy();
	}
}

/**
 * 
 */
package com.churchclerk.addressapi.storage;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;


/**
 * 
 * @author dongp
 *
 */
public interface AddressStorage extends CrudRepository<AddressEntity, String>, JpaSpecificationExecutor<AddressEntity> {

}

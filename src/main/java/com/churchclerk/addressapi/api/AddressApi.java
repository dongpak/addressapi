/**
 * 
 */
package com.churchclerk.addressapi.api;

import com.churchclerk.addressapi.model.Address;
import com.churchclerk.addressapi.service.AddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 
 * @author dongp
 *
 */
@Component
@Path("/address")
public class AddressApi extends BaseApi {

	private static Logger logger = LoggerFactory.getLogger(AddressApi.class);
	
	
	@PathParam("id")
	private String id;

	@DefaultValue("0")
	@QueryParam("page")
	private int page = 0;

	@DefaultValue("20")
	@QueryParam("size")
	private int size = 20;

	@QueryParam("street")
	private String streetLike;

	@QueryParam("city")
	private String cityLike;

	@QueryParam("state")
	private String stateLike;

	@QueryParam("zip")
	private String zipLike;

	@QueryParam("country")
	private String countryLike;

	@QueryParam("active")
	private Boolean active;

	@QueryParam("sortBy")
	private String sortBy;


	
//	@Autowired
//	private AuthenticationService	security;

	@Autowired
	private AddressService			service;

	@Context
	protected HttpServletRequest httpRequest;
	

	/**
	 * 
	 */
	public AddressApi() {
		super(logger);
	}


	@GET
	@Produces({MediaType.APPLICATION_JSON})

	public Response getResources() {
		Pageable pageable = PageRequest.of(page, size, createSort());

		return Response.ok(service.getResources(pageable, createCriteria())).build();
	}

	private Address createCriteria() {
		Address	criteria	= new Address();

		criteria.setStreet(streetLike);
		criteria.setCity(cityLike);
		criteria.setState(stateLike);
		criteria.setZip(zipLike);
		criteria.setCountry(countryLike);
		criteria.setActive(true);
		if (active != null) {
			criteria.setActive(active.booleanValue());
		}

		return criteria;
	}

	private Sort createSort() {
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

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getResource() {

		return Response.ok(service.getResource(id)).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response createResource(Address resource) {
		
		try {
			if (resource.getId() == null) {
				resource.setId(UUID.randomUUID());
			}
			resource.setActive(true);
			resource.setCreatedBy("SYS");
			resource.setCreatedDate(new Date());
			resource.setUpdatedBy("SYS");
			resource.setUpdatedDate(new Date());

			Address newResource = service.createResource(resource);
			//security.createToken(username);
			
			return Response.ok(newResource).build();
		}
		catch (Throwable t) {
			return generateErrorResponse(t);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateResource(Address resource) {
		resource.setUpdatedBy("SYS");
		resource.setUpdatedDate(new Date());

		service.updateResource(resource);
		return Response.ok(resource).build();
	}

	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
    public Response deleteResource() {
		
		try {
			return Response.ok(service.deleteResource(id)).build();
		}
		catch (Throwable t) {
			return generateErrorResponse(t);
		}
    }
}

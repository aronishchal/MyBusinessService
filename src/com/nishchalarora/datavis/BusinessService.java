package com.nishchalarora.datavis;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/businesses")
public class BusinessService {
	BusinessDao businessDao = new BusinessDao();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusinesses() {
		try {
			return Response.ok(businessDao.getBusinesses(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createBusiness(@Valid Business business) {
		try {
			Business insertedBusiness = businessDao.insertBusiness(business);
			return Response.status(Status.CREATED).entity(insertedBusiness).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateBusiness(@PathParam("id") @NotNull int id, @Valid Business business) {
		try {
			Business updatedBusiness = businessDao.updateBusiness(id, business);
			return Response.ok(updatedBusiness, MediaType.APPLICATION_JSON).build();
		} catch (NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getLocalizedMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteBusiness(@PathParam("id") @NotNull int id) {
		try {
			businessDao.deleteBusiness(id);
			return Response.status(Status.NO_CONTENT).build();
		} catch (NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getLocalizedMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
		}
	}
}

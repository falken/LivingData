package com.lightstream.demo;

import com.google.common.collect.ImmutableList;
import io.dropwizard.jersey.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/api/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PeopleResource {

    private PersonService personService;
    private PersonAccountService personAccountService;

    public PeopleResource(PersonService personService, PersonAccountService personAccountService) {
        this.personService = personService;
        this.personAccountService = personAccountService;
    }

    @GET
    public List<Person> getPeople(){
        return personService.getPeople();
    }

    @GET
    @Path("/{person_id}")
    public PersonData getPerson(@PathParam("person_id") UUID personId){
        return personService.getPersonData(personId);
    }

    @PATCH
    @Path("/{person_id}")
    public Response updatePerson(@PathParam("person_id") UUID personId, Map<String,Object> delta){
        personService.patchPersonData(personId,delta);
        return Response.noContent().build();
    }

    @GET
    @Path("/{person_id}/account")
    public PersonAccount getPersonAccount(@PathParam("person_id") UUID personId){
        return personAccountService.getPersonAccount(personId);
    }

    @GET
    @Path("/{person_id}/account/transactions")
    public List<AccountTransaction> getTransactions(@PathParam("person_id") UUID personId){
        return personAccountService.getAccountTransactions(personId);
    }

    @POST
    @Path("/{person_id}/account/transactions")
    public Response postTransaction(@PathParam("person_id") UUID personId, Amount amount){
        personAccountService.postTransaction(personId,amount.getAmount());
        return Response.noContent().build();
    }

}

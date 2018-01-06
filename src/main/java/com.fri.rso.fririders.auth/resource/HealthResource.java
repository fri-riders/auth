package com.fri.rso.fririders.auth.resource;

import com.fri.rso.fririders.auth.config.ConfigProperties;
import com.fri.rso.fririders.auth.util.Helpers;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("health-test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Log
public class HealthResource {

    @Inject
    private ConfigProperties configProperties;

    @GET
    @Path("instance")
    public Response getInstanceId() {
        return Response.ok(Helpers.buildMessageJson(EeRuntime.getInstance().getInstanceId())).build();
    }

    @POST
    @Path("update")
    public Response updateHealth(Boolean isHealthy) {
        configProperties.setHealthy(isHealthy);

        return Response.ok().build();
    }

    @GET
    @Path("dos/{n}")
    public Response dos(@PathParam("n") Integer n) {
        for (int i = 0; i < n; i++) fibonacci(n);

        return Response.ok().build();
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

package com.fri.rso.fririders.auth.resource;

import com.fri.rso.fririders.auth.config.ConfigProperties;
import com.kumuluz.ee.logs.cdi.Log;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("config")
@Log
public class ConfigResource {

    @Inject
    private ConfigProperties configProperties;

    @GET
    public Response getConfig() {
        return Response.ok(configProperties.toJsonString()).build();
    }

    @GET
    @Path("/info")
    public Response getProjectInfo() {
        String data = "{" +
                "\"clani\": [\"ts4293\", \"ub6189\", \"je1468\"]," +
                "\"opis_projekta\": \"Nas projekt implementira aplikacijo za oddajo nepremicnin.\"," +
                "\"mikrostoritve\": [\"http://169.51.17.29:30719/v1/users\", \"http://169.51.17.191:32279/v1/bookings\", \"http://169.51.16.54:32698/swagger-ui.html#/\"]," +
                "\"github\": [\"https://github.com/fri-riders/users\", \"https://github.com/fri-riders/accommodations\", \"https://github.com/fri-riders/display-bookings\", \"https://github.com/fri-riders/notifications\"]," +
                "\"travis\": [\"https://travis-ci.org/fri-riders/users\", \"https://travis-ci.org/fri-riders/accommodations\", \"https://travis-ci.org/fri-riders/display-bookings\", \"https://travis-ci.org/fri-riders/notifications\"]," +
                "\"dockerhub\": [\"https://hub.docker.com/r/tomisebjanic/rso-users\", \"https://hub.docker.com/r/janerz6/accommodations\", \"https://hub.docker.com/r/urosbajc/display-bookings\", \"https://hub.docker.com/r/janerz6/notifications\"]" +
                "}";

        return Response.ok(data).build();
    }
}

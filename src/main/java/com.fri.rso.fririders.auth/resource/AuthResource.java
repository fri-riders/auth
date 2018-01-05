package com.fri.rso.fririders.auth.resource;

import com.fri.rso.fririders.auth.entity.Jwt;
import com.fri.rso.fririders.auth.entity.User;
import com.fri.rso.fririders.auth.service.UserService;
import com.fri.rso.fririders.auth.util.Helpers;
import com.fri.rso.fririders.auth.util.PasswordAuthentication;
import com.kumuluz.ee.logs.cdi.Log;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("auth")
@Log
public class AuthResource {

    @Inject
    private UserService usersBean;

    private String issuer;
    private byte[] sharedSecret;

    public AuthResource() {
        this.sharedSecret = "t6i8wziwxjAhAtqCp4QLbeb2T9sz9VyY".getBytes();
        this.issuer = "http://auth.fririders.com";
    }

    @GET
    public Response test() {
        return Response.ok().build();
    }

    @POST
    @Path("issue")
    public Response issueToken(Jwt incomingJwt) throws JOSEException {
        assert incomingJwt != null && incomingJwt.getEmail() != null;

        System.out.println("called issueToken");
        System.out.println("incomingJwt = " + incomingJwt);

        Date now = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(incomingJwt.getEmail())
                .issuer(this.issuer)
                .audience(Arrays.asList("http://app.fririders.com", "http://users.fririders.com"))
                .expirationTime(new Date(now.getTime() + 60 * 1000))
                .issueTime(now)
                .notBeforeTime(now)
                .jwtID(UUID.randomUUID().toString())
                .claim("ROLE", "USER")
                .claim("EMAIL", incomingJwt.getEmail())
                .build();

        JWSSigner signer = new MACSigner(this.sharedSecret);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        Jwt jwt = new Jwt();
        jwt.setEmail(incomingJwt.getEmail());
        jwt.setToken(signedJWT.serialize());

        return Response.ok(jwt).build();
    }

    @POST
    @Path("verify")
    public Response verifyToken(Jwt incomingJwt) throws ParseException, JOSEException {
        assert incomingJwt != null && incomingJwt.getEmail() != null;

        SignedJWT signedJWT = SignedJWT.parse(incomingJwt.getToken());
        JWSVerifier verifier = new MACVerifier(this.sharedSecret);

        boolean valid = signedJWT.verify(verifier) &&
                signedJWT.getJWTClaimsSet().getSubject().equals(incomingJwt.getEmail()) &&
                signedJWT.getJWTClaimsSet().getClaim("EMAIL").equals(incomingJwt.getEmail()) &&
                signedJWT.getJWTClaimsSet().getIssuer().equals(this.issuer) &&
                new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()) &&
                new Date().after(signedJWT.getJWTClaimsSet().getNotBeforeTime());

        return (valid ? Response.ok() : Response.status(Response.Status.UNAUTHORIZED)).build();
    }

}

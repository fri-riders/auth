package com.fri.rso.fririders.auth.resource;

import com.kumuluz.ee.logs.cdi.Log;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.RequestScoped;
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
    public Response issueToken(String jsonPayload) throws JOSEException {
        HashMap<String, String> payload = Helpers.jsonToMap(jsonPayload);
        Date now = new Date();

        assert payload != null && payload.get("email") != null;

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(payload.get("email"))
                .issuer(this.issuer)
                .audience(Arrays.asList("http://app.fririders.com", "http://users.fririders.com"))
                .expirationTime(new Date(now.getTime() + 60 * 1000))
                .issueTime(now)
                .notBeforeTime(now)
                .jwtID(UUID.randomUUID().toString())
                .claim("ROLE", "USER")
                .claim("EMAIL", payload.get("email"))
                .build();

        JWSSigner signer = new MACSigner(this.sharedSecret);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        return Response.ok(Helpers.buildTokenMessage(signedJWT.serialize(), payload.get("email"))).build();
    }

    @POST
    @Path("verify")
    public Response verifyToken(String jsonPayload) throws ParseException, JOSEException {
        HashMap<String, String> payload = Helpers.jsonToMap(jsonPayload);

        assert payload != null && payload.get("token") != null && payload.get("email") != null;

        SignedJWT signedJWT = SignedJWT.parse(payload.get("token"));
        JWSVerifier verifier = new MACVerifier(this.sharedSecret);

        boolean valid = signedJWT.verify(verifier) &&
                signedJWT.getJWTClaimsSet().getSubject().equals(payload.get("email")) &&
                signedJWT.getJWTClaimsSet().getClaim("EMAIL").equals(payload.get("email")) &&
                signedJWT.getJWTClaimsSet().getIssuer().equals(this.issuer) &&
                new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()) &&
                new Date().after(signedJWT.getJWTClaimsSet().getNotBeforeTime());

        return (valid ? Response.ok() : Response.status(Response.Status.UNAUTHORIZED)).build();
    }
}

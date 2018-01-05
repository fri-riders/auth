package com.fri.rso.fririders.auth.service;

import com.fri.rso.fririders.auth.entity.User;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;
import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Log
public class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class.getName());

    private Client http = ClientBuilder.newClient();

    @Inject
    @DiscoverService(value = "users", version = "*", environment = "dev", accessType = AccessType.DIRECT)
    private Optional<String> usersUrl;

    @CircuitBreaker
    @Fallback(fallbackMethod = "findUserByEmailFallback")
    @CommandKey("http-find-user")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Asynchronous
    public User findUserByEmail(String email) {
        return http.target(this.usersUrl.get() + "/v1/users/" + email)
                .request(MediaType.APPLICATION_JSON)
                .get(User.class);
    }

    public User findUserByEmailFallback(String email) {
        log.warn("findUserByEmailFallback called");
        return null;
    }
}

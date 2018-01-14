package com.fri.rso.fririders.auth.health;

import com.fri.rso.fririders.auth.config.ConfigProperties;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class AuthServiceHealthCheck implements HealthCheck {

    @Inject
    private ConfigProperties configProperties;

    @Override
    public HealthCheckResponse call() {
        if (configProperties.isHealthy()) {
            return HealthCheckResponse.named(AuthServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(AuthServiceHealthCheck.class.getSimpleName()).down().build();
        }
    }
}

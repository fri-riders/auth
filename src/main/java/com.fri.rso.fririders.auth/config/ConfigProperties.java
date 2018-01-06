package com.fri.rso.fririders.auth.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("auth-config")
public class ConfigProperties {

    @ConfigValue(watch = true)
    private boolean healthy;

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String toJsonString() {
        return String.format(
                "{" +
                    "\"healthy\": %b" +
                "}",
                this.isHealthy()
        );
    }
}

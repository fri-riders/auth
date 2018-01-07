package com.fri.rso.fririders.auth.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.util.logging.Logger;

public class ConfigurationEventHandler {

    private static final Logger log = Logger.getLogger(ConfigurationEventHandler.class.getName());

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

//        ConfigurationUtil.getInstance().subscribe("auth-config.healthy", (String key, String value) -> {
//            if ("auth-config.healthy".equals(key)) {
//                if ("true".equals(value.toLowerCase())) {
//                    log.info("Service IS healthy.");
//                } else {
//                    log.info("Service is NOT healthy.");
//                }
//            }
//        });
    }
}

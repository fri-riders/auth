package com.fri.rso.fririders.auth.log;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.CloseableThreadContext;

import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.UUID;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogContextInterceptor {

    public Object logMethodEntryAndExit(InvocationContext invocationContext) throws Exception {
        HashMap<String, String> settings = new HashMap<>();

        settings.put("environment", EeConfig.getInstance().getEnv().getName());
        settings.put("serviceName", EeConfig.getInstance().getName());
        settings.put("applicationVersion", EeConfig.getInstance().getVersion());
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());
        settings.put("uniqueRequestId", UUID.randomUUID().toString());

        CloseableThreadContext.putAll(settings);

        return invocationContext.proceed();
    }

}

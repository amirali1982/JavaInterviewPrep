package com.interview.portfolio.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * A Dynamic Proxy InvocationHandler.
 * Intercepts method calls to the target object.
 * This is the underlying mechanism for Spring AOP (Aspect Oriented
 * Programming).
 */
public class LoggingHandler implements InvocationHandler {

    private final Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    /**
     * Factory method to create a Proxy instance for a given interface.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Object target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType },
                new LoggingHandler(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Check if the method has the @Logged annotation
        // Note: We must look at the method on the TARGET class, not necessarily the
        // interface
        // depending on where the annotation is placed. Ideally, it's on the interface
        // for proxies.
        // For this demo, we check the interface method.
        boolean isLogged = method.isAnnotationPresent(Logged.class);

        if (isLogged) {
            System.out.println("[PROXY] Entering method: " + method.getName() + " with args: " + Arrays.toString(args));
            long start = System.nanoTime();
            try {
                // Delegate to the actual object
                Object result = method.invoke(target, args);

                long duration = System.nanoTime() - start;
                System.out.println(
                        "[PROXY] Exiting method: " + method.getName() + " | Time: " + (duration / 1000) + " micros");

                return result;
            } catch (Exception e) {
                // Unwrap reflection exception
                throw e.getCause();
            }
        } else {
            // Just pass through
            return method.invoke(target, args);
        }
    }
}

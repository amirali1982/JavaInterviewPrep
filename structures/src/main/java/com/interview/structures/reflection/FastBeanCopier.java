package com.interview.structures.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates Modern Reflection using java.lang.invoke.MethodHandles (Java 7+
 * but mainstream later).
 * MethodHandles are faster than standard Reflection (Method.invoke) because the
 * JVM can inline them
 * essentially as direct calls after warm-up.
 * 
 * Scenario: Copy properties from one bean to another (like
 * BeanUtils.copyProperties).
 */
public class FastBeanCopier {

    // Cache handles to reuse them (Expensive to lookup, cheap to invoke)
    @SuppressWarnings("unused")
    private static final Map<String, MethodHandle> GETTER_CACHE = new HashMap<>();

    /**
     * Copies a string field from source to destination using MethodHandles.
     * Assumes simple POJO with standard getters/setters.
     */
    public static void copyProperty(Object source, Object dest, String propertyName) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            String capitalized = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            String getterName = "get" + capitalized;
            String setterName = "set" + capitalized;

            // 1. Lookup Getter (Only if not cached for demo simplicity - real world needs
            // ConcurrentMap)
            // Signature: ret String, args ()
            MethodType getterType = MethodType.methodType(String.class);
            MethodHandle getter = lookup.findVirtual(source.getClass(), getterName, getterType);

            // 2. Lookup Setter
            // Signature: ret void, args (String)
            MethodType setterType = MethodType.methodType(void.class, String.class);
            MethodHandle setter = lookup.findVirtual(dest.getClass(), setterName, setterType);

            // 3. Invoke
            // invokeExact is strictest (fastest). invoke is more permissive (some type
            // conversion).
            String value = (String) getter.invoke(source);
            setter.invoke(dest, value);

        } catch (Throwable e) {
            throw new RuntimeException("Fast Copy Failed", e);
        }
    }
}

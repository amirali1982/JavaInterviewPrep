package com.interview.portfolio;

/**
 * Utility class to demonstrate reflection capabilities.
 * Inspects class hierarchies and enforces type constraints dynamically.
 */
public class ReflectiveAssetInspector {

    /**
     * Checks if the potential subclass is actually a subclass of the superclass.
     * Use this to prevent usage of class types chains by explicitly checking
     * inheritance.
     *
     * @param potentialSubclass the class to check
     * @param superClass        the expected superclass
     * @return true if potentialSubclass extends superClass
     */
    public boolean checkSubclass(Class<?> potentialSubclass, Class<?> superClass) {
        if (potentialSubclass == null || superClass == null) {
            return false;
        }
        return superClass.isAssignableFrom(potentialSubclass) && !potentialSubclass.equals(superClass);
    }

    /**
     * Validates that the provided type is a valid Asset subclass.
     *
     * @param type the class to validate
     * @throws IllegalArgumentException if the type is not a subclass of Asset
     */
    public void validateAssetType(Class<?> type) {
        if (!checkSubclass(type, Asset.class)) {
            throw new IllegalArgumentException("Class " + type.getName() + " is not a valid Asset subclass");
        }
    }
}

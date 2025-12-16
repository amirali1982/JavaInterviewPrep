package com.interview.structures.reflection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FastBeanCopierTest {

    // Simple POJO for testing
    public static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void testCopyProperty() {
        Person p1 = new Person();
        p1.setName("Alice");

        Person p2 = new Person();

        FastBeanCopier.copyProperty(p1, p2, "name");

        assertEquals("Alice", p2.getName());
    }
}

package org.example.juc;

import java.util.concurrent.atomic.AtomicReference;

public class example06 {

    public static void main(String[] args) {
        Simple alex = new Simple("Alex", 12);
        AtomicReference<Simple> atomic = new AtomicReference<>(alex);
        System.out.println(atomic.get());

        boolean result = atomic.compareAndSet(alex, new Simple("Kelvin", 16));
        System.out.println(result);
        System.out.println(atomic.get());

    }

    static class Simple {
        private String name;
        private int age;

        public Simple(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Simple{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

    }

}

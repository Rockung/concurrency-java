package org.example.concurrency.example04;

import java.util.Arrays;
import java.util.Optional;

public class ThreadStackTracer {
    public static void main(String[] args) {
        new Test1().test();
    }

    static class Test1 {
        private Test2 test2 = new Test2();

        public void test() {
            test2.test();
        }
    }

    static class Test2 {
        public void test() {
            Arrays.asList(Thread.currentThread().getStackTrace())
                    .stream()
                    .filter(e -> !e.isNativeMethod())
                    .forEach(e -> Optional.of(e.getClassName() + ": " + e.getMethodName() + ":" + e.getLineNumber())
                                            .ifPresent(System.out::println)
                    );
        }
    }
}

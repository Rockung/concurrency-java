package org.example.juc;

/**
 * JIT optimization
 */
public class example03 {
    private static boolean done = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                while(!done) {
                    // comment this line
                    System.out.println("I'm working ...");
                }
                /**
                 * if **done** is not a volatile variable and there is no
                 * **while** body, JIT makes a optimization
                 *
                 * while(true) {}
                 *
                 */
            }
        }.start();

        Thread.sleep(1000);
        System.out.println("set done to true");
        done = true;
    }
}

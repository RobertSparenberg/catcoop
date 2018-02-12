/**
 * Main class that initializes the CatCoop server.
 * @author imahilus
 */
package net.frozenchaos.catcoop.main;

import net.frozenchaos.catcoop.io.Lights;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public final class Main {
    private static boolean shutdownRequested = false;
    private static AnnotationConfigWebApplicationContext applicationContext;

    public static void main(String[] varargs) {
        applicationContext = new AnnotationConfigWebApplicationContext();

        Lights lights = new Lights();
        lights.doSomething();

        Runtime.getRuntime().addShutdownHook(new Thread(new shutdownListener()) {
            @Override
            public void run()
            {
                System.out.println("Shutdown hook ran!");
            }
        });

        loopUntillShutdown();
    }

    private static void loopUntillShutdown() {
        while(!Main.shutdownRequested) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ignored) {

            }
        }
    }

    private static class shutdownListener implements Runnable {
        @Override
        public void run() {
            Main.shutdownRequested = true;
        }
    }
}

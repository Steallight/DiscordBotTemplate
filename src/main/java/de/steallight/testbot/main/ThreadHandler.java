package de.steallight.testbot.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Einfacher Thread-Handler, der einen gemeinsamen CachedThreadPool zur Ausführung
 * von Runnables bereitstellt. Nutzbar, um Hintergrundaufgaben ohne neuen Thread pro
 * Aufgabe auszuführen.
 */
public class ThreadHandler {
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /**
     * Startet die Ausführung eines Runnables im internen Executor.
     *
     * @param runnable der auszuführende Task
     */
    public static void startExecute(final Runnable runnable) {
        ThreadHandler.getExecutor().execute(runnable);
    }

    /**
     * Entfernt einen Runnable aus der Executor-Queue (falls noch nicht gestartet).
     *
     * @param runnable der zu entfernende Task
     */
    public static void removeExecute(final Runnable runnable) {
        ThreadHandler.getExecutor().remove(runnable);
    }


    /**
     * Liefert die interne ThreadPoolExecutor-Instanz zurück.
     *
     * @return der Executor
     */
    public static ThreadPoolExecutor getExecutor() {
        return ThreadHandler.executor;
    }
}

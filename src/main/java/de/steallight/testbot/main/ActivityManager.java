package de.steallight.testbot.main;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Verwaltet die Präsenz (Status und Aktivität) des Bots.
 *
 * <p>Die Klasse startet einen periodischen Timer, der die angezeigte Aktivität
 * in regelmäßigen Abständen wechselt (z. B. "Rocket League", "Chats an").
 */
public class ActivityManager {
    /** Referenz auf den laufenden Switcher-Thread (als Runnable verwaltet). */
    private Runnable switcherThread;
    // Referenz auf den Timer damit wir ihn später abbrechen können
    private Timer timer;

    /**
     * Standard-Konstruktor. Frührere Implementierungen hielten eine Referenz auf
     * {@link Bot}, die aber nicht verwendet wurde. Deshalb ist hier kein Parameter nötig
     * und die Gefahr eines "this-escape" beim Aufruf aus dem Bot-Konstruktor entfällt.
     */
    public ActivityManager() {
    }

    /**
     * Startet das regelmäßige Umschalten der Bot-Präsenz.
     * Die Aufgaben werden über einen Timer ausgeführt, der alle 60 Sekunden
     * die angezeigte Aktivität wechselt.
     */
    public void loadPresence() {
        ThreadHandler.startExecute(this.switcherThread = () -> {
            // Timer als Daemon starten, Referenz im Feld speichern
            this.timer = new Timer(true);

            this.timer.scheduleAtFixedRate(new TimerTask() {
                int currentRoutinePosition = 0;

                @Override
                public void run() {
                    switch (this.currentRoutinePosition) {
                        case 0:
                            // Beispiel-Aktivität: spielend
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.playing("Rocket League"));
                            this.currentRoutinePosition++;
                            break;
                        case 1:
                            // Beispiel-Aktivität: zuschauend
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching("Chats an"));
                            this.currentRoutinePosition++;
                            break;
                        case 2:
                            // Beispiel-Aktivität: zuhörend
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.listening("auf -> !"));
                            this.currentRoutinePosition = 0;
                            break;


                    }
                }
            }, 1, 60000);//<-- In Millisekunden
        });
    }

    /**
     * Stoppt das Umschalten der Präsenz, indem der zuvor registrierte Runnable
     * aus dem ThreadHandler entfernt wird.
     */
    public void stopPresence() {
        // Entferne den Hintergrund-Task und brich den Timer sauber ab
        if (this.switcherThread != null) {
            ThreadHandler.removeExecute(this.switcherThread);
            this.switcherThread = null;
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }
}

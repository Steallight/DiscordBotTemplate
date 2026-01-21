package de.steallight.testbot.main;

import javax.security.auth.login.LoginException;

/**
 * Einstiegspunkt zum Starten des Bots während der Entwicklung.
 * Diese Klasse enthält nur die main-Methode, die eine Instanz von {@link Bot}
 * erzeugt und startet. Fehler während der Authentifizierung werden geloggt.
 */
public class TestBot {


    /**
     * Main-Methode zum Starten der Anwendung.
     *
     * @param args Kommandozeilenargumente (derzeit ungenutzt)
     */
    public static void main(final String[] args) {
        try {
            // Bot-Instanz erstellen und starten (Konstruktor registriert selbstständig Listener)
            new Bot();
        } catch (final LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

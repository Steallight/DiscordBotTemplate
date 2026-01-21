package de.steallight.testbot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * Einstiegspunkt zum Starten des Bots während der Entwicklung.
 * Diese Klasse enthält nur die main-Methode, die eine Instanz von {@link Bot}
 * erzeugt und startet. Fehler während der Authentifizierung werden geloggt.
 */
public class TestBot {

    private static final Logger logger = LoggerFactory.getLogger(TestBot.class);

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
            logger.error("Fehler beim Starten des Bots", e);
            Thread.currentThread().interrupt();
        }
    }

}

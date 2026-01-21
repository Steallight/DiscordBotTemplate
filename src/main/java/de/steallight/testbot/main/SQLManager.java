package de.steallight.testbot.main;

/**
 * Utility-Klasse zur Initialisierung der benötigten Datenbank-Tabellen.
 *
 * <p>Diese Klasse verwendet {@link LiteSQL} um bei Programmstart die
 * benötigten Tabellen (RuleChannel, TicketChannel, NotifyChannel) zu erstellen,
 * falls diese noch nicht vorhanden sind.
 */
public class SQLManager {

    /**
     * Erstellt bei Aufruf die benötigten Tabellen, falls sie nicht existieren.
     * Diese Methode führt einfache DDL-Statements über {@link LiteSQL#onUpdate} aus.
     */
    public static void onCreate(){
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS RuleChannel(channelId INTEGER NOT NULL PRIMARY KEY)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS TicketChannel(channelId INTEGER NOT NULL PRIMARY KEY)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS NotifyChannel(channelId INTEGER NOT NULL PRIMARY KEY)");
    }
}

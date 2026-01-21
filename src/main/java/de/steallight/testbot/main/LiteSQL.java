package de.steallight.testbot.main;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Minimaler SQLite-Wrapper für einfache DB-Operationen.
 *
 * <p>Stellt Verbindung, einfache Update- und Query-Hilfsmethoden sowie
 * Connect/Disconnect-Funktionen bereit. Diese Klasse verwaltet eine
 * einzige statische Connection/Statement-Instanz.
 *
 * Hinweis: Fehler werden aktuell nur per Stacktrace ausgegeben; für Produktion
 * sollte ein Logging-Framework verwendet werden.
 */
public class LiteSQL {

    private static Connection conn;
    private static Statement stmt;

    /**
     * Stellt eine Verbindung zur lokalen SQLite-Datei 'datenbank.db' her.
     * Falls die Datei nicht existiert, wird sie angelegt.
     */
    public static void connect(){
        LiteSQL.conn = null;
        try {
            final File file = new File("datenbank.db");
            if(!file.exists()){
                boolean created = file.createNewFile();
                if (!created) {
                    System.err.println("Warnung: datenbank.db konnte nicht erstellt werden (existiert möglicherweise nicht und es gab einen Fehler)");
                }
            }
            final String url = "jdbc:sqlite:" + file.getPath();
            LiteSQL.conn = DriverManager.getConnection(url);
            System.out.println("Verbindung wurde hergestellt!");
            LiteSQL.stmt = LiteSQL.conn.createStatement();
        }catch (final SQLException | IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Trennt die bestehende Verbindung zur Datenbank, falls vorhanden.
     */
    public static void disconnect(){
        try {
            if (LiteSQL.conn != null){
                LiteSQL.conn.close();
                System.out.println("Verbindung getrennt!");
            }
        }catch (final SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Führt ein SQL-Update/DDL-Statement aus (z. B. CREATE TABLE, INSERT, UPDATE).
     * Fehler werden intern gefangen und ausgegeben.
     *
     * @param sql das auszuführende SQL-Statement
     */
    public static void onUpdate(final String sql){
        try {
            LiteSQL.stmt.execute(sql);
        }catch (final SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Führt eine SQL-Query aus und liefert das ResultSet zurück.
     *
     * @param sql das SELECT-Statement
     * @return das ResultSet oder null, falls ein Fehler auftrat
     */
    public static ResultSet onQuery(final String sql){
        try {
            return LiteSQL.stmt.executeQuery(sql);
        }catch (final SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

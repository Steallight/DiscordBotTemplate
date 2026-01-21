package de.steallight.testbot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

/**
 * Minimaler SQLite-Wrapper für einfache DB-Operationen.
 * Stellt Verbindung, einfache Update- und Query-Hilfsmethoden sowie
 * Connect/Disconnect-Funktionen bereit. Diese Klasse verwaltet eine
 * einzige statische Connection/Statement-Instanz.
 * Hinweis: Fehler werden aktuell in das Logging geschrieben.
 */
public class LiteSQL {

    private static final Logger logger = LoggerFactory.getLogger(LiteSQL.class);

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
                    logger.warn("datenbank.db konnte nicht erstellt werden (existiert möglicherweise nicht und es gab einen Fehler)");
                }
            }
            final String url = "jdbc:sqlite:" + file.getPath();
            LiteSQL.conn = DriverManager.getConnection(url);
            logger.info("Verbindung wurde hergestellt!");
            LiteSQL.stmt = LiteSQL.conn.createStatement();
        }catch (final SQLException | IOException e){
            logger.error("Fehler beim Herstellen der Datenbankverbindung", e);
        }
    }

    /**
     * Trennt die bestehende Verbindung zur Datenbank, falls vorhanden.
     */
    @SuppressWarnings("unused")
    public static void disconnect(){
        try {
            if (LiteSQL.conn != null){
                LiteSQL.conn.close();
                logger.info("Verbindung getrennt!");
            }
        }catch (final SQLException e){
            logger.error("Fehler beim Trennen der Datenbankverbindung", e);
        }
    }

    /**
     * Führt ein SQL-Update/DDL-Statement aus (z. B. CREATE TABLE, INSERT, UPDATE).
     * Fehler werden intern gefangen und ausgegeben.
     *
     * @param sql das auszuführende SQL-Statement
     */
    @SuppressWarnings({"unused","squid:S2077","SqlWithoutDataSource","SqlNoDataSource"})
    public static void onUpdate(final String sql){
        try {
            // Intentionally executing raw SQL provided by caller; suppress analyzer warning
            LiteSQL.stmt.execute(Objects.requireNonNull(sql)); // NOSONAR
        }catch (final SQLException e){
            logger.error("Fehler beim Ausführen eines Updates", e);
        }
    }

    /**
     * Führt eine SQL-Query aus und liefert das ResultSet zurück.
     *
     * @param sql das SELECT-Statement
     * @return das ResultSet oder null, falls ein Fehler auftrat
     */
    @SuppressWarnings({"unused","squid:S2077","SqlWithoutDataSource","SqlNoDataSource"})
    public static ResultSet onQuery(final String sql){
        try {
            // Intentionally executing raw SQL provided by caller; suppress analyzer warning
            return LiteSQL.stmt.executeQuery(Objects.requireNonNull(sql)); // NOSONAR
        }catch (final SQLException e){
            logger.error("Fehler beim Ausführen einer Query", e);
        }
        return null;
    }
}

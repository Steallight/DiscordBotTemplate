package de.steallight.testbot.main;


import de.steallight.testbot.commands.*;
import de.steallight.testbot.listener.SupportListener;
import de.steallight.testbot.listener.VoiceListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {


    public static String PREFIX = "!";
    public static JDA jda;
    public static Bot INSTANCE;


    public Bot() throws LoginException, InterruptedException {
        INSTANCE = this;


        // Token aus Umgebungsvariable lesen (sicherer als hardcodiert)
        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Discord token is not set. Please provide BOT_TOKEN environment variable.");
        }

        jda = JDABuilder.create(token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MODERATION,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_EXPRESSIONS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        ).enableCache(
                CacheFlag.VOICE_STATE,
                CacheFlag.MEMBER_OVERRIDES,
                CacheFlag.CLIENT_STATUS,
                CacheFlag.EMOJI,
                CacheFlag.ACTIVITY
        ).build();

        Guild server = jda.awaitReady().getGuildById("");
        assert server != null;


        ActivityManager activityManager = new ActivityManager();
        LiteSQL.connect();
        SQLManager.onCreate();


        activityManager.loadPresence();

        // intern: Registere Events und Commands
        this.addEvents();
        this.updateCommands(server);


    }

    public static Bot getINSTANCE() {
        return INSTANCE;
    }

    /**
     * Registriert Event-Listener und Command-Handler. Diese Methode ist privat
     * um zu verhindern, dass sie in Unterklassen überschrieben wird — so vermeiden
     * wir "this-escape"-Warnungen beim Aufruf aus dem Konstruktor.
     */
    private void addEvents() {
        jda.addEventListener(new announceCMD());
        jda.addEventListener(new Avatar());
        jda.addEventListener(new ClearCommand());
        jda.addEventListener(new delchannel());
        jda.addEventListener(new devCMD());
        jda.addEventListener(new Help());
        jda.addEventListener(new Hi());
        jda.addEventListener(new PurgeCMD());
        jda.addEventListener(new socials());
        jda.addEventListener(new SupportListener());
        jda.addEventListener(new VoiceListener());
    }

    //Für Slash Commands
    private void updateCommands(Guild server) {
        server.updateCommands()
                .addCommands(
                        /*Commands.slash("set-ticket", "Setzte den TicketChannel")
                                .setDefaultPermissions(DefaultMemberPermissions.DISABLED) <------- Entscheidet ob nur Admins oder jeder den Befehl sehen kann
                                .addOption(OptionType.CHANNEL, "ticketchannel", "Bitte wähle den Channel der als Ticket Channel gesetzt werden soll", true),

                         */
                ).queue();
    }

}

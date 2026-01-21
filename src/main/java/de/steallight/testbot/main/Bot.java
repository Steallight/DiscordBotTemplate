package de.steallight.testbot.main;


import de.steallight.testbot.commands.*;
import de.steallight.testbot.listener.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {


    public static String PREFIX = "!";
    public static JDA jda;
    public static Bot INSTANCE;
    public static TextChannel tc;


    public Bot() throws LoginException, InterruptedException {
        INSTANCE = this;


        jda = JDABuilder.create("",
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


        ActivityManager activityManager = new ActivityManager(this);
        LiteSQL.connect();
        SQLManager.onCreate();

        /*
        builder.setActivity(Activity.playing("Work in Progress"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);

         */

        activityManager.loadPresence();

        this.addEvents();
        this.updateCommands(server);


    }

    //Für Events
    public void addEvents() {
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
    public void updateCommands(Guild server) {
        server.updateCommands()
                .addCommands(
                        /*Commands.slash("set-ticket", "Setzte den TicketChannel")
                                .setDefaultPermissions(DefaultMemberPermissions.DISABLED) <------- Entscheidet ob nur Admins oder jeder den Befehl sehen kann
                                .addOption(OptionType.CHANNEL, "ticketchannel", "Bitte wähle den Channel der als Ticket Channel gesetzt werden soll", true),

                         */
                ).queue();
    }

    public static Bot getINSTANCE() {
        return INSTANCE;
    }

}

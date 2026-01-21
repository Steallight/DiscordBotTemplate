package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

/**
 * Einfacher Informations-Befehl, der den Entwickler des Bots anzeigt.
 * Verwendung: !dev
 */
public class devCMD extends ListenerAdapter {



    /**
     * Sendet ein Embed mit dem Entwickler-Hinweis in den aktuellen Channel.
     *
     * @param e Event-Kontext
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        // Vergleiche den Nachrichtentext (nicht das Message-Objekt) mit dem Befehl
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX + "dev")) {
            EmbedBuilder eb = new EmbedBuilder();

            String mention = "<unknown>";
            if (e.getGuild() != null && e.getGuild().getMemberById("438200912599580675") != null) {
                mention = e.getGuild().getMemberById("438200912599580675").getAsMention();
            }

            eb
                    .setTitle("Der Bot wurde von " + mention + " programmiert")
                    .setColor(Color.WHITE);

            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

}

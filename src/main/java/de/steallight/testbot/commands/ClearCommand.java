package de.steallight.testbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Befehl zum Entfernen einer Anzahl von Nachrichten aus dem aktuellen Channel.
 * Verwendung: !clear <Anzahl>
 * Der Aufrufer benötigt die Permission MESSAGE_MANAGE.
 */
public class ClearCommand extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearCommand.class);

    /**
     * Verarbeitet den Befehl, prüft Rechte und Anzahl-Argument und entfernt
     * die angegebene Anzahl Nachrichten (sofern möglich).
     *
     * @param e Event mit Kontext zur Nachricht
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        MessageChannel tc = e.getChannel().asTextChannel();
        String[] args = e.getMessage().getContentRaw().split(" ");
        EmbedBuilder eb = new EmbedBuilder();
        if (e.getMember() != null && e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            if (args.length == 2) {
                try {
                    int amount = Integer.parseInt(args[1]);
                    e.getChannel().purgeMessages(get(tc, amount));

                    eb
                            .setDescription("Es wurden " + amount + " Nachrichten entfernt.")
                            .setColor(Color.blue);
                    e.getChannel().sendMessageEmbeds(eb.build()).queue(a -> a.delete().queueAfter(3, TimeUnit.SECONDS));
                } catch (NumberFormatException ex) {
                    LOGGER.warn("Ungültige Zahl für clear-Befehl: {}", args[1], ex);
                    e.getChannel().sendMessage("Bitte eine gültige Zahl angeben.").queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
                }
            }
        } else {
            eb
                    .setColor(Color.RED)
                    .setDescription("Dafür fehlen dir die Rechte");
            e.getChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(3,TimeUnit.SECONDS));
        }
    }

    /**
     * Hilfsmethode zum Sammeln der letzten Nachrichten aus dem Channel (nicht gepinnt),
     * die dann an purgeMessages übergeben werden können.
     *
     * @param channel der MessageChannel
     * @param amount Anzahl der zu sammelnden Nachrichten
     * @return Liste der Nachrichten (max. amount + 1)
     */
    public List<Message> get(MessageChannel channel, int amount) {

        List<Message> messages = new ArrayList<>();
        int i = amount + 1;

        for (Message message : channel.getIterableHistory().cache(false)) {
            if (!message.isPinned()) {
                messages.add(message);
                if (--i <= 0) break;
            }
        }

        return messages;

    }

}

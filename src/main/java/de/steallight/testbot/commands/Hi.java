package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * Kleiner Begrüßungsbefehl, der den User im Channel grüßt und die Nachricht
 * nach kurzer Zeit wieder löscht.
 * Verwendung: !hi
 */
public class Hi extends ListenerAdapter {


    /**
     * Sendet eine kurzlebige Begrüßung in den Channel.
     *
     * @param e Event mit Kontext
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"hi")){
           e.getChannel().sendMessage("Hi " +e.getMember().getAsMention() + " ^^").queue(message -> message.delete().queueAfter(3,TimeUnit.SECONDS));
        }
    }

}

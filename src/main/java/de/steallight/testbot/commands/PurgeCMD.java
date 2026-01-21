package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Löscht den Inhalt eines Channels, indem eine Kopie erstellt und die Original-Channe
 * gelöscht wird. Verwendung: !purge
 * Benötigt Permission.MANAGE_CHANNEL.
 */
public class PurgeCMD extends ListenerAdapter {


    /**
     * Führt das Purge-Verhalten aus, wenn ein berechtigter Nutzer den Befehl sendet.
     *
     * @param e Event mit Kontext
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX + "purge")) {
            if (e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                try {
                    Integer position = e.getChannel().asTextChannel().getPosition();
                    e.getChannel().asTextChannel().createCopy().setPosition(position).queue();
                    e.getChannel().delete().queue();
                }catch (NullPointerException ex){ex.printStackTrace();}
            }
        }
    }
}

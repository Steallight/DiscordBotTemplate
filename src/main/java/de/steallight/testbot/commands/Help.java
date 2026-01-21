package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

/**
 * Sendet ein Hilfe-Menü als Private-Message an den anfragenden User.
 * Verwendung: !help
 */
public class Help extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX + "help")) {
            // In Direct Messages ist getMember() null: schütze vor NullPointerException
            if (e.getMember() == null) return;

            final EmbedBuilder eb = new EmbedBuilder();
            if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                eb.setTitle("----Hilfe Menü----");
                eb.setColor(Color.decode("#A900C3"));
                eb.addBlankField(false);
                eb.addField("", "**<> = Benötigt**", false);
                eb.addBlankField(false);
                eb.addField("Server Commands", "", false);
                eb.addField(Bot.PREFIX + "Help", "Zeigt dieses Hilfemenü an", false);
                eb.addField(Bot.PREFIX + "Hi", "Lässt den Bot Hi sagen", false);
                // eb.addField("-Join", "Schickt dir den Link sodass du dich bewerben kannst", false);
                eb.addField(Bot.PREFIX + "Avatar", "Zeigt dir deinen avatar vergrößert an.", false);
                eb.setFooter("made by Steallight");

                e.getMember().getUser().openPrivateChannel().queue(privateChannel1 -> privateChannel1.sendMessageEmbeds(eb.build()).queue());
            } else {
                eb.setTitle("----Hilfe Menü----");
                eb.setColor(Color.decode("#A900C3"));
                eb.addBlankField(false);
                eb.addField("", "**<> = Benötigt**", false);
                eb.addBlankField(false);
                eb.addField("Server Commands", "", false);
                eb.addField(Bot.PREFIX + "Help", "Zeigt dieses Hilfemenü an", false);
                eb.addField(Bot.PREFIX + "Hi", "Lässt den Bot Hi sagen", false);
                eb.addField(Bot.PREFIX + "Join", "Schickt dir den Link sodass du dich bewerben kannst", false);
                eb.addField(Bot.PREFIX + "Avatar", "Zeigt dir deinen Avatar vergrößert an.", false);
                eb.addField(Bot.PREFIX + "React", "Reagiert für dich auf Nachrichten z.B. bei Abstimmungen.", false);
                eb.addField(Bot.PREFIX + "clear <Zahl>", "Löscht für dich die Anzahl an Nachrichten", false);
                eb.addField(Bot.PREFIX + "purge", "Löscht den gesamten Channelinhalt!", false);
                eb.setFooter("made by Steallight");


                e.getMember().getUser().openPrivateChannel().queue(privateChannel1 -> privateChannel1.sendMessageEmbeds(eb.build()).queue());
            }
        }
    }
}

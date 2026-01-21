package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command-Handler für das Posten von Ankündigungen in einen erwähnten Channel.
 * Syntax: !announce @channel Nachricht...
 * Nur Mitglieder mit Permission.MESSAGE_MANAGE können diesen Befehl nutzen.
 */
public class announceCMD extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(announceCMD.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentStripped().equals(Bot.PREFIX+"announce")){
            final String[] args = e.getMessage().getContentRaw().split(" ");

            if (e.getMember() != null && e.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
                Optional<TextChannel> maybeTc = e.getMessage().getMentions().getChannels().stream()
                        .filter(ch -> ch instanceof TextChannel)
                        .map(ch -> (TextChannel) ch)
                        .findFirst();

                if (maybeTc.isPresent()){
                    TextChannel tc = maybeTc.get();

                    try {
                        StringBuilder builder = new StringBuilder();

                        // Baue die Nachricht aus den übergebenen Argumenten zusammen
                        for (int i = 2; i< args.length; i++){
                            builder.append(args[i]).append(" ");
                        }

                        String finishedString = builder.toString().trim();
                        // Lösche den Ursprungsbefehl
                        e.getMessage().delete().queue();

                        EmbedBuilder replyEmbed = new EmbedBuilder();
                        EmbedBuilder announceEmbed = new EmbedBuilder();

                        replyEmbed.setTitle("Die Information wurde gepostet").setColor(Color.GREEN);
                        announceEmbed.setDescription(finishedString).setFooter("posted by " + e.getMember().getEffectiveName());

                        e.getChannel().sendMessageEmbeds(replyEmbed.build()).queue(a -> a.delete().queueAfter(3, TimeUnit.SECONDS));
                        tc.sendMessageEmbeds(announceEmbed.build()).queue();
                    } catch (final NumberFormatException exception) {
                        LOGGER.warn("Fehler beim Parsen in announceCMD: {}", exception.getMessage(), exception);
                        e.getChannel().sendMessage("Fehler beim Verarbeiten des Befehls.").queue();
                    }
                }
            }
        }
    }
}

package de.steallight.testbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class SupportListener extends ListenerAdapter {

    String channelid = "655373012131905550";
    String textid = "773841319805452318";

    private static final Logger logger = LoggerFactory.getLogger(SupportListener.class);

    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {
        try {
            String supportWarteraumID = channelid;
            System.out.println("Support Warteraum ID: " + supportWarteraumID);

            // Null-Check: Wurde ein Channel betreten?
            AudioChannelUnion channelJoined = e.getChannelJoined();
            if (channelJoined == null) {
                // User hat Channel verlassen, nicht betreten
                return;
            }

            // Prüfe ob der richtige Channel betreten wurde
            if (channelJoined.getId().equals(supportWarteraumID)) {
                TextChannel notifyChannel = e.getGuild().getTextChannelById("1279592048751804436");

                if (notifyChannel == null) {
                    System.err.println("Notify Channel nicht gefunden!");
                    return;
                }

                String userID = e.getMember().getId();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Jemand wartet im Support-Warteraum")
                        .setDescription(e.getMember().getAsMention())
                        .setColor(Color.ORANGE);

                net.dv8tion.jda.api.components.buttons.Button acceptBtn = Button.success("accept:supportWarteraum_" + userID, "Annehmen");

                Role supRole = e.getGuild().getRoleById("729280923207729154");
                if (supRole != null){
                    notifyChannel.sendMessage(supRole.getAsMention())
                            .addComponents(ActionRow.of(acceptBtn))
                            .addEmbeds(eb.build())
                            .queue();
                }


            }
        } catch (Exception ex) {
            logger.error("Fehler beim Laden der Config");
        }
    }


}








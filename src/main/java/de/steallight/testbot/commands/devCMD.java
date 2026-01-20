package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class devCMD extends ListenerAdapter {



    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().equals(Bot.PREFIX + "dev")) {
            EmbedBuilder eb = new EmbedBuilder();

            eb
                    .setTitle("Der Bot wurde von " + e.getGuild().getMemberById("438200912599580675").getAsMention() + " programmiert")
                    .setColor(Color.WHITE);

            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

}

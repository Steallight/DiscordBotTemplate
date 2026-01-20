package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class announceCMD extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentStripped().equals(Bot.PREFIX+"announce")){
            final String[] args = e.getMessage().getContentRaw().split(" ");
            final List<GuildChannel> channels = e.getMessage().getMentions().getChannels();

            if (e.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
                if (!channels.isEmpty()){
                    final TextChannel tc = (TextChannel) e.getMessage().getMentions().getChannels().get(0);
                    final String message = args[2];

                    try {
                        StringBuilder builder = new StringBuilder();

                        for (int i = 2; i< args.length; i++){
                            builder.append(args[i]).append(" ");
                        }

                        String finishedString = builder.toString().trim();
                        e.getMessage().delete().queue();

                        EmbedBuilder replyEmbed = new EmbedBuilder();
                        EmbedBuilder announceEmbed = new EmbedBuilder();

                        replyEmbed
                                .setTitle("Die Information wurde gepostet")
                                .setColor(Color.GREEN);

                        announceEmbed
                                .setDescription(finishedString)
                                .setFooter("posted by " +e.getMember().getEffectiveName());

                        e.getChannel().sendMessageEmbeds(replyEmbed.build()).queue(a -> a.delete().queueAfter(3, TimeUnit.SECONDS));
                        tc.sendMessageEmbeds(announceEmbed.build()).queue();
                    }catch (final NumberFormatException exception){
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
}

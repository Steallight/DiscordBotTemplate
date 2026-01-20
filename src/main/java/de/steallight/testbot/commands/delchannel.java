package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class delchannel extends ListenerAdapter {

    EmbedBuilder eb = new EmbedBuilder();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"delchannel")) {
            if (e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                final List<GuildChannel> channels = e.getMessage().getMentions().getChannels();
                final TextChannel tc = (TextChannel) e.getMessage().getMentions().getChannels().get(0);

                if (!channels.isEmpty()){
                    if (e.getChannel() == tc) {
                        deleteChannel(e, tc.getId());
                    }else {
                        e.getChannel().sendMessage("Channel wurde gelöscht").queue(message -> message.delete().queueAfter(3,TimeUnit.SECONDS));
                        deleteChannel(e,tc.getId());
                    }
                }else {
                    e.getChannel().sendMessage("Geb einen Channel an!").queue(message -> message.delete().queueAfter(3,TimeUnit.SECONDS));
                }
            }else {
                e.getChannel().sendMessage("Keine Rechte!").queue(message -> message.delete().queueAfter(3,TimeUnit.SECONDS));
            }
        }
    }

    protected void deleteChannel(MessageReceivedEvent e, String channelID){
        TextChannel guildChannel = e.getGuild().getTextChannelById(channelID);
        if (guildChannel != null){
            guildChannel.delete().queue();
        }
    }


}

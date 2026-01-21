package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.Permission;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Command zum Löschen eines genannten Channels.
 * Verwendung: !delchannel @channel
 * Benötigt Permission.MANAGE_CHANNEL.
 */
public class delchannel extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"delchannel")) {
            // Schütze vor NullPointer wenn der Event aus einem DM kommt
            if (e.getMember() == null) return;

            if (e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                final Optional<TextChannel> maybeTc = e.getMessage().getMentions().getChannels().stream()
                        .filter(ch -> ch instanceof TextChannel)
                        .map(ch -> (TextChannel) ch)
                        .findFirst();

                if (maybeTc.isPresent()){
                    final TextChannel tc = maybeTc.get();

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

    /**
     * Führt das eigentliche Löschen des TextChannels durch, sofern dieser existiert.
     *
     * @param e Event-Kontext
     * @param channelID ID des zu löschenden Channels
     */
    protected void deleteChannel(MessageReceivedEvent e, String channelID){
        TextChannel guildChannel = e.getGuild().getTextChannelById(channelID);
        if (guildChannel != null){
            guildChannel.delete().queue();
        }
    }


}

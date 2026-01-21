package de.steallight.testbot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Listener, der beim Betreten eines bestimmten "Join-Channel" temporäre
 * Voice-Channels für Members erstellt und diese wieder löscht, wenn sie leer sind.
 */
public class VoiceListener extends ListenerAdapter {
    private final static long joinChannel = 774649165300105256L;
    private final List<Long> tempChannels = new ArrayList<>();

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {
        if (e.getChannelJoined() != null)
            onJoin(e.getChannelJoined().asVoiceChannel(), e.getEntity());
        if (e.getChannelLeft() != null)
            onLeave(e.getChannelLeft().asVoiceChannel());
        
    }

    /**
     * Wird aufgerufen, wenn ein Member dem Join-Channel beitritt. Erstellt einen
     * temporären Voice-Channel mit Rechten für den Member und verschiebt ihn hinein.
     *
     * @param joined der betretene VoiceChannel
     * @param member der Member, der beigetreten ist
     */
    public void onJoin(VoiceChannel joined, Member member) {
        if(joined.getIdLong() == joinChannel) {
            EnumSet<Permission> allow = EnumSet.of(Permission.MANAGE_CHANNEL);

            Category cat = joined.getParentCategory();
            if (cat == null) return;
            cat.createVoiceChannel("|⏳ " + member.getEffectiveName())
                    .flatMap(channel -> {
                        channel.getManager().setUserLimit(joined.getUserLimit()).queue();
            channel.getManager().putPermissionOverride(member, allow, null).queue();
                        tempChannels.add(channel.getIdLong());
                        return channel.getGuild().moveVoiceMember(member, channel);
                    }).queue();
        }
    }

    /**
     * Entfernt temporäre Voice-Channels wieder, wenn sie leer sind.
     *
     * @param channel der verlassene VoiceChannel
     */
    public void onLeave(VoiceChannel channel) {
        if (channel.getMembers().isEmpty()) {
            if (tempChannels.remove(channel.getIdLong())) {
                channel.delete().queueAfter(3, TimeUnit.SECONDS);
            }
        }
    }
}

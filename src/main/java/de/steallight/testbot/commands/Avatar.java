package de.steallight.testbot.commands;


import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;
import java.util.Objects;

/**
 * Gibt die Avatar-URL des erwähnten Members zurück.
 * Verwendung: !avatar @Benutzer
 */
public class Avatar extends ListenerAdapter {


    /**
     * Bei Empfang des Befehls wird die Avatar-URL des ersten erwähnten Members
     * in den Textchannel gepostet. Wenn kein Member erwähnt wurde, passiert nichts.
     *
     * @param e Event mit Nachricht und Kontext
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"avatar")){
            Optional<Member> maybeMember = e.getMessage().getMentions().getMembers().stream().findFirst();
            if (maybeMember.isPresent()){
                Member mentionedMember = maybeMember.get();
                String avatarUrl = Objects.requireNonNullElse(mentionedMember.getAvatarUrl(), "");
                if (!avatarUrl.isEmpty()) {
                    e.getChannel().sendMessage(avatarUrl).queue();
                } else {
                    e.getChannel().sendMessage("Dieser Benutzer hat kein Avatar.").queue();
                }
            }
        }
    }

}

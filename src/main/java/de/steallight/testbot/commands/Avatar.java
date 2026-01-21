package de.steallight.testbot.commands;


import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
            if (!e.getMessage().getMentions().getMembers().isEmpty()){
                Member mentionedMember = e.getMessage().getMentions().getMembers().get(0);
                if (mentionedMember != null){
                    String avatarUrl = mentionedMember.getAvatarUrl();
                    if (avatarUrl != null) {
                        e.getChannel().sendMessage(avatarUrl).queue();
                    } else {
                        e.getChannel().sendMessage("Dieser Benutzer hat kein Avatar.").queue();
                    }
                }
            }
        }
    }

}

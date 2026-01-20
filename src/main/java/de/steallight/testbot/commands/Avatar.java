package de.steallight.testbot.commands;


import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Avatar extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"avatar")){
            Member mentionedMember = e.getMessage().getMentions().getMembers().get(0);
            if (mentionedMember != null){
                e.getChannel().sendMessage(mentionedMember.getAvatarUrl()).queue();
            }
        }
    }

}

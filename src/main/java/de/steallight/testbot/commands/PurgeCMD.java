package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PurgeCMD extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX + "purge")) {
            if (e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                try {
                    Integer position = e.getChannel().asTextChannel().getPosition();
                    e.getChannel().asTextChannel().createCopy().setPosition(position).queue();
                    e.getChannel().delete().queue();
                }catch (NullPointerException ex){ex.printStackTrace();}
            }
        }
    }
}

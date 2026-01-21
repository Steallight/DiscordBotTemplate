package de.steallight.testbot.commands;

import de.steallight.testbot.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class socials extends ListenerAdapter {

    String instagram = "https://instagram.com/";
    String twitchURL = "https://twitch.tv/";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equals(Bot.PREFIX+"socials")){
            EmbedBuilder info = new EmbedBuilder();
            EmbedBuilder insta = new EmbedBuilder();
            EmbedBuilder twitch = new EmbedBuilder();

            info
                    .setTitle("Meine Socials")
                    .setColor(Color.WHITE);
            insta
                    .setTitle("Instagram", instagram+"steallight")
                    .setColor(Color.decode("#E1306C"))
                    .setThumbnail("https://cdn.icon-icons.com/icons2/1826/PNG/512/4202090instagramlogosocialsocialmedia-115598_115703.png");

            twitch
                    .setTitle("Twitch", twitchURL+"yugusaa")
                    .setColor(Color.decode("#9933ff"))
                    .setThumbnail("https://media-exp1.licdn.com/dms/image/C560BAQHm82ECP8zsGw/company-logo_200_200/0?e=2159024400&v=beta&t=r94Gy3UzJ4RMyHVIeECB9Q67gfAFC_FZVq9uCenVHXs");

            e.getChannel().asTextChannel().sendMessageEmbeds(info.build(),insta.build(),twitch.build()).queue();
        }
    }
}

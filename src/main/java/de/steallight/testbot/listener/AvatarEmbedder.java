package de.steallight.testbot.listener;

import net.coobird.thumbnailator.Thumbnails;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

public class AvatarEmbedder extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("avatarembed")) {
            event.deferReply(false).queue();
            Member targetUser = event.getOption("user") != null ?
                    event.getOption("user").getAsMember() : event.getMember();

            try {
                byte[] imageBytes = embedAvatar(targetUser, new File("template.png"),event.getGuild());

                event.getHook().sendMessageEmbeds(new EmbedBuilder()
                                .setTitle(targetUser.getEffectiveName() + "'s Profil")
                                .setImage("attachment://profile.png")
                                .setColor(Color.MAGENTA)
                                .build())
                        .addFiles(FileUpload.fromData(imageBytes, "profile.png"))
                        .queue();
            } catch (Exception e) {
                event.getHook().sendMessage("❌ Fehler beim Generieren: " + e.getMessage())
                        .setEphemeral(true).queue();
            }
        }
    }

    public static byte[] embedAvatar(Member user, File templateFile, Guild guild) throws Exception {
        // Template laden (2496x1664 oder deine Größe)
        BufferedImage template = ImageIO.read(templateFile);
        int width = template.getWidth();
        int height = template.getHeight();

        // User-Avatar (größer für perfekte Zentrierung)
        String avatarUrl = user.getEffectiveAvatarUrl() + "?size=1024";
        BufferedImage avatarRaw = ImageIO.read(new URI(avatarUrl).toURL());

        Graphics2D g2d = template.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Avatar-Größe + Position (zentriert)
        int avatarSize = 1024;
        int avatarX = (width - avatarSize) / 2;  // Leicht rechts
        int avatarY = (height - avatarSize) / 2;                 // Leicht höher

        // THUMBNAILATOR: Perfekt ausfüllen + zentrieren
        ByteArrayOutputStream resizedBaos = new ByteArrayOutputStream();
        Thumbnails.of(avatarRaw)
                .size(avatarSize , avatarSize)  // Etwas größer
                .keepAspectRatio(false)                  // Voll ausfüllen!
                .outputQuality(0.98)
                .outputFormat("png")
                .toOutputStream(resizedBaos);

        BufferedImage resizedAvatar = ImageIO.read(new ByteArrayInputStream(resizedBaos.toByteArray()));


        // Rund maskieren
        BufferedImage maskedAvatar = new BufferedImage(avatarSize, avatarSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D maskG = maskedAvatar.createGraphics();
        maskG.setClip(new RoundRectangle2D.Double(0, 0, avatarSize, avatarSize, avatarSize, avatarSize));
        maskG.drawImage(resizedAvatar, 0, 0, avatarSize, avatarSize, null);
        maskG.dispose();

        // Avatar ins Template einfügen
        g2d.drawImage(maskedAvatar, avatarX, avatarY, null);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 100));
        String tagline = "Willkommen auf " + guild.getName()+"!";
        FontMetrics mf = g2d.getFontMetrics();
        int taglineX = (width - mf.stringWidth(tagline)) / 2;
        int taglineY = avatarY + avatarSize + 180;
        g2d.drawString(tagline, taglineX, taglineY);

        // Username (dynamisch, zentriert)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 100));
        String username = user.getEffectiveName();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (width - fm.stringWidth(username)) / 2;
        int textY = avatarY + avatarSize + 320;
        g2d.drawString(username, textX, textY);

        g2d.dispose();

        // Final PNG
        ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
        ImageIO.write(template, "png", finalBaos);
        return finalBaos.toByteArray();
    }

}

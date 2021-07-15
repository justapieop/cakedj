package me.justapie.cakedj.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

public class EmbedUtils {
    public static void sendEmbed(SlashCommandEvent event, Color color, String desc) {
        MessageEmbed embed = new EmbedBuilder().setColor(color).setDescription(desc).build();
        event.deferReply().queue((resp) -> resp.sendMessageEmbeds(embed).queue());
    }

    public static MessageEmbed createEmbed(Color color, String desc) {
        return new EmbedBuilder().setColor(color).setDescription(desc).build();
    }
}

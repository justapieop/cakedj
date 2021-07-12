package me.justapie.cakedj.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

public class EmbedUtils {
    public static void sendEmbed(SlashCommandEvent event, Color color, String desc) {
        event.deferReply().addEmbeds(
                new EmbedBuilder().setColor(color).setDescription(desc).build()
        ).queue();
    }
}

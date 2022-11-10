package ru.kiefernpilz.ticket;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import ru.kiefernpilz.ticket.listener.TicketListener;

public class TicketBot {

    @SneakyThrows
    public void launchJda() {
        val TOKEN = "{TOKEN}";
        val jda = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new TicketListener())
                .setAutoReconnect(true)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();

        jda.getPresence().setPresence(Activity.playing("Ticket system bot"), true);
    }
}

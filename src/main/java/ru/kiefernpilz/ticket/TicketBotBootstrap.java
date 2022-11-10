package ru.kiefernpilz.ticket;

import lombok.val;

public class TicketBotBootstrap {

    public static void main(String[] args) {
        val ticketBot = new TicketBot();
        ticketBot.launchJda();
        System.out.println("Ticket system bot launched!");
    }
}

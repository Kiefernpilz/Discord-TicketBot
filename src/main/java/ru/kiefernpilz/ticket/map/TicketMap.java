package ru.kiefernpilz.ticket.map;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;

@UtilityClass
public class TicketMap {
    public final ArrayList<String> ticketMap = new ArrayList<>();

    /**
     * Добавить пользователя в мапу
     * @param idDiscord - игрок
     */
    public void addPlayer(String idDiscord) {
        ticketMap.add(idDiscord);
    }
    /**
     * Удалить пользователя из мапы
     */
    public void removePlayer(String idDiscord) {
        ticketMap.remove(idDiscord);
    }
    /**
     * Проверить пользователя в мапе
     * @param idDiscord - игрок
     */
    public boolean hasPlayer(String idDiscord) {
        return ticketMap.contains(idDiscord);
    }

}

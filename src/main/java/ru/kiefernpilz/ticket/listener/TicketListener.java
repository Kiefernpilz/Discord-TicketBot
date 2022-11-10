package ru.kiefernpilz.ticket.listener;

import lombok.val;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import ru.kiefernpilz.ticket.map.TicketMap;
import ru.kiefernpilz.ticket.utility.CooldownUtil;
import ru.kiefernpilz.ticket.utility.NumberUtil;

import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Блокаем сообщения от ботов и вебхука
        if (event.isWebhookMessage() || event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!createticket")) {
            val buttonOpenTicket = Button.secondary("button-open-ticket", "Открыть тикет")
                    .withEmoji(Emoji.fromUnicode("U+1F4E9"));
            event.getChannel().sendMessage("""
                            Считаешь свою блокировку на сервере неверным?
                            Обнаружил баг или ошибку в локализации?
                            Не пришел товар, купленный на нашем сайте?
                            Или у вас имеется вопрос, не относящейся к выше?
                                                    
                            Тогда, создайте тикет кликнув по кнопке ниже,
                            и мы постараемся как можно быстрее ответить
                            """)
                    .setActionRow(buttonOpenTicket)
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        val idMember = event.getMember().getId();
        val idButton = event.getButton().getId();
        val guild = event.getGuild();
        val cooldown = "discord_ticket_" + event.getMember().getId();
        val message = """
                Вы создали тикет, ожидайте, мы скоро ответим!
                Напоминаем что у нас разные часовые пояса,
                поэтому ответ на ваш вопрос может придти не быстро!
                """;

        if (Objects.equals(idButton, "button-open-ticket")) {
            // Проверяем пользователя на имеющейся задержку
            if (CooldownUtil.hasCooldown(cooldown)) {
                event.getInteraction().deferReply(true).complete().sendMessage("Вы можете создать следующий тикет, только после " + NumberUtil.getTime(CooldownUtil.getCooldown(cooldown)) + " часов с момента закрытия последнего вашего тикета").queue();
                return;
            }
            // Проверяем пользователя на имеющейся активный тикет
            if (TicketMap.hasPlayer(idMember)) {
                event.getInteraction().deferReply(true).complete().sendMessage("У вас уже имеется тикет").queue();
                return;
            }

            event.getInteraction().deferReply(true).complete().sendMessage("Тикет открыт").queue();

            guild.createTextChannel(event.getMember().getEffectiveName() + " ticket", guild.getCategoryById(1039308844997804072L))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete().sendMessage(message).setActionRow(Button.danger("button-closed-ticket", "Закрыть тикет")) // Отправляем сообщение после создания текстового канала
                    .queue();

            TicketMap.addPlayer(idMember);
        }

        if (Objects.equals(idButton, "button-closed-ticket")) {
            event.getInteraction().getChannel().delete().queue();
            TicketMap.removePlayer(idMember);
            CooldownUtil.putCooldown(cooldown, TimeUnit.HOURS.toMillis(6));
        }
    }
}
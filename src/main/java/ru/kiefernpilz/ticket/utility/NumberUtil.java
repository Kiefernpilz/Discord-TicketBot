package ru.kiefernpilz.ticket.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class NumberUtil {
    /**
     * Получить грамотно составленное время из
     * количества секунд
     *
     * @param seconds - кол-во секунд
     */
    public String getTime(int seconds) {
        int minutes = 0, hours = 0, days = 0, weeks = 0, months = 0, years = 0;

        if (seconds >= 60) {
            int i = seconds / 60;
            seconds -= 60 * i;
            minutes += i;
        }

        if (minutes >= 60) {
            int i = minutes / 60;
            minutes -= 60 * i;
            hours += i;
        }

        if (hours >= 24) {
            int i = hours / 24;
            hours -= 24 * i;
            days += i;
        }

        if (days >= 7) {
            int i = days / 7;
            days -= 7 * i;
            weeks += i;
        }

        if (weeks >= 4) {
            int i = weeks / 4;
            weeks -= 4 * i;
            months += i;
        }

        if (months >= 12) {
            int i = months / 12;
            months -= 12 * i;
            years += i;
        }

        val builder = new StringBuilder();

        if (years != 0) {
            builder.append(formatting(years, NumberTimeUnit.YEARS)).append(" ");
        }

        if (months != 0) {
            builder.append(formatting(months, NumberTimeUnit.MONTHS)).append(" ");
        }

        if (weeks != 0) {
            builder.append(formatting(weeks, NumberTimeUnit.WEEKS)).append(" ");
        }

        if (days != 0) {
            builder.append(formatting(days, NumberTimeUnit.DAYS)).append(" ");
        }

        if (hours != 0) {
            builder.append(formatting(hours, NumberTimeUnit.HOURS)).append(" ");
        }

        if (minutes != 0) {
            builder.append(formatting(minutes, NumberTimeUnit.MINUTES)).append(" ");
        }

        if (seconds != 0) {
            builder.append(formatting(seconds, NumberTimeUnit.SECONDS));
        }

        return builder.toString();
    }
    /**
     * Получить грамотно составленное время из
     * количества миллисекунд, переведенные в секунды
     *
     * @param millis - кол-во миллисекунд
     */
    public String getTime(long millis) {
        return getTime((int) millis / 1000);
    }
    /**
     * Преобразовать число в грамотно составленное
     * словосочетание
     *
     * @param number - число
     * @param single    - словосочетание, если число закаончивается на 1
     * @param twenty    - словосочетание, если число закаончивается на 2
     * @param other  - словосочетание, если число закаончивается на 5
     */
    public String formatting(int number, String single, String twenty, String other) {
        if (number % 100 > 10 && number % 100 < 15) {
            return number + " " + other;
        }
        switch (number % 10) {
            case 1 -> {
                return number + " " + single;
            }
            case 2, 3, 4 -> {
                return number + " " + twenty;
            }
            default -> {
                return number + " " + other;
            }
        }
    }
    /**
     * Преобразовать число в грамотно составленное
     * словосочетание
     *
     * @param number - число
     * @param unit   - словосочетание
     */
    public String formatting(int number, NumberTimeUnit unit) {
        return formatting(number, unit.getOne(), unit.getTwo(), unit.getOther());
    }
    @RequiredArgsConstructor
    @Getter
    public enum NumberTimeUnit {
        SECONDS("секунда", "секунды", "секунд"),
        MINUTES("минута", "минуты", "минут"),
        HOURS("час", "часа", "часов"),
        DAYS("день", "дня", "дней"),
        WEEKS("неделя", "недели", "недель"),
        MONTHS("месяц", "месяца", "месяцев"),
        YEARS("год", "года", "лет");

        private final String one, two, other;
    }
}

package client;

import model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class OrderGenerator {

    public static Order getRandomOrder() {
        return getRandomOrder(null);
    }

    public static Order getRandomOrder(List<String> colors) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        return new Order(
                "Сергей" + suffix,
                "Никитич" + suffix,
                "Test address " + suffix,
                "13",
                "+7900" + generateSixDigits(suffix),
                3,
                LocalDate.now().plusDays(1).toString(),
                "Тестовый комментарий " + suffix,
                colors
        );
    }

    private static String generateSixDigits(String seed) {
        String digits = String.valueOf(Math.abs(seed.hashCode()));
        return digits.length() >= 6
                ? digits.substring(0, 6)
                : String.format("%06d", Integer.parseInt(digits));
    }
}
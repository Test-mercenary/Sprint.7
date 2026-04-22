package client;

import model.Courier;
import java.util.UUID;

public class CourierGenerator {

    public static Courier getRandom() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new Courier(
                "Mens" + uniqueSuffix,
                "mem2333" + uniqueSuffix,
                "Name_" + uniqueSuffix
        );
    }
}

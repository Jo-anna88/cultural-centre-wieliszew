package pl.joannaz.culturalcentrewieliszew.culturalevent;

import pl.joannaz.culturalcentrewieliszew.address.MockAddress;

import java.math.BigDecimal;
import java.util.List;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

// cultural event sample data
public class MockCulturalEvent {
    public static CulturalEvent culturalEvent = new CulturalEvent(
            "assets/images/cultural-event1.jpg",
            "Event1",
            "2024-07-07",
            SIMPLE_TEXT,
            BigDecimal.valueOf(25),
            MockAddress.addressList.get(0),
            10
    );

    public static List<CulturalEvent> culturalEventList = List.of(
            new CulturalEvent(
                    "assets/images/cultural-event1.jpg",
                    "Event1",
                    "2024-07-07",
                    SIMPLE_TEXT,
                    BigDecimal.valueOf(25),
                    MockAddress.addressList.get(0),
                    10),
            new CulturalEvent(
                    "assets/images/cultural-event2.jpg",
                    "Event2",
                    "2024-11-20",
                    SIMPLE_TEXT,
                    BigDecimal.valueOf(50),
                    MockAddress.addressList.get(1),
                    100),
            new CulturalEvent(
                    "assets/images/cultural-event3.jpg",
                    "Event3",
                    "2024-08-10",
                    SIMPLE_TEXT,
                    BigDecimal.valueOf(75),
                    MockAddress.addressList.get(2),
                    50),
            new CulturalEvent(
                    "assets/images/cultural-event-default.jpg",
                    "Event4",
                    "2024-09-13",
                    SIMPLE_TEXT,
                    BigDecimal.valueOf(100),
                    MockAddress.addressList.get(3),
                    25)
    );
}

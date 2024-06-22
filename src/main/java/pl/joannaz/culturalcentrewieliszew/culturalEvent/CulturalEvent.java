package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.address.Address;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "cultural_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="CulturalEvent")
public class CulturalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name="img_source",
            nullable = false
    )
    private String imgSource;

    @Column(
            name="name",
            nullable = false
    )
    private String name;

    @Column(
            name="date",
            nullable = false
    )
    private LocalDate date;

    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name="price",
            nullable = false
    )
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)  // "By default, @ManyToOne associations use the FetchType.EAGER strategy, which can lead to N+1 query issues or fetching more data than necessary"
    @JoinColumn(
            name = "address_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "ADDRESS_ID_FK")
    )
    private Address address;

    public CulturalEvent(CulturalEventDTO culturalEventDTO) {
        this.imgSource = culturalEventDTO.getImgSource();
        this.name = culturalEventDTO.getName();
        this.date = culturalEventDTO.getDate();
        this.description = culturalEventDTO.getDescription();
        this.price = culturalEventDTO.getPrice();
        // address set with usage of setter
    }

    public CulturalEvent(String imgSource, String name, String date, String description, BigDecimal price, Address address) {
        this.imgSource = imgSource;
        this.name = name;
        this.date = LocalDate.parse(date);
        this.description = description;
        this.price = price;
        this.address = address;
    }
}

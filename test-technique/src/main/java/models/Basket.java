package models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL)
    private List<BasketItem> items = new ArrayList<>();

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrix() * item.getQuantity())
                .sum();
    }

}

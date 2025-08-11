package dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String nom;
    private double prix;
    private int quantiteStock;
    private Long categoryId;
}

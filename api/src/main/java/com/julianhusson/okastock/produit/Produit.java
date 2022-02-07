package com.julianhusson.okastock.produit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.utils.Auditable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Produit extends Auditable implements Serializable{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column
    @NotNull(message = "Un titre est obligatoire.")
    private String titre;

    @Column
    @NotNull(message = "Une description est obligatoire.")
    private String description;

    @Column
    @ColumnDefault("0.00")
    private double prix;

    @Column
    @ColumnDefault("0")
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    @JsonIgnoreProperties("produits")
    private Categorie categorie;
}

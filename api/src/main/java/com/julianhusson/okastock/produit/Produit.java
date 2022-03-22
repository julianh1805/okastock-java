package com.julianhusson.okastock.produit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import com.julianhusson.okastock.utils.Auditable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column(unique = true, updatable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column
    @NotBlank(message = "Un titre est obligatoire pour créer un produit.")
    private String titre;

    @Column
    @NotBlank(message = "Une description est obligatoire pour créer un produit.")
    private String description;

    @Column
    @DecimalMin(value = "0.01", message = "Le prix doit être égale ou supérieur à 1 centime.")
    private BigDecimal prix;

    @Column
    @ColumnDefault("0")
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonIgnoreProperties("produits")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, updatable = false)
    private Utilisateur utilisateur;
}

package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.utils.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
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
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Categorie categorie;
}

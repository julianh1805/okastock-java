package com.julianhusson.okastock.categorie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.julianhusson.okastock.produit.Produit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Categorie implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique = true, updatable = false)
    private String nom;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("categorie")
    private Collection<Produit> produits = new HashSet<>();
}

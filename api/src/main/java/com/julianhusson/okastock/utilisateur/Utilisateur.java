package com.julianhusson.okastock.utilisateur;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.julianhusson.okastock.utils.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column
    @NotBlank(message = "Un nom d'entreprise est obligatoire pour s'inscrire.")
    private String nom;

    @Column(unique = true, updatable = false)
    @NotNull(message = "Un SIRET est obligatoire pour s'inscrire.")
    private Long siret;

    @Column
    @NotNull(message = "Un code postal est obligatoire pour s'inscrire.")
    private int codePostal;

    @Column
    @NotNull(message = "Un telephone est obligatoire pour s'inscrire.")
    private Long telephone;

    @Column
    @NotBlank(message = "Un site est obligatoire pour s'inscrire.")
    private String site;

    @Column
    @NotBlank(message = "Un logo est obligatoire pour s'inscrire.")
    private String logo;

    @Column
    private boolean rgpd;

    @Column(unique = true)
    @NotBlank(message = "Un email est obligatoire pour s'inscrire.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Un mot de passe est obligatoire pour s'inscrire.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;

    //@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    //private List<Produit> produits;
}

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
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur extends Auditable implements Serializable {

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
    @Size(min = 3, max = 30, message = "Le nom d'entreprise doit faire entre 3 et 30 caractères.")
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
    @Pattern(regexp = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?$", message = "Le site web est invalide. Le format devrait être http://(www).siteweb.com")
    private String site;

    @Column
    @NotBlank(message = "Un logo est obligatoire pour s'inscrire.")
    private String logo;

    @Column
    private boolean rgpd = false;

    @Column(unique = true)
    @NotBlank(message = "Un email est obligatoire pour s'inscrire.")
    @Email(message = "L'email est invalide", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Un mot de passe est obligatoire pour s'inscrire.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;

    //@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    //private List<Produit> produits;
}

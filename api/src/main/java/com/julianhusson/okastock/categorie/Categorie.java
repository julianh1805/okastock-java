package com.julianhusson.okastock.categorie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categorie")
public class Categorie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ColumnDefault("random_uuid()")
    @Column(unique = true)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(unique = true, updatable = false)
    private String nom;
}

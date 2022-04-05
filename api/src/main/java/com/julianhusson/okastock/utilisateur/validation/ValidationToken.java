package com.julianhusson.okastock.utilisateur.validation;

import com.julianhusson.okastock.utilisateur.Utilisateur;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class ValidationToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "utilisateur_id")
    private Utilisateur utilisateur;

    public ValidationToken(Utilisateur utilisateur) {
        this.token = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
        this.confirmedAt = null;
        this.utilisateur = utilisateur;
    }
}

package com.julianhusson.okastock.role;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.categorie.CategorieRepository;
import com.julianhusson.okastock.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Sql("/role-data.sql")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    @Test
    void itShouldFindByNom() {
        //Given
        String roleName = "ROLE_USER";
        //When
        Optional<Role> role = underTest.findByNom(roleName);
        //Then
        assertThat(role).isPresent();
        assertThat(role.get().getNom()).isEqualTo(roleName);
    }

    @Test
    void itShouldNotFindByNom() {
        //Given
        String otherRoleName = "ROLE_ADMIN";
        //When
        Optional<Role> role = underTest.findByNom(otherRoleName);
        //Then
        assertThat(role).isEmpty();
    }
}
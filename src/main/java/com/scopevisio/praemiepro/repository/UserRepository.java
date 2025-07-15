package com.scopevisio.praemiepro.repository;

import com.scopevisio.praemiepro.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @Query("""
            SELECT u FROM User u
            JOIN u.authorities a
            WHERE a.name = 'ROLE_USER'
            AND NOT EXISTS (
                SELECT 1 FROM Authority a2
                WHERE a2 MEMBER OF u.authorities AND a2.name = 'ROLE_ADMIN'
            )
            """)
    List<User> findAllCustomers();
}

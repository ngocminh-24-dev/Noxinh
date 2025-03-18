package org.company.noxinh.repository;

import org.company.noxinh.entity.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Account token repository.
 */
@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Integer> {
    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<AccountToken> findByRefreshToken(String token);
}

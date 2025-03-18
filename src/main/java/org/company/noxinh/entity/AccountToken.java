package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.constants.TokenType;

import java.io.Serializable;

/**
 * The type Account token.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "[account_token]")
@Entity
public class AccountToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "account_id")
    private int accountId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "revoked")
    private boolean revoked;
}

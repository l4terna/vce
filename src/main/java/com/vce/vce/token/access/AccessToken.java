package com.vce.vce.token.access;

import com.vce.vce.token.shared.TokenEntity;
import com.vce.vce.usersession.UserSession;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "access_tokens")
public class AccessToken extends TokenEntity {

    @ManyToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}

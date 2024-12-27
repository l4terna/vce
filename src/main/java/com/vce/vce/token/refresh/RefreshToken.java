package com.vce.vce.token.refresh;


import com.vce.vce.token.shared.TokenEntity;
import com.vce.vce.usersession.UserSession;
import jakarta.persistence.*;
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
@Table(name = "refresh_tokens")
public class RefreshToken extends TokenEntity {

    @OneToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}

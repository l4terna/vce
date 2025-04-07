package com.laterna.connexemain.v1.token.refresh;


import com.laterna.connexemain.v1.token.shared.Token;
import com.laterna.connexemain.v1.usersession.UserSession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@DiscriminatorValue("REFRESH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class RefreshToken extends Token {

    @OneToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}

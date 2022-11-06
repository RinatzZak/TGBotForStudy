package org.rinatzzak.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.rinatzzak.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserState state;
}

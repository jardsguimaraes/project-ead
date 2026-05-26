package com.ead.authuser.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

import com.ead.authuser.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_ROLES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleType roleName;

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }

}

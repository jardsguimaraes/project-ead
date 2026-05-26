package com.ead.authuser.services.impl;

import org.springframework.stereotype.Service;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.repositories.RoleRepository;
import com.ead.authuser.services.RoleService;

import jakarta.ws.rs.NotFoundException;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleModel findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType).orElseThrow(() -> new NotFoundException("Error: Role is not found!"));
    }
}

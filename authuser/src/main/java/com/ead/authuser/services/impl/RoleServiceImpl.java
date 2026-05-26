package com.ead.authuser.services.impl;

import org.springframework.stereotype.Service;

import com.ead.authuser.repositories.RoleRepository;
import com.ead.authuser.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}

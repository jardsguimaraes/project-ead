package com.ead.course.services.impl;

import org.springframework.stereotype.Service;

import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService {

    final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    
}

package com.ead.course.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.dots.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleRecordDto moduleRecordDto, CourseModel courseModel);

    List<ModuleModel> findAllModuleIntoCourse(UUID courseId);

    ModuleModel findModuleIntoCourse(UUID moduleId, UUID courseId);

    ModuleModel update(ModuleRecordDto moduleRecordDto, ModuleModel moduleModel);

    ModuleModel findById(UUID moduleId);

    Page<ModuleModel> findAllModulesIntoCourse(Specification<ModuleModel> spec, Pageable pageable);
}

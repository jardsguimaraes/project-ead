package com.ead.course.services;

import java.util.List;
import java.util.UUID;

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
}

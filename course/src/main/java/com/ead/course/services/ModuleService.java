package com.ead.course.services;

import com.ead.course.dots.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleRecordDto moduleRecordDto, CourseModel courseModel);

}

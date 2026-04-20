package com.ead.course.controlles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dots.ModuleRecordDto;
import com.ead.course.especifications.SpecificationTemplate;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class ModuleController {

    final ModuleService moduleService;
    final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @Transactional
    @PostMapping("/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid ModuleRecordDto moduleRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(moduleService.save(moduleRecordDto, courseService.findById(courseId)));
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
            SpecificationTemplate.ModuleSpec spec, Pageable pageable) {
        var modulePageModel = moduleService
                .findAllModulesIntoCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable);

        if (!modulePageModel.isEmpty()) {
            for (ModuleModel model : modulePageModel) {
                model.add(linkTo(methodOn(ModuleController.class).getOneModule(courseId, model.getModuleId()))
                        .withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(modulePageModel);
    }

    @GetMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<ModuleModel> getOneModule(@PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findModuleIntoCourse(moduleId, courseId));
    }

    @Transactional
    @DeleteMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId) {
        moduleService.delete(moduleService.findModuleIntoCourse(moduleId, courseId));
        return ResponseEntity.status(HttpStatus.OK)
                .body("Module deleted successfully.");
    }

    @Transactional
    @PutMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId, @RequestBody @Valid ModuleRecordDto moduleRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.update(moduleRecordDto,
                moduleService.findModuleIntoCourse(moduleId, courseId)));
    }

}

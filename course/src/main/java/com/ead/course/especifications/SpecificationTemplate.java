package com.ead.course.especifications;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {

    @And({
            @Spec(path = "CourseLevel", spec = Equal.class),
            @Spec(path = "CourseStatus", spec = Equal.class),
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "userInstructor", spec = Equal.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {
    }

    @Spec(path = "title", spec = LikeIgnoreCase.class)
    public interface ModuleSpec extends Specification<ModuleModel> {
    }

    @And({
            @Spec(path = "title", spec = LikeIgnoreCase.class),
            @Spec(path = "description", spec = LikeIgnoreCase.class)
    })
    public interface LessonSpec extends Specification<LessonModel> {
    }

    @And({
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = LikeIgnoreCase.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "userType", spec = Equal.class)
    })
    public interface UserSpec extends Specification<UserModel> {
    }

    // Arquitetura apresentada no curso, mais complexa(por ter um ROOT a mais) e
    // propicia a falhar
    // public static Specification<ModuleModel> moduleCourseId(final UUID courseId)
    // {
    // return (root, query, criteriaBuilder) -> {
    // if (query == null) {
    // return criteriaBuilder.conjunction();
    // }
    // query.distinct(true);
    // Root<ModuleModel> module = root;
    // Root<CourseModel> course = query.from(CourseModel.class);
    // Expression<Collection<ModuleModel>> courseModules = course.get("modules");
    // return criteriaBuilder.and(criteriaBuilder.equal(course.get("courseId"),
    // courseId),
    // criteriaBuilder.isMember(module, courseModules));
    // };
    // }

    // Arquitetura apresentada no curso, mais complexa(por ter um ROOT a mais) e
    // propicia a falhar
    // public static Specification<LessonModel> lessonModuleId(final UUID moduleId)
    // {
    // return (root, query, criteriaBuilder) -> {
    // if (query == null) {
    // return criteriaBuilder.conjunction();
    // }
    // query.distinct(true);
    // Root<LessonModel> lesson = root;
    // Root<ModuleModel> module = query.from(ModuleModel.class);
    // Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
    // return criteriaBuilder.and(criteriaBuilder.equal(module.get("moduleId"),
    // moduleId),
    // criteriaBuilder.isMember(lesson, moduleLessons));
    // };
    // }

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }

            query.distinct(true);
            Join<ModuleModel, CourseModel> courseJoin = root.join("courses");
            return cb.equal(courseJoin.get("courseId"), courseId);
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }

            query.distinct(true);
            Join<LessonModel, ModuleModel> moduleJoin = root.join("module");
            return cb.equal(moduleJoin.get("moduleId"), moduleId);
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }

            Root<CourseModel> course = root;
            Root<UserModel> user = query.from(UserModel.class);
            Expression<Collection<CourseModel>> usersCourses = user.get("courses");
            return cb.and(cb.equal(user.get("userId"), userId), cb.isMember(course, usersCourses));
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            
            query.distinct(true);
            Root<UserModel> user = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<UserModel>> coursesUsers = course.get("users");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(user, coursesUsers));
        };
    }

}

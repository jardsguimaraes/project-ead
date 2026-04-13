package com.ead.course.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.course.models.ModuleModel;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    // Utilizado para um conportamento Ansioso mesmo que esteja configurado com Lazy
    // @EntityGraph(attributePaths = {"course"})
    // ModuleModel findByTitle(String title);

    /*
     * Para uma consultas utilizar o @Query, caso seja necessário alterar ou deletar
     * utilizar o @Modifying associado como @Query
     * 
     * @Modifying
     * 
     * @Query(value =
     * "delete from tb_modules where course_course_id = :course_id""",
     * nativeQuery = true)
     */
    @Query(value = """
            select
                *
            from
                tb_modules
            where
                course_course_id = :course_id""", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("course_id") UUID course_id);

    @Query(value = """
            select
                *
            from
                tb_modules
            where
                course_course_id = :course_id and
                module_id = :module_id
            """, nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("module_id") UUID module_id, @Param("course_id") UUID course_id);
}

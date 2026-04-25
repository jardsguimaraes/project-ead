package com.ead.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
// import org.hibernate.annotations.OnDelete;
// import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.RepresentationModel;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel extends RepresentationModel<CourseModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
    // "yyyy-MM-dd'T'HH:mm:ss'Z")
    @Column(nullable = false)
    //
    private LocalDateTime creationDate;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
    // "yyyy-MM-dd'T'HH:mm:ss'Z")
    @Column(nullable = false)
    //
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Column(nullable = false)
    private UUID userInstructor;

    @Column(length = 255)
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE) -- Deleção em cascata pela
    // JPA(menor controle)
    @Fetch(FetchMode.SUBSELECT)
    Set<ModuleModel> modules;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    // @JoinColumn(name = "course_id") Caso queira personalizar o nome da FK
    private Set<CourseUserModel> coursesUsers;

    public CourseUserModel convertToCourseUserModel(UUID userId) {
        return new CourseUserModel(null, userId, this);
    }
}

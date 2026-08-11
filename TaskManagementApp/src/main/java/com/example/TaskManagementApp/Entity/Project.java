package com.example.TaskManagementApp.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "project")

@Getter
@Setter

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private projectStatus status = projectStatus.ACTIVE;

    public Project(String name){
        this.name = name;
    }


    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}

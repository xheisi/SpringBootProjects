package com.example.TaskManagementApp.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "task")

@Getter
@Setter
public class Task {
}

package com.example.TaskManagementApp.Service;
import java.util.List;
import com.example.TaskManagementApp.Entity.Project;
import com.example.TaskManagementApp.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

    @Service
    public class ProjectService {

        private final ProjectRepository projectRepository;

        public ProjectService(ProjectRepository projectRepository) {
            this.projectRepository = projectRepository;
        }

        public Project createProject(Project project) {
            return projectRepository.save(project);
        }

        public List<Project> getAllProjects() {
            return projectRepository.findAll();
        }

        public Project getProjectById(Long id) {
            return projectRepository.findById(id)
                    .orElseThrow(() -> new ProjectNotFoundException(id));
        }  //add exception

        public void deleteProject(Long id) {
            Project project = getProjectById(id);
            projectRepository.delete(project);
        }


}

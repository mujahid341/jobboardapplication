package jobboardapplication.domain;

import jakarta.persistence.*;
import jobboardapplication.domain.model.BaseEntity;

import java.util.Objects;

@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    @Id
    private String id;

    private String title;
    private String location;
    private String description;
    private String skill;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Job() {
    }

    public Job(String id, String title, String description, String skill, String location, User createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.skill = skill;
        this.location = location;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id)
                && Objects.equals(title, job.title)
                && Objects.equals(location, job.location)
                && Objects.equals(description, job.description)
                && Objects.equals(skill, job.skill)
                && Objects.equals(createdBy, job.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, location, description, skill, createdBy);
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", skill='" + skill + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}

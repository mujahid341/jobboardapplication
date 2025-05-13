package jobboardapplication.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "jobs")
public class Job {
    @Id
    private String id;
    private String title;
    private String location;
    private String description;
    private String skill;

    private User user;

    public Job() {
    }

    public Job(String id, String title, String description, String skill, String location, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.skill = skill;
        this.location = location;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", skill='" + skill + '\'' +
                ", user=" + user +
                '}';
    }
}

package jobboardapplication.domain;

public class CreateJobRequest {
    private String title;
    private String location;
    private String description;
    private String skills;

    public CreateJobRequest(String title, String location, String description, String skills) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.skills = skills;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}

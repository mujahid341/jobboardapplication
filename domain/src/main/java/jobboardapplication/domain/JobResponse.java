package jobboardapplication.domain;

public class JobResponse {
    private String id;
    private String title;
    private String location;
    private String description;
    private String skill;

    public JobResponse(String id, String title, String location, String description, String skill) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.skill = skill;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSkill() {
        return skill;
    }
}

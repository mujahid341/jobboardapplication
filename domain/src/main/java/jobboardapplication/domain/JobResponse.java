package jobboardapplication.domain;

public class JobResponse {
    private String id;
    private String title;
    private String location;
    private String description;
    private String skill;

    public JobResponse() {
        // No-args constructor required for serialization
    }

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.description = job.getDescription();
        this.skill = job.getSkill(); // FIXED: should fetch skill, not description
    }

    // Getters
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

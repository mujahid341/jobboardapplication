package jobboardapplication.domain;

public class JobResponse {
    private String id;
    private String title;
    private String location;
    private String description;
    private String skill;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.description = job.getDescription();
        this.skill = job.getDescription();
    }
}

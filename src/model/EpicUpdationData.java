package model;

public class EpicUpdationData {
    private Integer id;
    private String name;
    private String description;
    
    public EpicUpdationData(Epic epic) {
        this.id = epic.getId();
        this.name = epic.getName();
        this.description = epic.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

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

    public EpicUpdationData(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public EpicUpdationData(Integer id) {
        this.id = id;
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

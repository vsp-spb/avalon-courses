package data;

public class Definition {
    private int id;
    private String definition;

    public Definition(int id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}

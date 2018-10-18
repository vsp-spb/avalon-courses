package main;

public class FormData {
    private String term;
    private String definition;
    private boolean prevDisabled;
    private boolean nextDisabled;

    public FormData() {
    }

    public FormData(String term, String definition, boolean prevDisabled, boolean nextDisabled) {
        this.term = term;
        this.definition = definition;
        this.prevDisabled = prevDisabled;
        this.nextDisabled = nextDisabled;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isPrevDisabled() {
        return prevDisabled;
    }

    public void setPrevDisabled(boolean prevDisabled) {
        this.prevDisabled = prevDisabled;
    }

    public boolean isNextDisabled() {
        return nextDisabled;
    }

    public void setNextDisabled(boolean nextDisabled) {
        this.nextDisabled = nextDisabled;
    }
}

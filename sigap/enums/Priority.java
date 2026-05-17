package sigap.enums;

public enum Priority {

    LOW    ("Rendah"),
    MEDIUM ("Sedang"),
    HIGH   ("Tinggi");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

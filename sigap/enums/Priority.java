package sigap.enums;

/**
 * ============================================================
 * Enum Priority
 * Merepresentasikan tingkat prioritas sebuah aspirasi.
 *
 * Ordinal (urutan angka otomatis Java):
 *   LOW    = 0
 *   MEDIUM = 1
 *   HIGH   = 2
 *
 * Ordinal ini digunakan oleh Aspiration.compareTo() untuk
 * menentukan urutan di dalam PriorityQueue.
 * ============================================================
 */
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

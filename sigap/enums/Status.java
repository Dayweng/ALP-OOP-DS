package sigap.enums;

/**
 * ============================================================
 * Enum Status
 * Merepresentasikan status dari sebuah aspirasi.
 * Enum digunakan agar nilai status konsisten dan tidak bisa
 * diisi sembarangan (type-safe).
 * ============================================================
 */
public enum Status {

    PENDING     ("Menunggu Verifikasi"),
    APPROVED    ("Disetujui Admin"),
    REJECTED    ("Ditolak Admin"),
    ON_PROGRESS ("Sedang Diproses Institusi"),
    DONE        ("Selesai");

    // Label ramah-baca (bahasa Indonesia)
    private final String label;

    /**
     * Constructor enum — dipanggil saat program dimuat.
     * Setiap konstanta enum memiliki label sendiri.
     */
    Status(String label) {
        this.label = label;
    }

    /** Kembalikan label bahasa Indonesia */
    public String getLabel() {
        return label;
    }
}

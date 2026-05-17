package sigap.enums;

public enum Status {

    PENDING     ("Menunggu Verifikasi"),
    APPROVED    ("Disetujui Admin"),
    REJECTED    ("Ditolak Admin"),
    ON_PROGRESS ("Sedang Diproses Institusi"),
    DONE        ("Selesai");


    private final String label;


    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

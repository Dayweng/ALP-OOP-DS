package sigap.model;

/**
 * ============================================================
 * Class: InstitutionAdmin (extends User)
 * ============================================================
 * Merepresentasikan petugas dari institusi pemerintah yang
 * bertanggung jawab memproses aspirasi yang diarahkan kepada
 * institusinya.
 *
 * Contoh: Dinas PU, Dinas Kesehatan, dll.
 *
 * Fitur (Phase 3):
 *  - Proses laporan aspirasi (ON_PROGRESS)
 *  - Update status aspirasi (DONE)
 * ============================================================
 */
public class InstitutionAdmin extends User {

    // Atribut tambahan: nama institusi yang dinaungi
    private String institutionName;

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Membuat objek InstitutionAdmin baru.
     *
     * @param username        username login
     * @param password        password login
     * @param nama            nama petugas
     * @param institutionName nama institusi yang diwakili
     */
    public InstitutionAdmin(String username, String password,
                             String nama, String institutionName) {
        super(username, password, nama);
        this.institutionName = institutionName;
    }

    // ======================================================
    // IMPLEMENTASI ABSTRACT METHOD DARI USER
    // ======================================================

    @Override
    public String getRole() {
        return "INSTITUTION_ADMIN";
    }

    /**
     * Tampilkan menu dashboard khusus InstitutionAdmin.
     */
    @Override
    public void showDashboard() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       DASHBOARD ADMIN INSTITUSI          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Halo, " + getNama() + "!");
        System.out.println("║  Institusi : " + institutionName);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  [1] Proses Laporan        (Phase 3)     ║");
        System.out.println("║  [2] Update Status Laporan (Phase 3)     ║");
        System.out.println("║  [0] Logout                              ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ======================================================
    // GETTER & SETTER
    // ======================================================
    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}

package sigap.model;

/**
 * ============================================================
 * Class: Admin (extends User)
 * ============================================================
 * Merepresentasikan Administrator Sistem SIGAP.
 * Admin memiliki akses penuh untuk mengelola aspirasi:
 *  - Verifikasi aspirasi (Phase 2)
 *  - Menentukan prioritas (Phase 2)
 *  - Distribusi ke institusi (Phase 3)
 *  - Melihat dashboard statistik (Phase 3)
 *  - Menambah admin baru (Phase 3)
 * ============================================================
 */
public class Admin extends User {

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Membuat objek Admin baru.
     *
     * @param username username admin
     * @param password password admin
     * @param nama     nama lengkap admin
     */
    public Admin(String username, String password, String nama) {
        super(username, password, nama);
    }

    // ======================================================
    // IMPLEMENTASI ABSTRACT METHOD DARI USER
    // ======================================================

    @Override
    public String getRole() {
        return "ADMIN";
    }

    /**
     * Tampilkan menu dashboard khusus Admin.
     * Fitur Phase 2 dan 3 ditampilkan sebagai placeholder.
     */
    @Override
    public void showDashboard() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         DASHBOARD ADMINISTRATOR          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Halo, " + getNama() + "!");
        System.out.println("║  Role    : Administrator Sistem          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  [1] Verifikasi Aspirasi   (Phase 2)     ║");
        System.out.println("║  [2] Penentuan Prioritas   (Phase 2)     ║");
        System.out.println("║  [3] Distribusi Institusi  (Phase 3)     ║");
        System.out.println("║  [4] Dashboard Statistik   (Phase 3)     ║");
        System.out.println("║  [5] Tambah Admin Baru     (Phase 3)     ║");
        System.out.println("║  [0] Logout                              ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}

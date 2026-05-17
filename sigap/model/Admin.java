package sigap.model;

public class Admin extends User {

    public Admin(String username, String password, String nama) {
        super(username, password, nama);
    }


    @Override
    public String getRole() {
        return "ADMIN";
    }

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

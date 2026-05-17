package sigap.model;

public class Citizen extends User {
    public Citizen(String username, String password, String nama) {
        super(username, password, nama); 
    }

    @Override
    public String getRole() {
        return "CITIZEN";
    }
    @Override
    public void showDashboard() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       DASHBOARD WARGA NEGARA             ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Halo, " + getNama() + "!");
        System.out.println("║  Role    : Warga / Citizen               ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  [1] Tambah Aspirasi Baru                ║");
        System.out.println("║  [2] Lihat Aspirasi Saya   (Phase 2)     ║");
        System.out.println("║  [3] Upvote Aspirasi       (Phase 2)     ║");
        System.out.println("║  [4] Cari Aspirasi         (Phase 2)     ║");
        System.out.println("║  [0] Logout                              ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
    public Aspiration buatAspirasi(String id, String title,
                                   String description, String category,
                                   String location) {

        return new Aspiration(id, title, description, category, location, this.getUsername());
    }
}

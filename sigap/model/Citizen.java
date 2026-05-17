package sigap.model;

/**
 * ============================================================
 * Class: Citizen (extends User)
 * ============================================================
 * Merepresentasikan warga/masyarakat biasa yang menggunakan
 * sistem SIGAP untuk menyampaikan aspirasi.
 *
 * Konsep OOP:
 *  - Inheritance    : Citizen mewarisi semua atribut & method User
 *  - Polymorphism   : showDashboard() di-override dari User
 *  - Encapsulation  : tidak ada atribut tambahan yang perlu disembunyikan
 * ============================================================
 */
public class Citizen extends User {

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Membuat objek Citizen baru.
     * Memanggil constructor induk (User) dengan super().
     *
     * @param username username unik warga
     * @param password password
     * @param nama     nama lengkap warga
     */
    public Citizen(String username, String password, String nama) {
        super(username, password, nama);  // panggil constructor User
    }

    // ======================================================
    // IMPLEMENTASI ABSTRACT METHOD DARI USER
    // ======================================================

    /**
     * Kembalikan peran Citizen sebagai String konstan.
     */
    @Override
    public String getRole() {
        return "CITIZEN";
    }

    /**
     * Tampilkan menu dashboard khusus Warga.
     * Override dari User → Polymorphism.
     */
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

    // ======================================================
    // FITUR CITIZEN
    // ======================================================

    /**
     * Membuat objek Aspiration baru berdasarkan input dari warga.
     * Method ini dipanggil dari MainFlow setelah mengumpulkan
     * semua input dari pengguna.
     *
     * @param id          ID aspirasi yang sudah di-generate
     * @param title       judul aspirasi
     * @param description deskripsi lengkap aspirasi
     * @param category    kategori aspirasi
     * @param location    lokasi terkait aspirasi
     * @return objek Aspiration baru yang siap disimpan
     */
    public Aspiration buatAspirasi(String id, String title,
                                   String description, String category,
                                   String location) {
        // Buat aspirasi baru, dengan author = username Citizen ini
        return new Aspiration(id, title, description, category, location, this.getUsername());
    }
}

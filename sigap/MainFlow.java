package sigap;

import sigap.enums.Priority;
import sigap.enums.Status;
import sigap.model.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * ============================================================
 * Class: MainFlow  (Entry Point Program SIGAP)
 * ============================================================
 * Mengatur seluruh alur program:
 *  - Inisialisasi data
 *  - Menu utama
 *  - Proses register & login
 *  - Routing ke dashboard sesuai role
 *  - Fitur Tambah Aspirasi (Phase 1)
 *
 * STRUKTUR DATA GLOBAL:
 *  - HashMap<String, User>       → penyimpanan semua user
 *  - HashMap<String, Aspiration> → penyimpanan semua aspirasi
 *  - LinkedList<Aspiration>      → antrian verifikasi admin (FIFO)
 *  - (PriorityQueue digunakan di dalam objek Institution)
 * ============================================================
 */
public class MainFlow {

    // ==========================================================
    // ===          STRUKTUR DATA UTAMA (Global)              ===
    // ==========================================================

    /**
     * Menyimpan semua user (Citizen, Admin, InstitutionAdmin).
     * Key   = username (String)
     * Value = objek User
     * Digunakan untuk: cek unik username, validasi login, pencarian user.
     */
    static HashMap<String, User> userMap = new HashMap<>();

    /**
     * Menyimpan semua aspirasi yang pernah dibuat.
     * Key   = ID aspirasi (String), contoh: "ASP001"
     * Value = objek Aspiration
     * Digunakan untuk: pencarian, detail aspirasi, update status.
     */
    static HashMap<String, Aspiration> aspirationMap = new HashMap<>();

    /**
     * Antrian verifikasi aspirasi oleh Admin (FIFO).
     * Aspirasi yang masuk lebih dulu → diverifikasi lebih dulu.
     * Menggunakan LinkedList agar bisa add() di belakang dan
     * removeFirst() dari depan → perilaku seperti Queue (FIFO).
     */
    static LinkedList<Aspiration> verificationQueue = new LinkedList<>();

    /**
     * Menyimpan semua institusi.
     * Key   = nama institusi
     * Value = objek Institution (yang punya PriorityQueue internal)
     */
    static HashMap<String, Institution> institutionMap = new HashMap<>();

    // Counter otomatis untuk membuat ID aspirasi: ASP001, ASP002, ...
    static int aspirationCounter = 1;

    // Scanner global — dibuat satu kali, dipakai di mana-mana
    static Scanner sc = new Scanner(System.in);

    // User yang sedang aktif login (null jika tidak ada)
    static User currentUser = null;

    // ==========================================================
    // ===                     MAIN                           ===
    // ==========================================================

    public static void main(String[] args) {
        printBanner();                 // tampilkan banner awal
        inisialisasiDummyData();       // isi data awal
        tekanEnterUntukMulai();        // jeda sebelum menu utama
        menuUtama();                   // jalankan menu utama
        sc.close();                    // tutup scanner saat selesai
    }

    // ==========================================================
    // ===                  BANNER & JEDA                     ===
    // ==========================================================

    /** Tampilkan banner SIGAP saat program pertama kali dijalankan */
    static void printBanner() {
        System.out.println();
        System.out.println("  ███████╗██╗ ██████╗  █████╗ ██████╗ ");
        System.out.println("  ██╔════╝██║██╔════╝ ██╔══██╗██╔══██╗");
        System.out.println("  ███████╗██║██║  ███╗███████║██████╔╝");
        System.out.println("  ╚════██║██║██║   ██║██╔══██║██╔═══╝ ");
        System.out.println("  ███████║██║╚██████╔╝██║  ██║██║     ");
        System.out.println("  ╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝     ");
        System.out.println();
        System.out.println("  Sistem Informasi Aspirasi Publik");
        System.out.println("  Pemerintah Kabupaten Sidoarjo");
        System.out.println("  Versi 1.0 — Phase 1 (Register, Login, Tambah Aspirasi)");
        System.out.println();
        System.out.println("══════════════════════════════════════════════════════");
    }

    /** Meminta user menekan Enter sebelum masuk ke menu */
    static void tekanEnterUntukMulai() {
        System.out.println("  Tekan [Enter] untuk memulai...");
        sc.nextLine();
    }

    /** Header kecil yang ditampilkan di atas setiap menu */
    static void printHeader() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║  SIGAP — Sistem Informasi Aspirasi Publik    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // ==========================================================
    // ===                 DUMMY DATA                         ===
    // ==========================================================

    /**
     * Mengisi data awal program saat pertama kali dijalankan.
     * Berisi: 1 admin, 2 institution admin, 2 citizen, 3 aspirasi.
     */
    static void inisialisasiDummyData() {

        // ── Admin Sistem ────────────────────────────────────────
        Admin adminSistem = new Admin("admin", "admin123", "Administrator SIGAP");
        userMap.put("admin", adminSistem);

        // ── Admin Institusi ─────────────────────────────────────
        InstitutionAdmin iaPU = new InstitutionAdmin(
                "inst_pu", "pu123",
                "Pak Agus Setiawan",
                "Dinas Pekerjaan Umum Sidoarjo"
        );
        InstitutionAdmin iaDinkes = new InstitutionAdmin(
                "inst_dinkes", "dinkes123",
                "Bu Rina Widayanti",
                "Dinas Kesehatan Sidoarjo"
        );
        userMap.put("inst_pu",     iaPU);
        userMap.put("inst_dinkes", iaDinkes);

        // ── Institusi (dengan PriorityQueue internal) ──────────
        Institution instPU     = new Institution("Dinas Pekerjaan Umum Sidoarjo");
        Institution instDinkes = new Institution("Dinas Kesehatan Sidoarjo");
        institutionMap.put("Dinas Pekerjaan Umum Sidoarjo", instPU);
        institutionMap.put("Dinas Kesehatan Sidoarjo",      instDinkes);

        // ── Citizen Contoh ──────────────────────────────────────
        Citizen warga1 = new Citizen("budi",  "budi123",  "Budi Santoso");
        Citizen warga2 = new Citizen("siti",  "siti123",  "Siti Aminah");
        userMap.put("budi", warga1);
        userMap.put("siti", warga2);

        // ── Aspirasi Contoh ─────────────────────────────────────

        // ASP001
        Aspiration asp1 = new Aspiration(
                generateId(),
                "Jalan Berlubang di Jl. Pahlawan",
                "Jalan di depan SD Negeri 1 Sidoarjo sudah rusak parah. " +
                "Banyak lubang besar yang membahayakan pengendara motor dan anak sekolah. " +
                "Mohon segera diperbaiki.",
                "Infrastruktur",
                "Jl. Pahlawan No. 12, Kec. Sidoarjo",
                "budi"
        );
        asp1.setPriority(Priority.HIGH);

        // ASP002
        Aspiration asp2 = new Aspiration(
                generateId(),
                "Kurangnya Dokter di Puskesmas Waru",
                "Puskesmas Kecamatan Waru hanya memiliki 2 dokter untuk melayani " +
                "lebih dari 50.000 warga. Antrean sangat panjang dan waktu tunggu " +
                "bisa mencapai 4-5 jam. Mohon penambahan tenaga medis.",
                "Kesehatan",
                "Puskesmas Waru, Kec. Waru, Sidoarjo",
                "siti"
        );
        asp2.setPriority(Priority.MEDIUM);

        // ASP003
        Aspiration asp3 = new Aspiration(
                generateId(),
                "Sampah Menumpuk di Bantaran Sungai Porong",
                "Bantaran sungai Porong penuh dengan sampah rumah tangga. " +
                "Bau tidak sedap mengganggu warga sekitar dan berpotensi " +
                "menjadi sarang nyamuk. Perlu penanganan segera.",
                "Lingkungan",
                "Bantaran Sungai Porong, Kec. Porong, Sidoarjo",
                "budi"
        );
        asp3.setPriority(Priority.MEDIUM);

        // Tambah ke HashMap dan LinkedList verifikasi
        aspirationMap.put(asp1.getId(), asp1);
        aspirationMap.put(asp2.getId(), asp2);
        aspirationMap.put(asp3.getId(), asp3);

        verificationQueue.add(asp1);   // masuk antrian FIFO → ASP001 duluan
        verificationQueue.add(asp2);
        verificationQueue.add(asp3);

        System.out.println();
        System.out.println("  [SISTEM] Data awal berhasil dimuat.");
        System.out.println("  [SISTEM] " + userMap.size() + " user, "
                + aspirationMap.size() + " aspirasi siap.");
    }

    /**
     * Generate ID aspirasi secara otomatis dan berurutan.
     * Format: ASP001, ASP002, ASP003, ...
     *
     * @return String ID baru
     */
    static String generateId() {
        // String.format("%03d", n) → padded dengan nol di kiri, minimal 3 digit
        String id = "ASP" + String.format("%03d", aspirationCounter);
        aspirationCounter++;
        return id;
    }

    // ==========================================================
    // ===                  MENU UTAMA                        ===
    // ==========================================================

    /** Loop menu utama: Register, Login, Keluar */
    static void menuUtama() {
        boolean running = true;

        while (running) {
            printHeader();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║               MENU UTAMA                 ║");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║  [1] Register Akun Baru                  ║");
            System.out.println("║  [2] Login                               ║");
            System.out.println("║  [0] Keluar Program                      ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    prosesRegister();
                    break;
                case 2:
                    prosesLogin();
                    break;
                case 0:
                    System.out.println();
                    System.out.println("  Terima kasih telah menggunakan SIGAP!");
                    System.out.println("  Program selesai. Sampai jumpa!");
                    System.out.println();
                    running = false;
                    break;
                default:
                    System.out.println("  ⚠ Pilihan tidak valid! Masukkan angka 0–2.");
            }
        }
    }

    // ==========================================================
    // ===            FEATURE 1: REGISTER                     ===
    // ==========================================================

    /**
     * Alur registrasi akun Citizen baru.
     *
     * Langkah:
     * 1. Input nama lengkap
     * 2. Input username → cek unik di userMap
     * 3. Input password → konfirmasi ulang
     * 4. Buat objek Citizen → simpan ke userMap
     */
    static void prosesRegister() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         REGISTRASI AKUN WARGA            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Daftarkan diri Anda untuk mulai         ║");
        System.out.println("║  menyampaikan aspirasi kepada pemerintah.║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // Input nama lengkap
        System.out.print("  Nama Lengkap       : ");
        String nama = sc.nextLine().trim();

        if (nama.isEmpty()) {
            System.out.println("  ⚠ Nama tidak boleh kosong. Registrasi dibatalkan.");
            return;
        }

        // Input username
        System.out.print("  Username           : ");
        String username = sc.nextLine().trim().toLowerCase();

        // Validasi: tidak boleh kosong
        if (username.isEmpty()) {
            System.out.println("  ⚠ Username tidak boleh kosong. Registrasi dibatalkan.");
            return;
        }

        // Validasi: tidak boleh ada spasi
        if (username.contains(" ")) {
            System.out.println("  ⚠ Username tidak boleh mengandung spasi.");
            return;
        }

        // Cek keunikan username di HashMap
        if (userMap.containsKey(username)) {
            System.out.println("  ⚠ Username '" + username + "' sudah digunakan.");
            System.out.println("    Silakan pilih username lain.");
            return;
        }

        // Input password
        System.out.print("  Password           : ");
        String password = sc.nextLine().trim();

        if (password.length() < 6) {
            System.out.println("  ⚠ Password minimal 6 karakter.");
            return;
        }

        // Konfirmasi password
        System.out.print("  Konfirmasi Password: ");
        String konfirmasi = sc.nextLine().trim();

        if (!password.equals(konfirmasi)) {
            System.out.println("  ⚠ Password dan konfirmasi tidak cocok. Registrasi dibatalkan.");
            return;
        }

        // Semua validasi lulus → buat objek Citizen dan simpan
        Citizen wargaBaru = new Citizen(username, password, nama);
        userMap.put(username, wargaBaru);    // simpan ke HashMap

        // Tampilkan konfirmasi berhasil
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║   ✓ REGISTRASI BERHASIL!             ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println("    Nama     : " + nama);
        System.out.println("    Username : " + username);
        System.out.println("    Role     : Warga / Citizen");
        System.out.println();
        System.out.println("  Silakan login dengan akun Anda.");
    }

    // ==========================================================
    // ===            FEATURE 2: LOGIN                        ===
    // ==========================================================

    /**
     * Alur login pengguna.
     *
     * Langkah:
     * 1. Input username → cari di userMap
     * 2. Input password → validasi dengan user.login()
     * 3. Routing ke dashboard sesuai getRole()
     */
    static void prosesLogin() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║                  LOGIN                   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        System.out.print("  Username : ");
        String username = sc.nextLine().trim().toLowerCase();

        // Cek apakah username terdaftar
        if (!userMap.containsKey(username)) {
            System.out.println("  ⚠ Username '" + username + "' tidak ditemukan.");
            System.out.println("    Silakan register terlebih dahulu.");
            return;
        }

        System.out.print("  Password : ");
        String password = sc.nextLine().trim();

        // Ambil objek user dari HashMap
        User user = userMap.get(username);

        // Validasi password menggunakan method login() dari abstract class User
        if (!user.login(password)) {
            System.out.println("  ⚠ Password salah! Silakan coba lagi.");
            return;
        }

        // Login berhasil
        currentUser = user;
        System.out.println();
        System.out.println("  ✓ Login berhasil!");
        System.out.println("  Selamat datang, " + user.getNama() + "! [" + user.getRole() + "]");

        // Routing ke dashboard sesuai role (Polymorphism melalui instanceof & casting)
        String role = user.getRole();

        if (role.equals("CITIZEN")) {
            dashboardCitizen((Citizen) user);

        } else if (role.equals("ADMIN")) {
            dashboardAdmin((Admin) user);

        } else if (role.equals("INSTITUTION_ADMIN")) {
            dashboardInstitutionAdmin((InstitutionAdmin) user);

        } else {
            System.out.println("  ⚠ Role tidak dikenali. Hubungi administrator.");
        }

        // Setelah logout dari dashboard, hapus currentUser
        currentUser = null;
    }

    // ==========================================================
    // ===         DASHBOARD CITIZEN (Phase 1)               ===
    // ==========================================================

    /**
     * Dashboard untuk Citizen / Warga.
     * Phase 1: hanya fitur [1] Tambah Aspirasi yang aktif.
     * Fitur lain menampilkan pesan "Phase 2".
     *
     * @param citizen objek Citizen yang sedang login
     */
    static void dashboardCitizen(Citizen citizen) {
        boolean loggedIn = true;

        while (loggedIn) {
            citizen.showDashboard();   // tampilkan menu (override dari User)
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    // ── FEATURE 4: Tambah Aspirasi ──
                    prosesTambahAspirasi(citizen);
                    break;

                case 2:
                case 3:
                case 4:
                    // Fitur yang belum tersedia di Phase 1
                    System.out.println();
                    System.out.println("  ⏳ Fitur ini akan tersedia di Phase 2.");
                    System.out.println("     Silakan tunggu update berikutnya.");
                    break;

                case 0:
                    // ── FEATURE 3: Logout ──
                    citizen.logout();    // dipanggil dari abstract class User
                    loggedIn = false;
                    break;

                default:
                    System.out.println("  ⚠ Pilihan tidak valid! Masukkan angka 0–4.");
            }
        }
    }

    // ==========================================================
    // ===           FEATURE 4: TAMBAH ASPIRASI              ===
    // ==========================================================

    /**
     * Alur penambahan aspirasi baru oleh Citizen.
     *
     * Langkah:
     * 1. Input judul, deskripsi, kategori, lokasi
     * 2. Generate ID otomatis
     * 3. Panggil citizen.buatAspirasi() → buat objek Aspiration
     * 4. Simpan ke aspirationMap (HashMap)
     * 5. Masukkan ke verificationQueue (LinkedList FIFO)
     *
     * @param citizen Citizen yang sedang login
     */
    static void prosesTambahAspirasi(Citizen citizen) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         TAMBAH ASPIRASI BARU             ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Sampaikan aspirasi Anda kepada          ║");
        System.out.println("║  pemerintah daerah Sidoarjo.             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // ── Input Judul ─────────────────────────────────────────
        System.out.print("  Judul Aspirasi  : ");
        String title = sc.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("  ⚠ Judul tidak boleh kosong. Dibatalkan.");
            return;
        }

        // ── Input Deskripsi ─────────────────────────────────────
        System.out.print("  Deskripsi       : ");
        String description = sc.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("  ⚠ Deskripsi tidak boleh kosong. Dibatalkan.");
            return;
        }

        // ── Pilih Kategori ──────────────────────────────────────
        System.out.println();
        System.out.println("  Pilih Kategori:");
        System.out.println("    [1] Infrastruktur   (jalan, jembatan, drainase)");
        System.out.println("    [2] Pendidikan      (sekolah, beasiswa, guru)");
        System.out.println("    [3] Kesehatan       (puskesmas, dokter, obat)");
        System.out.println("    [4] Lingkungan      (sampah, polusi, taman)");
        System.out.println("    [5] Lainnya");
        System.out.print("  Pilihan Kategori: ");

        int katPilihan = bacaInt();
        String category;

        // Switch tradisional (kompatibel Java 8+)
        switch (katPilihan) {
            case 1:  category = "Infrastruktur"; break;
            case 2:  category = "Pendidikan";    break;
            case 3:  category = "Kesehatan";     break;
            case 4:  category = "Lingkungan";    break;
            default: category = "Lainnya";       break;
        }

        System.out.println("  Kategori dipilih: " + category);

        // ── Input Lokasi ─────────────────────────────────────────
        System.out.print("  Lokasi          : ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("  ⚠ Lokasi tidak boleh kosong. Dibatalkan.");
            return;
        }

        // ── Konfirmasi ────────────────────────────────────────────
        System.out.println();
        System.out.println("  ── Konfirmasi Aspirasi ──────────────────");
        System.out.println("  Judul     : " + title);
        System.out.println("  Deskripsi : " + description);
        System.out.println("  Kategori  : " + category);
        System.out.println("  Lokasi    : " + location);
        System.out.println("  ─────────────────────────────────────────");
        System.out.print("  Kirim aspirasi ini? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("  Aspirasi dibatalkan.");
            return;
        }

        // ── Generate ID ───────────────────────────────────────────
        String newId = generateId();

        // ── Buat Aspiration via method Citizen ────────────────────
        // Ini memanggil method buatAspirasi() di class Citizen
        Aspiration aspirasiBaru = citizen.buatAspirasi(newId, title, description, category, location);

        // ── Simpan ke HashMap aspirasi ────────────────────────────
        // O(1) insert — HashMap sangat efisien untuk penyimpanan
        aspirationMap.put(newId, aspirasiBaru);

        // ── Masukkan ke LinkedList verifikasi (FIFO) ──────────────
        // add() menambah di AKHIR list → Admin ambil dari DEPAN (FIFO)
        verificationQueue.add(aspirasiBaru);

        // ── Tampilkan Konfirmasi ───────────────────────────────────
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    ✓ ASPIRASI BERHASIL DIKIRIM!          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println("    ID Aspirasi  : " + newId);
        System.out.println("    Judul        : " + title);
        System.out.println("    Kategori     : " + category);
        System.out.println("    Lokasi       : " + location);
        System.out.println("    Status       : " + aspirasiBaru.getStatus().getLabel());
        System.out.println("    Antrian ke-  : " + verificationQueue.size());
        System.out.println();
        System.out.println("  Aspirasi Anda akan segera diverifikasi oleh Admin.");
        System.out.println("  Simpan ID Aspirasi Anda: " + newId);
    }

    // ==========================================================
    // ===         DASHBOARD ADMIN (Placeholder Phase 2)      ===
    // ==========================================================

    /**
     * Dashboard Admin. Fitur Phase 2 & 3 masih terkunci.
     *
     * @param admin objek Admin yang login
     */
    static void dashboardAdmin(Admin admin) {
        boolean loggedIn = true;

        while (loggedIn) {
            admin.showDashboard();
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                case 2:
                    System.out.println();
                    System.out.println("  ⏳ Fitur ini akan tersedia di Phase 2.");
                    break;
                case 3:
                case 4:
                case 5:
                    System.out.println();
                    System.out.println("  ⏳ Fitur ini akan tersedia di Phase 3.");
                    break;
                case 0:
                    admin.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("  ⚠ Pilihan tidak valid!");
            }
        }
    }

    // ==========================================================
    // ===    DASHBOARD INSTITUTION ADMIN (Placeholder Ph 3)  ===
    // ==========================================================

    /**
     * Dashboard InstitutionAdmin. Fitur Phase 3 masih terkunci.
     *
     * @param ia objek InstitutionAdmin yang login
     */
    static void dashboardInstitutionAdmin(InstitutionAdmin ia) {
        boolean loggedIn = true;

        while (loggedIn) {
            ia.showDashboard();
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                case 2:
                    System.out.println();
                    System.out.println("  ⏳ Fitur ini akan tersedia di Phase 3.");
                    break;
                case 0:
                    ia.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("  ⚠ Pilihan tidak valid!");
            }
        }
    }

    // ==========================================================
    // ===                   UTILITY                         ===
    // ==========================================================

    /**
     * Membaca input integer dari Scanner dengan aman.
     * Jika user mengetik bukan angka → kembalikan -1 (tidak crash).
     *
     * @return integer yang dibaca, atau -1 jika input tidak valid
     */
    static int bacaInt() {
        try {
            int nilai = sc.nextInt();
            sc.nextLine();   // konsumsi sisa newline setelah nextInt()
            return nilai;
        } catch (InputMismatchException e) {
            sc.nextLine();   // buang input yang tidak valid
            System.out.println("  ⚠ Input harus berupa angka!");
            return -1;
        }
    }
}

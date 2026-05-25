package sigap;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import sigap.enums.Status;


public class MainFlow {
    static HashMap<String, User> userMap = new HashMap<>();
    static HashMap<String, Aspiration> aspirationMap = new HashMap<>();
    static LinkedList<Aspiration> verificationQueue = new LinkedList<>();
    static HashMap<String, Institution> institutionMap = new HashMap<>();

    
    static int aspirationCounter = 1;
    static Scanner sc = new Scanner(System.in);
    static User currentUser = null;

    public static void main(String[] args) {
        printBanner();                 
        inisialisasiDummyData();       
        tekanEnterUntukMulai();        
        menuUtama();                  
        sc.close();                   
    }
 
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
        System.out.println("  Versi 1.0 - (Register, Login, Tambah Aspirasi)");
        System.out.println();
        System.out.println("══════════════════════════════════════════════════════");
    }


    static void tekanEnterUntukMulai() {
        System.out.println("  Tekan [Enter] untuk memulai...");
        sc.nextLine();
    }


    static void printHeader() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║  SIGAP - Sistem Informasi Aspirasi Publik    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

 
    static void inisialisasiDummyData() {


        Admin adminSistem = new Admin("admin", "admin123", "Administrator SIGAP");
        userMap.put("admin", adminSistem);

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


        Institution instPU     = new Institution("Dinas Pekerjaan Umum Sidoarjo");
        Institution instDinkes = new Institution("Dinas Kesehatan Sidoarjo");
        institutionMap.put("Dinas Pekerjaan Umum Sidoarjo", instPU);
        institutionMap.put("Dinas Kesehatan Sidoarjo",      instDinkes);

   
        Citizen warga1 = new Citizen("budi",  "budi123",  "Budi Santoso");
        Citizen warga2 = new Citizen("siti",  "siti123",  "Siti Aminah");
        userMap.put("budi", warga1);
        userMap.put("siti", warga2);

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
        aspirationMap.put(asp1.getId(), asp1);
        aspirationMap.put(asp2.getId(), asp2);
        aspirationMap.put(asp3.getId(), asp3);

        verificationQueue.add(asp1);    
        verificationQueue.add(asp2);
        verificationQueue.add(asp3);

        System.out.println();
        System.out.println("  [SISTEM] Data awal berhasil dimuat.");
        System.out.println("  [SISTEM] " + userMap.size() + " user, "
                + aspirationMap.size() + " aspirasi siap.");
    }


    static String generateId() {
        String id = "ASP" + String.format("%03d", aspirationCounter);
        aspirationCounter++;
        return id;
    }


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
                    System.out.println("   Pilihan tidak valid! Masukkan angka 0 -2.");
            }
        }
    }


    static void prosesRegister() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         REGISTRASI AKUN WARGA            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Daftarkan diri Anda untuk mulai         ║");
        System.out.println("║  menyampaikan aspirasi kepada pemerintah.║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        System.out.print("  Nama Lengkap       : ");
        String nama = sc.nextLine().trim();

        if (nama.isEmpty()) {
            System.out.println("   Nama tidak boleh kosong. Registrasi dibatalkan.");
            return;
        }

        System.out.print("  Username           : ");
        String username = sc.nextLine().trim().toLowerCase();

        if (username.isEmpty()) {
            System.out.println("  Username tidak boleh kosong. Registrasi dibatalkan.");
            return;
        }

        if (username.contains(" ")) {
            System.out.println("  Username tidak boleh mengandung spasi.");
            return;
        }

        if (userMap.containsKey(username)) {
            System.out.println("   Username '" + username + "' sudah digunakan.");
            System.out.println("  Silakan pilih username lain.");
            return;
        }

        System.out.print("  Password           : ");
        String password = sc.nextLine().trim();

        if (password.length() < 6) {
            System.out.println("  Password minimal 6 karakter.");
            return;
        }

        System.out.print("  Konfirmasi Password: ");
        String konfirmasi = sc.nextLine().trim();

        if (!password.equals(konfirmasi)) {
            System.out.println("  Password dan konfirmasi tidak cocok. Registrasi dibatalkan.");
            return;
        }

        Citizen wargaBaru = new Citizen(username, password, nama);
        userMap.put(username, wargaBaru);  

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║     REGISTRASI BERHASIL!             ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println("    Nama     : " + nama);
        System.out.println("    Username : " + username);
        System.out.println("    Role     : Warga / Citizen");
        System.out.println();
        System.out.println("  Silakan login dengan akun Anda.");
    }


    static void prosesLogin() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║                  LOGIN                   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        System.out.print("  Username : ");
        String username = sc.nextLine().trim().toLowerCase();

        if (!userMap.containsKey(username)) {
            System.out.println("   Username '" + username + "' tidak ditemukan.");
            System.out.println("    Silakan register terlebih dahulu.");
            return;
        }

        System.out.print("  Password : ");
        String password = sc.nextLine().trim();


        User user = userMap.get(username);

        if (!user.login(password)) {
            System.out.println("   Password salah! Silakan coba lagi.");
            return;
        }

        currentUser = user;
        System.out.println();
        System.out.println("    Login berhasil!");
        System.out.println("  Selamat datang, " + user.getNama() + "! [" + user.getRole() + "]");

        String role = user.getRole();

        if (role.equals("CITIZEN")) {
            dashboardCitizen((Citizen) user);

        } else if (role.equals("ADMIN")) {
            dashboardAdmin((Admin) user);

        } else if (role.equals("INSTITUTION_ADMIN")) {
            dashboardInstitutionAdmin((InstitutionAdmin) user);

        } else {
            System.out.println("   Role tidak dikenali. Hubungi administrator.");
        }

        currentUser = null;
    }

   
    static void dashboardCitizen(Citizen citizen) {
        boolean loggedIn = true;

        while (loggedIn) {
            citizen.showDashboard();
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    prosesTambahAspirasi(citizen);
                    break;
                case 2:
                    tampilkanSemuaAspirasi();
                    break;
                case 3:
                    upvoteAspiration(citizen);
                    break;
                case 4:
                    cariAspirasi();
                    break;
                case 0:
                    citizen.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("   Pilihan tidak valid! Masukkan angka 0 -4.");
            }
        }
    }

    static void prosesTambahAspirasi(Citizen citizen) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          TAMBAH ASPIRASI BARU            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║        Sampaikan aspirasi Anda           ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        System.out.print("  Judul Aspirasi  : ");
        String title = sc.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Judul tidak boleh kosong.");
            return;
        }

        System.out.print("  Deskripsi       : ");
        String description = sc.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Deskripsi tidak boleh kosong.");
            return;
        }

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

        switch (katPilihan) {
            case 1:  category = "Infrastruktur"; break;
            case 2:  category = "Pendidikan";    break;
            case 3:  category = "Kesehatan";     break;
            case 4:  category = "Lingkungan";    break;
            default: category = "Lainnya";       break;
        }

        System.out.println("Kategori dipilih: " + category);

        System.out.print("Lokasi          : ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("Lokasi tidak boleh kosong.");
            return;
        }

        System.out.println();
        System.out.println("  ── Konfirmasi Aspirasi ──────────────────");
        System.out.println("  Judul     : " + title);
        System.out.println("  Deskripsi : " + description);
        System.out.println("  Kategori  : " + category);
        System.out.println("  Lokasi    : " + location);
        System.out.println("  ─────────────────────────────────────────");
        System.out.print("Kirim aspirasi ini? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("Aspirasi dibatalkan.");
            return;
        }

        String newId = generateId();
        Aspiration aspirasiBaru = citizen.buatAspirasi(newId, title, description, category, location);
        aspirationMap.put(newId, aspirasiBaru);
        verificationQueue.add(aspirasiBaru);

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║      ASPIRASI BERHASIL DIKIRIM!          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println("    ID Aspirasi  : " + newId);
        System.out.println("    Judul        : " + title);
        System.out.println("    Kategori     : " + category);
        System.out.println("    Lokasi       : " + location);
        System.out.println("    Status       : " + aspirasiBaru.getStatus().getLabel());
        System.out.println("    Antrian ke-  : " + verificationQueue.size());
        System.out.println();
        System.out.println("Aspirasi Anda akan segera diverifikasi oleh Admin.");
        System.out.println("Simpan ID Aspirasi Anda: " + newId);
    }

    static void tampilkanSemuaAspirasi() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         DAFTAR ASPIRASI PUBLIK           ║");
        System.out.println("╠══════════════════════════════════════════╣");

        if (aspirationMap.isEmpty()) {
            System.out.println("  Belum ada aspirasi yang tersedia.");
        } else {
            for (Aspiration aspirasi : aspirationMap.values()) {
                aspirasi.displaySummary();
            }
        }

        System.out.println("╚══════════════════════════════════════════╝");
    }

    static void upvoteAspiration(Citizen citizen) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║              UPVOTE ASPIRASI             ║");
        System.out.println("╠══════════════════════════════════════════╣");

        if (aspirationMap.isEmpty()) {
            System.out.println("  Tidak ada aspirasi untuk diupvote saat ini.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        for (Aspiration aspirasi : aspirationMap.values()) {
            aspirasi.displaySummary();
        }

        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  Masukkan ID aspirasi yang ingin diupvote: ");
        String id = sc.nextLine().trim().toUpperCase();

        if (!aspirationMap.containsKey(id)) {
            System.out.println("  Aspirasi dengan ID " + id + " tidak ditemukan.");
            return;
        }

        Aspiration aspirasi = aspirationMap.get(id);

        if (aspirasi.getAuthor().equals(citizen.getUsername())) {
            System.out.println("  Anda tidak dapat mengupvote aspirasi sendiri.");
            return;
        }

        if (aspirasi.getStatus() == sigap.enums.Status.REJECTED) {
            System.out.println("  Aspirasi ini sudah ditolak dan tidak dapat diupvote.");
            return;
        }

        if (!aspirasi.addUpvote(citizen.getUsername())) {
            System.out.println("  Anda sudah pernah mengupvote aspirasi ini sebelumnya.");
            return;
        }

        System.out.println("  Terima kasih! Aspirasi " + id + " berhasil diupvote.");
        System.out.println("  Total upvote sekarang: " + aspirasi.getUpvotes());
    }

    static void cariAspirasi() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║               CARI ASPIRASI              ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.print("  Masukkan ID laporan: ");
        String id = sc.nextLine().trim().toUpperCase();

        if (id.isEmpty()) {
            System.out.println("  ID tidak boleh kosong.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        Aspiration aspirasi = aspirationMap.get(id);
        if (aspirasi == null) {
            System.out.println("  Aspirasi dengan ID '" + id + "' tidak ditemukan.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        aspirasi.displayDetail();
    }

    static void dashboardAdmin(Admin admin) {
        boolean loggedIn = true;

        while (loggedIn) {
            admin.showDashboard();
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    verifikasiAspirasi(admin);
                    break;
                case 2:
                    tetapkanPrioritasAspirasi(admin);
                    break;
                case 3:
                case 4:
                case 5:
                    System.out.println();
                    System.out.println("    Fitur ini akan tersedia di Phase 3.");
                    break;
                case 0:
                    admin.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("   Pilihan tidak valid!");
            }
        }
    }

    static void verifikasiAspirasi(Admin admin) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║            VERIFIKASI ASPIRASI           ║");
        System.out.println("╠══════════════════════════════════════════╣");

        if (verificationQueue.isEmpty()) {
            System.out.println("  Tidak ada aspirasi menunggu verifikasi.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        Aspiration aspirasi = verificationQueue.poll();
        aspirasi.displayDetail();

        System.out.println();
        System.out.println("  [1] Setujui aspirasi");
        System.out.println("  [2] Tolak aspirasi");
        System.out.print("  Pilihan Anda: ");
        int pilihan = bacaInt();

        switch (pilihan) {
            case 1:
                aspirasi.setStatus(sigap.enums.Status.APPROVED);
                System.out.println("  Aspirasi " + aspirasi.getId() + " disetujui.");
                break;
            case 2:
                aspirasi.setStatus(sigap.enums.Status.REJECTED);
                System.out.println("  Aspirasi " + aspirasi.getId() + " ditolak.");
                break;
            default:
                System.out.println("  Pilihan tidak valid. Aspirasi dikembalikan ke antrean.");
                verificationQueue.addFirst(aspirasi);
                return;
        }

        System.out.println("  Status terbaru: " + aspirasi.getStatus().getLabel());
    }

    static void tetapkanPrioritasAspirasi(Admin admin) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         PENENTUAN PRIORITAS              ║");
        System.out.println("╠══════════════════════════════════════════╣");

        LinkedList<Aspiration> siap = new LinkedList<>();
        for (Aspiration asp : aspirationMap.values()) {
            if (asp.getStatus() == Status.APPROVED && !asp.isScoreLocked()) {
                siap.add(asp);
            }
        }

        if (siap.isEmpty()) {
            System.out.println("  Tidak ada aspirasi siap diprioritaskan.");
            System.out.println("  Pastikan ada aspirasi berstatus DISETUJUI.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Votes");
        System.out.println("  " + "─".repeat(66));
        for (Aspiration asp : siap) {
            System.out.printf("  %-8s | %-30s | %-15s | %d%n",
                    asp.getId(),
                    asp.getTitle().length() > 28 ? asp.getTitle().substring(0, 28) + ".." : asp.getTitle(),
                    asp.getCategory(),
                    asp.getUpvotes());
        }

        System.out.print("\n  ID Aspirasi yang akan diprioritaskan (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration target = aspirationMap.get(id);
        if (target == null || target.getStatus() != Status.APPROVED || target.isScoreLocked()) {
            System.out.println("  ID tidak valid atau aspirasi tidak memenuhi syarat.");
            return;
        }

        target.displayDetail();

        System.out.println();
        System.out.println("  ── Komponen Skor Otomatis ──────────────────────");
        System.out.printf("  S_scale (15%%) : %.2f  [Skala Dampak — %d upvotes]%n",
                target.calcSScale(), target.getUpvotes());
        System.out.printf("  S_cat   (15%%) : %.2f  [Bobot Kategori: %s]%n",
                target.calcSCat(), target.getCategory());
        System.out.printf("  S_sys   (15%%) : %.2f  [Aging / Waktu Tunggu]%n",
                target.calcSSys());

        System.out.println();
        System.out.println("  ── Input Admin ─────────────────────────────────");
        System.out.println("  S_auth (30%) — Otoritas Admin [1-10]");
        System.out.println("  Nilai RENDAH = laporan dilebih-lebihkan.");
        System.out.println("  Nilai TINGGI = darurat, butuh penanganan segera.");
        System.out.print("  S_auth = ");
        double sAuth = bacaDouble(1, 10);

        System.out.println();
        System.out.println("  S_safe (25%) — Tingkat Ancaman Keselamatan [1-10]");
        System.out.println("  1=Tidak berbahaya   5=Cukup berbahaya   10=Mengancam nyawa");
        System.out.print("  S_safe = ");
        double sSafe = bacaDouble(1, 10);

        double preview = target.previewScore(sAuth, sSafe);
        String tierLabel;
        if      (preview >= 7.0) tierLabel = "TINGGI";
        else if (preview >= 4.0) tierLabel = "SEDANG";
        else                     tierLabel = "RENDAH";

        System.out.println();
        System.out.println("  ── Kalkulasi Skor Final ────────────────────────");
        System.out.printf("  S_auth  × 0.30 = %.3f%n", sAuth               * 0.30);
        System.out.printf("  S_safe  × 0.25 = %.3f%n", sSafe               * 0.25);
        System.out.printf("  S_scale × 0.15 = %.3f%n", target.calcSScale() * 0.15);
        System.out.printf("  S_cat   × 0.15 = %.3f%n", target.calcSCat()   * 0.15);
        System.out.printf("  S_sys   × 0.15 = %.3f%n", target.calcSSys()   * 0.15);
        System.out.println("  ──────────────────────────────────────────────");
        System.out.printf("  Total Score    = %.2f / 10.0  [%s]%n", preview, tierLabel);

        System.out.println();
        System.out.println("  ── Distribusi ke Institusi ─────────────────────");
        if (!institutionMap.isEmpty()) {
            String[] instNames = institutionMap.keySet().toArray(new String[0]);
            for (int i = 0; i < instNames.length; i++) {
                System.out.printf("  [%d] %s%n", i + 1, instNames[i]);
            }
            System.out.printf("  [%d] Lewati (tentukan nanti)%n", instNames.length + 1);
            System.out.print("  Pilih institusi: ");
            int instPilihan = bacaInt();
            if (instPilihan >= 1 && instPilihan <= instNames.length) {
                target.setInstitutionTarget(instNames[instPilihan - 1]);
            }
        }

        System.out.println();
        System.out.print("  Kunci skor dan simpan prioritas? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();
        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("  Penentuan prioritas dibatalkan.");
            return;
        }

        target.lockScore(sAuth, sSafe);

        if (!target.getInstitutionTarget().equals("-")) {
            Institution inst = institutionMap.get(target.getInstitutionTarget());
            if (inst != null) inst.addAspiration(target);
        }

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    PRIORITAS BERHASIL DIKUNCI!           ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.printf("    ID        : %s%n",          target.getId());
        System.out.printf("    Skor      : %.2f / 10.0%n", target.getTotalScore());
        System.out.printf("    Prioritas : %s%n",          target.getPriority().getLabel());
        System.out.printf("    Institusi : %s%n",          target.getInstitutionTarget());
        System.out.println("    [Skor tidak dapat diubah oleh siapapun]");
    }

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
                    System.out.println("    Fitur ini akan tersedia di Phase 3.");
                    break;
                case 0:
                    ia.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("   Pilihan tidak valid!");
            }
        }
    }

    static int bacaInt() {
        try {
            int nilai = sc.nextInt();
            sc.nextLine();
            return nilai;
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Input harus berupa angka!");
            return -1;
        }
    }

    static double bacaDouble(double min, double max) {
        while (true) {
            try {
                double val = sc.nextDouble();
                sc.nextLine();
                if (val >= min && val <= max) return val;
                System.out.printf("  Masukkan angka antara %.0f - %.0f: ", min, max);
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.print("  Input harus berupa angka: ");
            }
        }
    }
}

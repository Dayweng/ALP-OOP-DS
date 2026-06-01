package sigap;

import java.io.*;
import java.util.*;
import sigap.enums.Status;


public class MainFlow {

    static HashMap<String, User> userMap = new HashMap<>();
    static HashMap<String, Aspiration> aspirationMap = new HashMap<>();
    static LinkedList<Aspiration> verificationQueue = new LinkedList<>();
    static HashMap<String, Institution> institutionMap = new HashMap<>();
    static HashMap<String, LinkedList<String>> invertedIndex = new HashMap<>();

    static final String DATA_DIR = "data";
    static int aspirationCounter = 1;
    static Scanner sc = new Scanner(System.in);
    static User currentUser = null;

    public static void main(String[] args) {
        printBanner();
        loadData();
        buildInvertedIndex();
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
        System.out.println("  ALP - Kelompok 4");
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

 
    // static void inisialisasiDummyData() {


    //     Admin adminSistem = new Admin("admin", "admin123", "Administrator SIGAP");
    //     userMap.put("admin", adminSistem);

    //     InstitutionAdmin iaPU = new InstitutionAdmin(
    //             "inst_pu", "pu123",
    //             "Pak Agus Setiawan",
    //             "Dinas Pekerjaan Umum Sidoarjo"
    //     );
    //     InstitutionAdmin iaDinkes = new InstitutionAdmin(
    //             "inst_dinkes", "dinkes123",
    //             "Bu Rina Widayanti",
    //             "Dinas Kesehatan Sidoarjo"
    //     );
    //     userMap.put("inst_pu",     iaPU);
    //     userMap.put("inst_dinkes", iaDinkes);


    //     Institution instPU     = new Institution("Dinas Pekerjaan Umum Sidoarjo");
    //     Institution instDinkes = new Institution("Dinas Kesehatan Sidoarjo");
    //     institutionMap.put("Dinas Pekerjaan Umum Sidoarjo", instPU);
    //     institutionMap.put("Dinas Kesehatan Sidoarjo",      instDinkes);

   
    //     Citizen warga1 = new Citizen("budi",  "budi123",  "Budi Santoso");
    //     Citizen warga2 = new Citizen("siti",  "siti123",  "Siti Aminah");
    //     userMap.put("budi", warga1);
    //     userMap.put("siti", warga2);

    //     Aspiration asp1 = new Aspiration(
    //             generateId(),
    //             "Jalan Berlubang di Jl. Pahlawan",
    //             "Jalan di depan SD Negeri 1 Sidoarjo sudah rusak parah. " +
    //             "Banyak lubang besar yang membahayakan pengendara motor dan anak sekolah. " +
    //             "Mohon segera diperbaiki.",
    //             "Infrastruktur",
    //             "Jl. Pahlawan No. 12, Kec. Sidoarjo",
    //             "budi"
    //     );
    //     Aspiration asp2 = new Aspiration(
    //             generateId(),
    //             "Kurangnya Dokter di Puskesmas Waru",
    //             "Puskesmas Kecamatan Waru hanya memiliki 2 dokter untuk melayani " +
    //             "lebih dari 50.000 warga. Antrean sangat panjang dan waktu tunggu " +
    //             "bisa mencapai 4-5 jam. Mohon penambahan tenaga medis.",
    //             "Kesehatan",
    //             "Puskesmas Waru, Kec. Waru, Sidoarjo",
    //             "siti"
    //     );
    //     Aspiration asp3 = new Aspiration(
    //             generateId(),
    //             "Sampah Menumpuk di Bantaran Sungai Porong",
    //             "Bantaran sungai Porong penuh dengan sampah rumah tangga. " +
    //             "Bau tidak sedap mengganggu warga sekitar dan berpotensi " +
    //             "menjadi sarang nyamuk. Perlu penanganan segera.",
    //             "Lingkungan",
    //             "Bantaran Sungai Porong, Kec. Porong, Sidoarjo",
    //             "budi"
    //     );
    //     aspirationMap.put(asp1.getId(), asp1);
    //     aspirationMap.put(asp2.getId(), asp2);
    //     aspirationMap.put(asp3.getId(), asp3);

    //     verificationQueue.add(asp1);    
    //     verificationQueue.add(asp2);
    //     verificationQueue.add(asp3);

    //     System.out.println();
    //     System.out.println("  [SISTEM] Data awal berhasil dimuat.");
    //     System.out.println("  [SISTEM] " + userMap.size() + " user, "
    //             + aspirationMap.size() + " aspirasi siap.");
    // }


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
        saveData();

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
        System.out.println("  Login berhasil!");
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
        System.out.println("  ────────── Konfirmasi Aspirasi ──────────");
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
        indexAspiration(aspirasiBaru);
        saveData();

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

        saveData();
        System.out.println("  Terima kasih! Aspirasi " + id + " berhasil diupvote.");
        System.out.println("  Total upvote sekarang: " + aspirasi.getUpvotes());
    }

    static void cariAspirasi() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║               CARI ASPIRASI              ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.print("  Masukkan kata kunci: ");
        String input = sc.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            System.out.println("  Kata kunci tidak boleh kosong.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        String[] keywords = input.split("[^a-zA-Z0-9]+");

        LinkedList<String> hasilIds = new LinkedList<>();
        for (String keyword : keywords) {
            if (keyword.isEmpty()) continue;
            LinkedList<String> ids = invertedIndex.get(keyword);
            if (ids != null) {
                for (String id : ids) {
                    if (!hasilIds.contains(id)) hasilIds.add(id);
                }
            }
        }

        if (hasilIds.isEmpty()) {
            System.out.println("  Tidak ada aspirasi yang cocok dengan kata kunci \"" + input + "\".");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        System.out.println("  Ditemukan " + hasilIds.size() + " aspirasi:");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Status");
        System.out.println("  " + "─".repeat(66));
        for (String id : hasilIds) {
            aspirationMap.get(id).displaySummary();
        }
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.print("  Lihat detail? Masukkan ID (0=batal): ");
        String pilihanId = sc.nextLine().trim().toUpperCase();
        if (!pilihanId.equals("0") && aspirationMap.containsKey(pilihanId)) {
            aspirationMap.get(pilihanId).displayDetail();
        }
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
                    lihatAntreanDualQueue();
                    break;
                case 4:
                    dashboardStatistik(admin);
                    break;
                case 5:
                    distribusiInstitusi(admin);
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

        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Pelapor");
        System.out.println("  " + "─".repeat(66));
        for (Aspiration asp : verificationQueue) {
            String judul = asp.getTitle().length() > 28
                    ? asp.getTitle().substring(0, 28) + ".." : asp.getTitle();
            System.out.printf("  %-8s | %-30s | %-15s | %s%n",
                    asp.getId(), judul, asp.getCategory(), asp.getAuthor());
        }
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.print("  ID aspirasi yang akan diverifikasi (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration aspirasi = null;
        for (Aspiration asp : verificationQueue) {
            if (asp.getId().equals(id)) { aspirasi = asp; break; }
        }

        if (aspirasi == null) {
            System.out.println("  ID tidak ditemukan dalam antrean verifikasi.");
            return;
        }

        aspirasi.displayDetail();

        System.out.println();
        System.out.println("  [1] Setujui aspirasi");
        System.out.println("  [2] Tolak aspirasi");
        System.out.println("  [0] Batal");
        System.out.print("  Pilihan Anda: ");
        int pilihan = bacaInt();

        switch (pilihan) {
            case 1:
                verificationQueue.remove(aspirasi);
                aspirasi.setStatus(sigap.enums.Status.APPROVED);
                System.out.println("  Aspirasi " + aspirasi.getId() + " disetujui.");
                break;
            case 2:
                verificationQueue.remove(aspirasi);
                aspirasi.setStatus(sigap.enums.Status.REJECTED);
                System.out.println("  Aspirasi " + aspirasi.getId() + " ditolak.");
                break;
            default:
                System.out.println("  Dibatalkan. Aspirasi tetap dalam antrean.");
                return;
        }

        saveData();
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
        System.out.printf("  S_auth  x 0.30 = %.3f%n", sAuth               * 0.30);
        System.out.printf("  S_safe  x 0.25 = %.3f%n", sSafe               * 0.25);
        System.out.printf("  S_scale x 0.15 = %.3f%n", target.calcSScale() * 0.15);
        System.out.printf("  S_cat   x 0.15 = %.3f%n", target.calcSCat()   * 0.15);
        System.out.printf("  S_sys   x 0.15 = %.3f%n", target.calcSSys()   * 0.15);
        System.out.println("  ──────────────────────────────────────────────");
        System.out.printf("  Total Score    = %.2f / 10.0  [%s]%n", preview, tierLabel);

        System.out.println();
        System.out.print("  Kunci skor dan simpan prioritas? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();
        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("  Penentuan prioritas dibatalkan.");
            return;
        }

        target.lockScore(sAuth, sSafe);
        saveData();

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    PRIORITAS BERHASIL DIKUNCI!           ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.printf("    ID        : %s%n",          target.getId());
        System.out.printf("    Skor      : %.2f / 10.0%n", target.getTotalScore());
        System.out.printf("    Prioritas : %s%n",          target.getPriority().getLabel());
        System.out.println("    [Skor tidak dapat diubah oleh siapapun]");
        System.out.println("    Gunakan menu [5] Distribusi Institusi untuk mengirim laporan ini.");
    }

    static void dashboardInstitutionAdmin(InstitutionAdmin ia) {
        boolean loggedIn = true;

        while (loggedIn) {
            ia.showDashboard();
            System.out.print("  Pilihan Anda: ");

            int pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    prosesLaporanInstitusi(ia);
                    break;
                case 2:
                    updateStatusLaporan(ia);
                    break;
                case 3:
                    registrasiAdminInstitusi(ia);
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

    static void distribusiInstitusi(Admin admin) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          DISTRIBUSI KE INSTITUSI         ║");
        System.out.println("╠══════════════════════════════════════════╣");

        List<Aspiration> siap = new ArrayList<>();
        for (Aspiration asp : aspirationMap.values()) {
            if (asp.isScoreLocked() && !asp.isDistributed()) siap.add(asp);
        }

        if (siap.isEmpty()) {
            System.out.println("  Tidak ada laporan yang siap didistribusi.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-10s | %s%n", "ID", "Judul", "Prioritas", "Skor");
        System.out.println("  " + "─".repeat(62));
        for (Aspiration asp : siap) {
            String judul = asp.getTitle().length() > 28 ? asp.getTitle().substring(0, 28) + ".." : asp.getTitle();
            System.out.printf("  %-8s | %-30s | %-10s | %.2f%n",
                    asp.getId(), judul, asp.getPriority().getLabel(), asp.getTotalScore());
        }
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  ID laporan (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration target = aspirationMap.get(id);
        if (target == null || !target.isScoreLocked() || target.isDistributed()) {
            System.out.println("  ID tidak valid atau laporan sudah didistribusi.");
            return;
        }

        if (institutionMap.isEmpty()) {
            System.out.println("  Tidak ada institusi terdaftar.");
            return;
        }

        System.out.println();
        String[] names = institutionMap.keySet().toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, names[i]);
        }
        System.out.print("  Pilih institusi tujuan: ");
        int pilihan = bacaInt();
        if (pilihan < 1 || pilihan > names.length) {
            System.out.println("  Pilihan tidak valid. Distribusi dibatalkan.");
            return;
        }

        String instName = names[pilihan - 1];
        Institution inst = institutionMap.get(instName);
        target.setInstitutionTarget(instName);
        inst.addAspiration(target);
        target.setDistributed(true);
        saveData();

        System.out.println();
        System.out.println("  Laporan " + target.getId() + " berhasil dikirim ke " + instName + ".");
    }

    static void prosesLaporanInstitusi(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         PROSES LAPORAN INSTITUSI         ║");
        System.out.println("╠══════════════════════════════════════════╣");

        Institution inst = institutionMap.get(ia.getInstitutionName());
        if (inst == null || inst.getJumlahAntrian() == 0) {
            System.out.println("  Tidak ada laporan dalam antrean institusi Anda.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        Aspiration laporan = inst.prosesAspirasi();
        laporan.displayDetail();

        System.out.println();
        System.out.println("  [1] Tandai Sedang Diproses");
        System.out.println("  [0] Kembalikan ke antrean");
        System.out.print("  Pilihan Anda: ");
        int pilihan = bacaInt();

        if (pilihan == 1) {
            laporan.setStatus(Status.ON_PROGRESS);
            System.out.println("  Status laporan " + laporan.getId() + " diperbarui: Sedang Diproses.");
        } else {
            inst.addAspiration(laporan);
            System.out.println("  Laporan dikembalikan ke antrean.");
        }
        saveData();
    }

    static void updateStatusLaporan(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         UPDATE STATUS LAPORAN            ║");
        System.out.println("╠══════════════════════════════════════════╣");

        List<Aspiration> onProgress = new ArrayList<>();
        for (Aspiration asp : aspirationMap.values()) {
            if (asp.getInstitutionTarget().equals(ia.getInstitutionName())
                    && asp.getStatus() == Status.ON_PROGRESS) {
                onProgress.add(asp);
            }
        }

        if (onProgress.isEmpty()) {
            System.out.println("  Tidak ada laporan sedang diproses oleh institusi Anda.");
            System.out.println("╚══════════════════════════════════════════╝");
            return;
        }

        System.out.printf("  %-8s | %-30s | %s%n", "ID", "Judul", "Upvotes");
        System.out.println("  " + "─".repeat(55));
        for (Aspiration asp : onProgress) {
            String judul = asp.getTitle().length() > 28 ? asp.getTitle().substring(0, 28) + ".." : asp.getTitle();
            System.out.printf("  %-8s | %-30s | %d%n", asp.getId(), judul, asp.getUpvotes());
        }
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  ID laporan yang akan ditutup (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration target = aspirationMap.get(id);
        if (target == null
                || !target.getInstitutionTarget().equals(ia.getInstitutionName())
                || target.getStatus() != Status.ON_PROGRESS) {
            System.out.println("  ID tidak valid atau laporan tidak memenuhi syarat.");
            return;
        }

        System.out.println();
        System.out.print("  Closing statement (apa yang telah dilakukan): ");
        String stmt = sc.nextLine().trim();
        if (stmt.isEmpty()) {
            System.out.println("  Closing statement tidak boleh kosong.");
            return;
        }

        System.out.print("  Bukti penyelesaian (URL/deskripsi foto): ");
        String bukti = sc.nextLine().trim();
        if (bukti.isEmpty()) bukti = "(tidak ada)";

        target.setClosingStatement(stmt);
        target.setStatus(Status.DONE);
        saveData();

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║     LAPORAN DITANDAI SELESAI!        ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println("    ID        : " + target.getId());
        System.out.println("    Statement : " + stmt);
        System.out.println("    Bukti     : " + bukti);
        System.out.println("  [NOTIF] Pelapor " + target.getAuthor() + " telah diberitahu.");
        System.out.println("  [INFO]  Laporan dianggap selesai jika tidak ada komplain dalam 3x24 jam.");
    }

    static void registrasiAdminInstitusi(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       REGISTRASI ADMIN INSTITUSI         ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("  Institusi : " + ia.getInstitutionName());
        System.out.println();

        System.out.print("  Nama Admin   : ");
        String nama = sc.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("  Nama tidak boleh kosong.");
            return;
        }

        System.out.print("  Username     : ");
        String username = sc.nextLine().trim().toLowerCase();
        if (username.isEmpty() || username.contains(" ")) {
            System.out.println("  Username tidak valid.");
            return;
        }
        if (userMap.containsKey(username)) {
            System.out.println("  Username '" + username + "' sudah digunakan.");
            return;
        }

        System.out.print("  Password     : ");
        String password = sc.nextLine().trim();
        if (password.length() < 6) {
            System.out.println("  Password minimal 6 karakter.");
            return;
        }

        InstitutionAdmin adminBaru = new InstitutionAdmin(username, password, nama, ia.getInstitutionName());
        userMap.put(username, adminBaru);
        saveData();

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║   AKUN ADMIN INSTITUSI DIBUAT!       ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println("    Nama      : " + nama);
        System.out.println("    Username  : " + username);
        System.out.println("    Institusi : " + ia.getInstitutionName());
    }

    static void dashboardStatistik(Admin admin) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           DASHBOARD STATISTIK SIGAP          ║");
        System.out.println("╠══════════════════════════════════════════════╣");

        int total = aspirationMap.size();
        int done = 0, pending = 0, onProgress = 0;
        Aspiration topUpvote = null;
        HashMap<String, Integer> perInstitusi = new HashMap<>();
        HashMap<String, Integer> perKategori  = new HashMap<>();

        for (Aspiration asp : aspirationMap.values()) {
            Status s = asp.getStatus();
            if      (s == Status.DONE)        done++;
            else if (s == Status.PENDING)     pending++;
            else if (s == Status.ON_PROGRESS) onProgress++;

            String inst = asp.getInstitutionTarget();
            if (!inst.equals("-")) perInstitusi.merge(inst, 1, Integer::sum);

            perKategori.merge(asp.getCategory(), 1, Integer::sum);

            if (topUpvote == null || asp.getUpvotes() > topUpvote.getUpvotes()) topUpvote = asp;
        }

        String trending = "-";
        int maxKat = 0;
        for (Map.Entry<String, Integer> e : perKategori.entrySet()) {
            if (e.getValue() > maxKat) { maxKat = e.getValue(); trending = e.getKey(); }
        }

        System.out.printf("  Total Laporan      : %d%n", total);
        System.out.printf("  Selesai            : %d%n", done);
        System.out.printf("  Sedang Diproses    : %d%n", onProgress);
        System.out.printf("  Pending            : %d%n", pending);
        System.out.println();
        System.out.println("  ── Per Institusi ──────────────────────────────");
        if (perInstitusi.isEmpty()) {
            System.out.println("  (belum ada laporan yang didistribusi)");
        } else {
            for (Map.Entry<String, Integer> e : perInstitusi.entrySet()) {
                System.out.printf("  %-35s : %d laporan%n", e.getKey(), e.getValue());
            }
        }
        System.out.println();
        if (topUpvote != null) {
            System.out.println("  ── Upvote Tertinggi ───────────────────────────");
            System.out.printf("  %s | %s | %d votes%n",
                    topUpvote.getId(), topUpvote.getTitle(), topUpvote.getUpvotes());
        }
        System.out.println();
        System.out.printf("  Trending Issue     : %s (%d laporan)%n", trending, maxKat);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    static void lihatAntreanDualQueue() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║             DUAL-QUEUE SYSTEM VIEW               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");


        System.out.println();
        System.out.println(" ANTREAN PRIORITAS ");
        if (institutionMap.isEmpty()) {
            System.out.println("  │  (tidak ada institusi)");
        } else {
            for (Institution inst : institutionMap.values()) {
                System.out.println("  │");
                System.out.println("  │  " + inst.getNamaInstitution() + "  (" + inst.getJumlahAntrian() + " laporan)");

                if (inst.getJumlahAntrian() == 0) {
                    System.out.println("  │    (kosong)");
                } else {
                    List<Aspiration> sorted = new ArrayList<>(inst.getQueue());
                    Collections.sort(sorted);

                    int rank = 1;
                    for (Aspiration asp : sorted) {
                        String judul = asp.getTitle();
                        if (judul.length() > 18) {
                            judul = judul.substring(0, 18) + "..";
                        }
                        System.out.println("  │    #" + rank
                                + "  " + asp.getId()
                                + " | " + judul
                                + " | Skor: " + String.format("%.2f", asp.getTotalScore())
                                + " | " + asp.getPriority().getLabel());
                        rank++;
                    }
                }
            }
        }
    }

    static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "{{PIPE}}").replace("\n", "{{NL}}");
    }

    static String unescape(String s) {
        if (s == null) return "";
        return s.replace("{{NL}}", "\n").replace("{{PIPE}}", "|");
    }

    static void saveData() {
        try {
            new File(DATA_DIR).mkdirs();

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/users.txt"), "UTF-8"))) {
                for (User u : userMap.values()) {
                    String instName = u.getRole().equals("INSTITUTION_ADMIN")
                            ? ((InstitutionAdmin) u).getInstitutionName() : "";
                    pw.println(escape(u.getRole()) + "|" + escape(u.getUsername()) + "|"
                            + escape(u.getPassword()) + "|" + escape(u.getNama()) + "|" + escape(instName));
                }
            }

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/aspirations.txt"), "UTF-8"))) {
                for (Aspiration asp : aspirationMap.values()) {
                    StringBuilder upvoters = new StringBuilder();
                    for (String u : asp.getUpvotedUsers()) {
                        if (upvoters.length() > 0) upvoters.append(";");
                        upvoters.append(escape(u));
                    }
                    pw.println(escape(asp.getId()) + "|" + escape(asp.getTitle()) + "|"
                            + escape(asp.getDescription()) + "|" + escape(asp.getCategory()) + "|"
                            + escape(asp.getLocation()) + "|" + escape(asp.getAuthor()) + "|"
                            + asp.getStatus().name() + "|" + asp.getUpvotes() + "|"
                            + escape(asp.getInstitutionTarget()) + "|" + asp.isScoreLocked() + "|"
                            + asp.isDistributed() + "|" + escape(asp.getClosingStatement()) + "|"
                            + asp.getSAuth() + "|" + asp.getSSafe() + "|" + asp.getTotalScore() + "|"
                            + escape(asp.getCreatedAt()) + "|" + upvoters.toString());
                }
            }

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/institutions.txt"), "UTF-8"))) {
                for (Institution inst : institutionMap.values()) {
                    StringBuilder queueIds = new StringBuilder();
                    for (Aspiration asp : inst.getQueue()) {
                        if (queueIds.length() > 0) queueIds.append(";");
                        queueIds.append(asp.getId());
                    }
                    pw.println(escape(inst.getNamaInstitution()) + "|" + queueIds.toString());
                }
            }

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/verqueue.txt"), "UTF-8"))) {
                for (Aspiration asp : verificationQueue) {
                    pw.println(asp.getId());
                }
            }

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/counter.txt"), "UTF-8"))) {
                pw.println(aspirationCounter);
            }

        } catch (Exception e) {
            System.out.println("  [WARN] Gagal menyimpan data: " + e.getMessage());
        }
    }

    static void loadData() {
        File counterFile = new File(DATA_DIR + "/counter.txt");
        if (!counterFile.exists()) {
            // inisialisasiDummyData();
            saveData();
            return;
        }
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(DATA_DIR + "/counter.txt"), "UTF-8"))) {
                String line = br.readLine();
                if (line != null) aspirationCounter = Integer.parseInt(line.trim());
            }

            File usersFile = new File(DATA_DIR + "/users.txt");
            if (usersFile.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(usersFile), "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        String[] p = line.split("\\|", -1);
                        if (p.length < 5) continue;
                        String role     = unescape(p[0]);
                        String username = unescape(p[1]);
                        String password = unescape(p[2]);
                        String nama     = unescape(p[3]);
                        String instName = unescape(p[4]);
                        User u;
                        switch (role) {
                            case "ADMIN":
                                u = new Admin(username, password, nama); break;
                            case "INSTITUTION_ADMIN":
                                u = new InstitutionAdmin(username, password, nama, instName); break;
                            default:
                                u = new Citizen(username, password, nama); break;
                        }
                        userMap.put(username, u);
                    }
                }
            }

            File aspFile = new File(DATA_DIR + "/aspirations.txt");
            if (aspFile.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(aspFile), "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        String[] p = line.split("\\|", -1);
                        if (p.length < 16) continue;
                        String  id          = unescape(p[0]);
                        String  title       = unescape(p[1]);
                        String  description = unescape(p[2]);
                        String  category    = unescape(p[3]);
                        String  location    = unescape(p[4]);
                        String  author      = unescape(p[5]);
                        Status  status      = Status.valueOf(p[6]);
                        String  instTarget  = unescape(p[8]);
                        boolean scoreLocked = Boolean.parseBoolean(p[9]);
                        boolean distributed = Boolean.parseBoolean(p[10]);
                        String  closingStmt = unescape(p[11]);
                        double  sAuth       = Double.parseDouble(p[12]);
                        double  sSafe       = Double.parseDouble(p[13]);
                        double  totalScore  = Double.parseDouble(p[14]);
                        String  createdAt   = unescape(p[15]);
                        String  upvotersStr = p.length > 16 ? p[16] : "";

                        Aspiration asp = new Aspiration(id, title, description, category, location, author);
                        asp.setCreatedAt(createdAt);
                        asp.setStatus(status);
                        asp.setInstitutionTarget(instTarget);
                        asp.setDistributed(distributed);
                        asp.setClosingStatement(closingStmt);
                        if (!upvotersStr.isEmpty()) {
                            for (String u : upvotersStr.split(";")) {
                                asp.addUpvote(unescape(u));
                            }
                        }
                        if (scoreLocked) asp.restoreScore(sAuth, sSafe, totalScore);
                        aspirationMap.put(id, asp);
                    }
                }
            }

            File instFile = new File(DATA_DIR + "/institutions.txt");
            if (instFile.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(instFile), "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        String[] p = line.split("\\|", -1);
                        String instName = unescape(p[0]);
                        Institution inst = new Institution(instName);
                        institutionMap.put(instName, inst);
                        if (p.length > 1 && !p[1].isEmpty()) {
                            for (String aspId : p[1].split(";")) {
                                Aspiration asp = aspirationMap.get(aspId.trim());
                                if (asp != null) inst.addAspiration(asp);
                            }
                        }
                    }
                }
            }

            File vqFile = new File(DATA_DIR + "/verqueue.txt");
            if (vqFile.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(vqFile), "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        Aspiration asp = aspirationMap.get(line.trim());
                        if (asp != null) verificationQueue.add(asp);
                    }
                }
            }

            System.out.println();
            System.out.println("  [SISTEM] Data berhasil dimuat dari penyimpanan.");
            System.out.println("  [SISTEM] " + userMap.size() + " user, "
                    + aspirationMap.size() + " aspirasi siap.");

        } catch (Exception e) {
            System.out.println("  [WARN] Gagal memuat data: " + e.getMessage() + ". Memuat data awal...");
            userMap.clear();
            aspirationMap.clear();
            verificationQueue.clear();
            institutionMap.clear();
            // inisialisasiDummyData();
            saveData();
        }
    }

    // ── INVERTED INDEX ───────────────────────────────────────────────────────

    static void buildInvertedIndex() {
        invertedIndex.clear();
        for (Aspiration asp : aspirationMap.values()) {
            indexAspiration(asp);
        }
    }

    static void indexAspiration(Aspiration asp) {
        String gabungan = asp.getTitle()       + " "
                        + asp.getDescription() + " "
                        + asp.getCategory()    + " "
                        + asp.getLocation();

        String[] words = gabungan.toLowerCase().split("[^a-zA-Z0-9]+");

        for (String word : words) {
            if (word.isEmpty()) continue;
            invertedIndex.computeIfAbsent(word, k -> new LinkedList<>());
            if (!invertedIndex.get(word).contains(asp.getId())) {
                invertedIndex.get(word).add(asp.getId());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

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

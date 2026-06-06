package sigap;

import java.io.*;
import java.util.*;
import sigap.enums.Status;


public class MainFlow {

    // #region STATIC FIELDS & INDEXES
    static HashMap<String, User> userMap = new HashMap<>();
    static HashMap<String, Aspiration> aspirationMap = new HashMap<>();
    static LinkedList<Aspiration> verificationQueue = new LinkedList<>();
    static HashMap<String, Institution> institutionMap = new HashMap<>();
    static HashMap<String, LinkedList<String>> invertedIndex = new HashMap<>();

    // -- O(1) indexes & caches (rebuilt on load, maintained incrementally) --

    // #1 - O(1) lookup inside verification queue
    static HashMap<String, Aspiration> verificationQueueMap = new HashMap<>();

    // #2 - approved but not yet scored (for prioritization)
    static LinkedList<Aspiration> approvedUnscoredList = new LinkedList<>();

    // #3 - scored but not yet distributed
    static LinkedList<Aspiration> readyToDistributeList = new LinkedList<>();

    // #4 & #5 - per-institution ON_PROGRESS and DONE lists
    static HashMap<String, LinkedList<Aspiration>> institutionOnProgressMap = new HashMap<>();
    static HashMap<String, LinkedList<Aspiration>> institutionDoneMap       = new HashMap<>();

    // #6 - cached statistics (updated incrementally, O(1) on display)
    static int statTotal      = 0;
    static int statDone       = 0;
    static int statPending    = 0;
    static int statOnProgress = 0;
    static HashMap<String, Integer> statPerInstitusi = new HashMap<>();
    static HashMap<String, Integer> statPerKategori  = new HashMap<>();
    static Aspiration statTopUpvote = null;

    static final String DATA_DIR = "data";
    static int aspirationCounter = 1;
    static Scanner sc = new Scanner(System.in);
    static User currentUser = null;
    // #endregion

    // #region MAIN & INITIALIZATION
    public static void main(String[] args) {
        printBanner();
        loadData();
        buildInvertedIndex();
        rebuildIndexes();
        tekanEnterUntukMulai();
        menuUtama();
        sc.close();
    }

    // Rebuilds all O(1) indexes from current aspirationMap + verificationQueue.
    // Called once after loadData(). After that, each method maintains its own slice.
    static void rebuildIndexes() {
        verificationQueueMap.clear();
        approvedUnscoredList.clear();
        readyToDistributeList.clear();
        institutionOnProgressMap.clear();
        institutionDoneMap.clear();

        statTotal      = aspirationMap.size();
        statDone       = 0;
        statPending    = 0;
        statOnProgress = 0;
        statPerInstitusi.clear();
        statPerKategori.clear();
        statTopUpvote  = null;

        for (Aspiration asp : verificationQueue) {
            verificationQueueMap.put(asp.getId(), asp);
        }

        for (Aspiration asp : aspirationMap.values()) {
            Status s    = asp.getStatus();
            String inst = asp.getInstitutionTarget();

            if      (s == Status.DONE)        statDone++;
            else if (s == Status.PENDING)     statPending++;
            else if (s == Status.ON_PROGRESS) statOnProgress++;

            if (!inst.equals("-")) statPerInstitusi.merge(inst, 1, Integer::sum);
            statPerKategori.merge(asp.getCategory(), 1, Integer::sum);
            if (statTopUpvote == null || asp.getUpvotes() > statTopUpvote.getUpvotes())
                statTopUpvote = asp;

            if (s == Status.APPROVED && !asp.isScoreLocked())
                approvedUnscoredList.add(asp);

            if (asp.isScoreLocked() && !asp.isDistributed())
                readyToDistributeList.add(asp);

            if (s == Status.ON_PROGRESS && !inst.equals("-"))
                institutionOnProgressMap.computeIfAbsent(inst, k -> new LinkedList<>()).add(asp);

            if (s == Status.DONE && !inst.equals("-"))
                institutionDoneMap.computeIfAbsent(inst, k -> new LinkedList<>()).add(asp);
        }
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
        System.out.println("──────────────────────────────────────────────────");
    }


    static void tekanEnterUntukMulai() {
        System.out.println("  Tekan [Enter] untuk memulai...");
        sc.nextLine();
    }


    static void printHeader() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  SIGAP - Sistem Informasi Aspirasi Publik");
        System.out.println("──────────────────────────────────────────────────");
    }

    static String generateId() {
        String id = "ASP" + String.format("%03d", aspirationCounter);
        aspirationCounter++;
        return id;
    }
    // #endregion

    // #region AUTH
    static void menuUtama() {
        boolean running = true;

        while (running) {
            printHeader();
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  MENU UTAMA");
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  [1] Register Akun Baru");
            System.out.println("  [2] Login");
            System.out.println("  [0] Keluar Program");
            System.out.println("──────────────────────────────────────────────────");
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  REGISTRASI AKUN WARGA");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Daftarkan diri Anda untuk mulai");
        System.out.println("  menyampaikan aspirasi kepada pemerintah.");
        System.out.println("──────────────────────────────────────────────────");
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  REGISTRASI BERHASIL!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("    Nama     : " + nama);
        System.out.println("    Username : " + username);
        System.out.println("    Role     : Warga / Citizen");
        System.out.println();
        System.out.println("  Silakan login dengan akun Anda.");
    }


    static void prosesLogin() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  LOGIN");
        System.out.println("──────────────────────────────────────────────────");
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
    // #endregion

    // #region CITIZEN DASHBOARD
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
                    tampilkanSemuaAspirasi(citizen);
                    break;
                case 3:
                    upvoteAspiration(citizen);
                    break;
                case 4:
                    cariAspirasi();
                    break;
                case 5:
                    prosesKomentarAspirasi(citizen);
                    break;
                case 0:
                    citizen.logout();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("   Pilihan tidak valid! Masukkan angka 0 - 5.");
            }
        }
    }

    static void prosesTambahAspirasi(Citizen citizen) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  TAMBAH ASPIRASI BARU");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Sampaikan aspirasi Anda");
        System.out.println("──────────────────────────────────────────────────");
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Konfirmasi Aspirasi");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Judul     : " + title);
        System.out.println("  Deskripsi : " + description);
        System.out.println("  Kategori  : " + category);
        System.out.println("  Lokasi    : " + location);
        System.out.println("──────────────────────────────────────────────────");
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
        verificationQueueMap.put(newId, aspirasiBaru);          // #1
        indexAspiration(aspirasiBaru);
        statTotal++;                                            // #6
        statPending++;
        statPerKategori.merge(category, 1, Integer::sum);
        if (statTopUpvote == null) statTopUpvote = aspirasiBaru;
        saveData();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  ASPIRASI BERHASIL DIKIRIM!");
        System.out.println("──────────────────────────────────────────────────");
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

    static void tampilkanSemuaAspirasi(Citizen citizen) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DAFTAR ASPIRASI PUBLIK");
        System.out.println("──────────────────────────────────────────────────");

        if (aspirationMap.isEmpty()) {
            System.out.println("  Belum ada aspirasi yang tersedia.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        // -- My Aspirations first --
        LinkedList<Aspiration> mine  = new LinkedList<>();
        LinkedList<Aspiration> other = new LinkedList<>();
        for (Aspiration asp : aspirationMap.values()) {
            if (asp.getAuthor().equals(citizen.getUsername())) mine.add(asp);
            else                                               other.add(asp);
        }

        System.out.println("  [ Aspirasi Saya ]");
        if (mine.isEmpty()) {
            System.out.println("  (belum ada aspirasi)");
        } else {
            for (Aspiration asp : mine) asp.displaySummary();
        }

        System.out.println();
        System.out.println("  [ Aspirasi Publik Lainnya ]");
        if (other.isEmpty()) {
            System.out.println("  (tidak ada aspirasi lain)");
        } else {
            for (Aspiration asp : other) asp.displaySummary();
        }

        System.out.println("──────────────────────────────────────────────────");
    }

    static void upvoteAspiration(Citizen citizen) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  UPVOTE ASPIRASI");
        System.out.println("──────────────────────────────────────────────────");

        if (aspirationMap.isEmpty()) {
            System.out.println("  Tidak ada aspirasi untuk diupvote saat ini.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        for (Aspiration aspirasi : aspirationMap.values()) {
            aspirasi.displaySummary();
        }

        System.out.println("──────────────────────────────────────────────────");
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

        if (aspirasi.getStatus() == Status.REJECTED) {
            System.out.println("  Aspirasi ini sudah ditolak dan tidak dapat diupvote.");
            return;
        }

        if (!aspirasi.addUpvote(citizen.getUsername())) {
            System.out.println("  Anda sudah pernah mengupvote aspirasi ini sebelumnya.");
            return;
        }

        if (statTopUpvote == null || aspirasi.getUpvotes() > statTopUpvote.getUpvotes())
            statTopUpvote = aspirasi;                           // #6

        saveData();
        System.out.println("  Terima kasih! Aspirasi " + id + " berhasil diupvote.");
        System.out.println("  Total upvote sekarang: " + aspirasi.getUpvotes());
    }

    static void cariAspirasi() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  CARI ASPIRASI");
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  Masukkan kata kunci: ");
        String input = sc.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            System.out.println("  Kata kunci tidak boleh kosong.");
            System.out.println("──────────────────────────────────────────────────");
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
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.println("  Ditemukan " + hasilIds.size() + " aspirasi:");
        System.out.println("──────────────────────────────────────────────────");
        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Status");
        System.out.println("  " + "-".repeat(66));
        for (String id : hasilIds) {
            aspirationMap.get(id).displaySummary();
        }
        System.out.println("──────────────────────────────────────────────────");

        System.out.print("  Lihat detail? Masukkan ID (0=batal): ");
        String pilihanId = sc.nextLine().trim().toUpperCase();
        if (!pilihanId.equals("0") && aspirationMap.containsKey(pilihanId)) {
            aspirationMap.get(pilihanId).displayDetail();
        }
    }

    static void prosesKomentarAspirasi(Citizen citizen) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  BERI KOMENTAR PADA ASPIRASI");
        System.out.println("──────────────────────────────────────────────────");

        if (aspirationMap.isEmpty()) {
            System.out.println("  Belum ada aspirasi yang tersedia.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        for (Aspiration asp : aspirationMap.values()) {
            asp.displaySummary();
        }

        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  Masukkan ID aspirasi yang ingin dikomentari (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration target = aspirationMap.get(id);
        if (target == null) {
            System.out.println("  Aspirasi dengan ID " + id + " tidak ditemukan.");
            return;
        }

        target.displayDetail();

        System.out.println();
        System.out.print("  Tulis komentar Anda: ");
        String teks = sc.nextLine().trim();
        if (teks.isEmpty()) {
            System.out.println("  Komentar tidak boleh kosong.");
            return;
        }

        CommentAspiration komentar = new CommentAspiration(citizen.getUsername(), teks);
        target.addComment(komentar);
        saveComments();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  KOMENTAR BERHASIL DITAMBAHKAN!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Aspirasi : " + target.getTitle());
        System.out.println("  Komentar : " + teks);
        System.out.println("  Waktu    : " + komentar.getTimestamp());
    }
    // #endregion

    // #region ADMIN DASHBOARD
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
                case 6:
                    kelolaInstitusi(admin);
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  VERIFIKASI ASPIRASI");
        System.out.println("──────────────────────────────────────────────────");

        if (verificationQueue.isEmpty()) {
            System.out.println("  Tidak ada aspirasi menunggu verifikasi.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Pelapor");
        System.out.println("  " + "-".repeat(66));
        for (Aspiration asp : verificationQueue) {
            String judul = truncate(asp.getTitle(), 28);
            System.out.printf("  %-8s | %-30s | %-15s | %s%n",
                    asp.getId(), judul, asp.getCategory(), asp.getAuthor());
        }
        System.out.println("──────────────────────────────────────────────────");

        System.out.print("  ID aspirasi yang akan diverifikasi (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration aspirasi = verificationQueueMap.get(id);    // #1 - O(1)

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
                verificationQueueMap.remove(id);                // #1
                aspirasi.setStatus(Status.APPROVED);
                approvedUnscoredList.add(aspirasi);             // #2
                statPending--;                                  // #6
                System.out.println("  Aspirasi " + aspirasi.getId() + " disetujui.");
                break;
            case 2:
                verificationQueue.remove(aspirasi);
                verificationQueueMap.remove(id);                // #1
                aspirasi.setStatus(Status.REJECTED);
                statPending--;                                  // #6
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  PENENTUAN PRIORITAS");
        System.out.println("──────────────────────────────────────────────────");

        if (approvedUnscoredList.isEmpty()) {                  // #2 - O(1) check
            System.out.println("  Tidak ada aspirasi siap diprioritaskan.");
            System.out.println("  Pastikan ada aspirasi berstatus DISETUJUI.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-15s | %s%n", "ID", "Judul", "Kategori", "Votes");
        System.out.println("  " + "-".repeat(66));
        for (Aspiration asp : approvedUnscoredList) {          // #2 - iterate only eligible
            System.out.printf("  %-8s | %-30s | %-15s | %d%n",
                    asp.getId(),
                    truncate(asp.getTitle(), 28),
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Input Admin");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  S_auth (30%) - Otoritas Admin [1-10]");
        System.out.println("  Nilai RENDAH = laporan dilebih-lebihkan.");
        System.out.println("  Nilai TINGGI = darurat, butuh penanganan segera.");
        System.out.print("  S_auth = ");
        double sAuth = bacaDouble(1, 10);

        System.out.println();
        System.out.println("  S_safe (25%) - Tingkat Ancaman Keselamatan [1-10]");
        System.out.println("  1=Tidak berbahaya   5=Cukup berbahaya   10=Mengancam nyawa");
        System.out.print("  S_safe = ");
        double sSafe = bacaDouble(1, 10);

        double preview = target.previewScore(sAuth, sSafe);
        String tierLabel;
        if      (preview >= 7.0) tierLabel = "TINGGI";
        else if (preview >= 4.0) tierLabel = "SEDANG";
        else                     tierLabel = "RENDAH";

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Kalkulasi Skor Final");
        System.out.println("──────────────────────────────────────────────────");
        System.out.printf("  S_auth  x 0.30 = %.3f%n", sAuth               * 0.30);
        System.out.printf("  S_safe  x 0.25 = %.3f%n", sSafe               * 0.25);
        System.out.printf("  S_scale x 0.15 = %.3f%n", target.calcSScale() * 0.15);
        System.out.printf("  S_cat   x 0.15 = %.3f%n", target.calcSCat()   * 0.15);
        System.out.printf("  S_sys   x 0.15 = %.3f%n", target.calcSSys()   * 0.15);
        System.out.println("──────────────────────────────────────────────────");
        System.out.printf("  Total Score    = %.2f / 10.0  [%s]%n", preview, tierLabel);

        System.out.println();
        System.out.print("  Kunci skor dan simpan prioritas? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();
        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("  Penentuan prioritas dibatalkan.");
            return;
        }

        target.lockScore(sAuth, sSafe);
        approvedUnscoredList.remove(target);                   // #2
        readyToDistributeList.add(target);                     // #3
        saveData();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  PRIORITAS BERHASIL DIKUNCI!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.printf("    ID        : %s%n",          target.getId());
        System.out.printf("    Skor      : %.2f / 10.0%n", target.getTotalScore());
        System.out.printf("    Prioritas : %s%n",          target.getPriority().getLabel());
        System.out.println("    [Skor tidak dapat diubah oleh siapapun]");
        System.out.println("    Gunakan menu [5] Distribusi Institusi untuk mengirim laporan ini.");
    }

    static void kelolaInstitusi(Admin admin) {
        boolean open = true;
        while (open) {
            System.out.println();
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  KELOLA INSTITUSI");
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  Jumlah institusi terdaftar: " + institutionMap.size());
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  [1] Tambah Institusi");
            System.out.println("  [2] Hapus Institusi");
            System.out.println("  [3] Lihat Daftar Institusi");
            System.out.println("  [0] Kembali");
            System.out.println("──────────────────────────────────────────────────");
            System.out.print("  Pilihan Anda: ");

            switch (bacaInt()) {
                case 1: tambahInstitusi(); break;
                case 2: hapusInstitusi();  break;
                case 3: lihatDaftarInstitusi(); break;
                case 0: open = false;      break;
                default: System.out.println("  Pilihan tidak valid.");
            }
        }
    }

    static void lihatDaftarInstitusi() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DAFTAR INSTITUSI");
        System.out.println("──────────────────────────────────────────────────");
        if (institutionMap.isEmpty()) {
            System.out.println("  (belum ada institusi terdaftar)");
        } else {
            int no = 1;
            for (Institution inst : institutionMap.values()) {
                LinkedList<Aspiration> onProg = institutionOnProgressMap.get(inst.getNamaInstitution());
                LinkedList<Aspiration> done   = institutionDoneMap.get(inst.getNamaInstitution());
                System.out.printf("  %d. %s%n", no++, inst.getNamaInstitution());
                System.out.printf("     Antrean: %d  |  Diproses: %d  |  Selesai: %d%n",
                        inst.getJumlahAntrian(),
                        onProg != null ? onProg.size() : 0,
                        done   != null ? done.size()   : 0);
            }
        }
        System.out.println("──────────────────────────────────────────────────");
    }

    static void tambahInstitusi() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  TAMBAH INSTITUSI BARU");
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  Nama Institusi: ");
        String nama = sc.nextLine().trim();

        if (nama.isEmpty()) {
            System.out.println("  Nama institusi tidak boleh kosong.");
            return;
        }
        if (institutionMap.containsKey(nama)) {
            System.out.println("  Institusi \"" + nama + "\" sudah terdaftar.");
            return;
        }

        Institution instBaru = new Institution(nama);
        institutionMap.put(nama, instBaru);
        saveData();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  INSTITUSI BERHASIL DITAMBAHKAN!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Nama: " + nama);
        System.out.println("  Gunakan menu Distribusi Institusi untuk mengirim laporan ke sini.");
    }

    static void hapusInstitusi() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  HAPUS INSTITUSI");
        System.out.println("──────────────────────────────────────────────────");

        if (institutionMap.isEmpty()) {
            System.out.println("  Tidak ada institusi yang terdaftar.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        String[] names = institutionMap.keySet().toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            Institution inst = institutionMap.get(names[i]);
            LinkedList<Aspiration> onProg = institutionOnProgressMap.get(names[i]);
            System.out.printf("  [%d] %s  (Antrean: %d | Diproses: %d)%n",
                    i + 1, names[i],
                    inst.getJumlahAntrian(),
                    onProg != null ? onProg.size() : 0);
        }
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  Nomor institusi yang akan dihapus (0=batal): ");
        int pilihan = bacaInt();

        if (pilihan == 0) return;
        if (pilihan < 1 || pilihan > names.length) {
            System.out.println("  Nomor tidak valid.");
            return;
        }

        String target = names[pilihan - 1];
        Institution inst = institutionMap.get(target);
        LinkedList<Aspiration> onProg = institutionOnProgressMap.get(target);

        // Safety check: block deletion if there are active aspirations
        if (inst.getJumlahAntrian() > 0) {
            System.out.println("  Tidak dapat menghapus: institusi masih memiliki "
                    + inst.getJumlahAntrian() + " laporan dalam antrean.");
            System.out.println("  Selesaikan atau pindahkan laporan terlebih dahulu.");
            return;
        }
        if (onProg != null && !onProg.isEmpty()) {
            System.out.println("  Tidak dapat menghapus: institusi masih memiliki "
                    + onProg.size() + " laporan yang sedang diproses.");
            System.out.println("  Selesaikan laporan tersebut terlebih dahulu.");
            return;
        }

        System.out.print("  Konfirmasi hapus institusi \"" + target + "\"? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();
        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("  Penghapusan dibatalkan.");
            return;
        }

        // Remove from all maps
        institutionMap.remove(target);
        institutionOnProgressMap.remove(target);
        institutionDoneMap.remove(target);
        statPerInstitusi.remove(target);
        saveData();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  INSTITUSI BERHASIL DIHAPUS!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  \"" + target + "\" telah dihapus dari sistem.");
        System.out.println("  Catatan: riwayat laporan yang sudah selesai tetap tersimpan.");
    }

    // #endregion

    // #region INSTITUTION ADMIN DASHBOARD
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
                    lihatRiwayatInstitusi(ia);
                    break;
                case 4:
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DISTRIBUSI KE INSTITUSI");
        System.out.println("──────────────────────────────────────────────────");

        if (readyToDistributeList.isEmpty()) {                 // #3 - O(1) check
            System.out.println("  Tidak ada laporan yang siap didistribusi.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-10s | %s%n", "ID", "Judul", "Prioritas", "Skor");
        System.out.println("  " + "-".repeat(62));
        for (Aspiration asp : readyToDistributeList) {         // #3 - iterate only eligible
            String judul = truncate(asp.getTitle(), 28);
            System.out.printf("  %-8s | %-30s | %-10s | %.2f%n",
                    asp.getId(), judul, asp.getPriority().getLabel(), asp.getTotalScore());
        }
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  ID laporan (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration target = aspirationMap.get(id);             // #3 - O(1) lookup
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
        readyToDistributeList.remove(target);                  // #3
        statPerInstitusi.merge(instName, 1, Integer::sum);     // #6
        saveData();

        System.out.println();
        System.out.println("  Laporan " + target.getId() + " berhasil dikirim ke " + instName + ".");
    }

    static void prosesLaporanInstitusi(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  PROSES LAPORAN INSTITUSI");
        System.out.println("──────────────────────────────────────────────────");

        Institution inst = institutionMap.get(ia.getInstitutionName());
        if (inst == null || inst.getJumlahAntrian() == 0) {
            System.out.println("  Tidak ada laporan dalam antrean institusi Anda.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        List<Aspiration> sorted = new ArrayList<>(inst.getQueue());
        Collections.sort(sorted);

        System.out.printf("  %-4s | %-8s | %-30s | %-8s | %s%n", "No.", "ID", "Judul", "Prioritas", "Skor");
        System.out.println("  " + "-".repeat(68));
        int rank = 1;
        for (Aspiration asp : sorted) {
            String judul = truncate(asp.getTitle(), 28);
            System.out.printf("  %-4d | %-8s | %-30s | %-8s | %.2f%n",
                    rank++, asp.getId(), judul, asp.getPriority().getLabel(), asp.getTotalScore());
        }
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  ID laporan yang akan diproses (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Aspiration laporan = aspirationMap.get(id);
        if (laporan == null || !inst.getQueue().contains(laporan)) {
            System.out.println("  ID tidak ditemukan dalam antrean.");
            return;
        }

        laporan.displayDetail();

        System.out.println();
        System.out.println("  [1] Tandai Sedang Diproses");
        System.out.println("  [0] Batal");
        System.out.print("  Pilihan Anda: ");
        int pilihan = bacaInt();

        if (pilihan == 1) {
            inst.removeAspiration(laporan);
            laporan.setStatus(Status.ON_PROGRESS);
            institutionOnProgressMap                           // #4
                    .computeIfAbsent(ia.getInstitutionName(), k -> new LinkedList<>())
                    .add(laporan);
            statOnProgress++;                                  // #6
            System.out.println("  Status laporan " + laporan.getId() + " diperbarui: Sedang Diproses.");
            saveData();
        }
    }

    static void updateStatusLaporan(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  UPDATE STATUS LAPORAN");
        System.out.println("──────────────────────────────────────────────────");

        LinkedList<Aspiration> onProgress =                    // #4 - O(1) lookup
                institutionOnProgressMap.get(ia.getInstitutionName());

        if (onProgress == null || onProgress.isEmpty()) {
            System.out.println("  Tidak ada laporan sedang diproses oleh institusi Anda.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.printf("  %-8s | %-30s | %s%n", "ID", "Judul", "Upvotes");
        System.out.println("  " + "-".repeat(55));
        for (Aspiration asp : onProgress) {
            String judul = truncate(asp.getTitle(), 28);
            System.out.printf("  %-8s | %-30s | %d%n", asp.getId(), judul, asp.getUpvotes());
        }
        System.out.println("──────────────────────────────────────────────────");
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
        onProgress.remove(target);                             // #4
        institutionDoneMap                                     // #5
                .computeIfAbsent(ia.getInstitutionName(), k -> new LinkedList<>())
                .add(target);
        statDone++;                                            // #6
        statOnProgress--;
        saveData();

        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  LAPORAN DITANDAI SELESAI!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("    ID        : " + target.getId());
        System.out.println("    Statement : " + stmt);
        System.out.println("    Bukti     : " + bukti);
        System.out.println("  [NOTIF] Pelapor " + target.getAuthor() + " telah diberitahu.");
        System.out.println("  [INFO]  Laporan dianggap selesai jika tidak ada komplain dalam 3x24 jam.");
    }

    static void lihatRiwayatInstitusi(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  RIWAYAT LAPORAN SELESAI");
        System.out.println("──────────────────────────────────────────────────");

        LinkedList<Aspiration> selesai =                       // #5 - O(1) lookup
                institutionDoneMap.get(ia.getInstitutionName());

        if (selesai == null || selesai.isEmpty()) {
            System.out.println("  Belum ada laporan yang selesai ditangani.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        System.out.printf("  %-8s | %-30s | %-8s | %s%n", "ID", "Judul", "Prioritas", "Closing Statement");
        System.out.println("  " + "-".repeat(72));
        for (Aspiration asp : selesai) {
            String judul = truncate(asp.getTitle(), 28);
            String stmt  = truncate(asp.getClosingStatement(), 20);
            System.out.printf("  %-8s | %-30s | %-8s | %s%n",
                    asp.getId(), judul, asp.getPriority().getLabel(), stmt);
        }
        System.out.println("──────────────────────────────────────────────────");
        System.out.print("  Lihat detail? Masukkan ID (0=batal): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (!id.equals("0") && aspirationMap.containsKey(id)) {
            aspirationMap.get(id).displayDetail();
        }
    }

    static void registrasiAdminInstitusi(InstitutionAdmin ia) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  REGISTRASI ADMIN INSTITUSI");
        System.out.println("──────────────────────────────────────────────────");
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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  AKUN ADMIN INSTITUSI DIBUAT!");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("    Nama      : " + nama);
        System.out.println("    Username  : " + username);
        System.out.println("    Institusi : " + ia.getInstitutionName());
    }

    // #endregion

    // #region ADMIN TOOLS
    static void dashboardStatistik(Admin admin) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DASHBOARD STATISTIK SIGAP");
        System.out.println("──────────────────────────────────────────────────");

        // #6 - all values are cached; no scan of aspirationMap needed
        String trending = "-";
        int maxKat = 0;
        for (Map.Entry<String, Integer> e : statPerKategori.entrySet()) {
            if (e.getValue() > maxKat) { maxKat = e.getValue(); trending = e.getKey(); }
        }

        System.out.printf("  Total Laporan      : %d%n", statTotal);
        System.out.printf("  Selesai            : %d%n", statDone);
        System.out.printf("  Sedang Diproses    : %d%n", statOnProgress);
        System.out.printf("  Pending            : %d%n", statPending);
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Per Institusi");
        System.out.println("──────────────────────────────────────────────────");
        if (statPerInstitusi.isEmpty()) {
            System.out.println("  (belum ada laporan yang didistribusi)");
        } else {
            for (Map.Entry<String, Integer> e : statPerInstitusi.entrySet()) {
                System.out.printf("  %-35s : %d laporan%n", e.getKey(), e.getValue());
            }
        }
        System.out.println();
        if (statTopUpvote != null) {
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  Upvote Tertinggi");
            System.out.println("──────────────────────────────────────────────────");
            System.out.printf("  %s | %s | %d votes%n",
                    statTopUpvote.getId(), statTopUpvote.getTitle(), statTopUpvote.getUpvotes());
        }
        System.out.println();
        System.out.printf("  Trending Issue     : %s (%d laporan)%n", trending, maxKat);
        System.out.println("──────────────────────────────────────────────────");
    }

    static void lihatAntreanDualQueue() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DUAL-QUEUE SYSTEM VIEW");
        System.out.println("──────────────────────────────────────────────────");

        System.out.println();
        System.out.println("  ANTREAN PRIORITAS");
        System.out.println("──────────────────────────────────────────────────");
        if (institutionMap.isEmpty()) {
            System.out.println("  (tidak ada institusi)");
        } else {
            for (Institution inst : institutionMap.values()) {
                System.out.println("  " + inst.getNamaInstitution());

                List<Aspiration> sorted = new ArrayList<>(inst.getQueue());
                Collections.sort(sorted);

                if (sorted.isEmpty()) {
                    System.out.println("    [Antrean] (kosong)");
                } else {
                    int rank = 1;
                    for (Aspiration asp : sorted) {
                        String judul = truncate(asp.getTitle(), 18);
                        System.out.println("    [Antrean] #" + rank
                                + "  " + asp.getId()
                                + " | " + judul
                                + " | Skor: " + String.format("%.2f", asp.getTotalScore())
                                + " | " + asp.getPriority().getLabel());
                        rank++;
                    }
                }

                LinkedList<Aspiration> onProgress =             // #7 - O(1) lookup
                        institutionOnProgressMap.get(inst.getNamaInstitution());

                if (onProgress == null || onProgress.isEmpty()) {
                    System.out.println("    [Diproses] (tidak ada)");
                } else {
                    for (Aspiration asp : onProgress) {
                        String judul = truncate(asp.getTitle(), 18);
                        System.out.println("    [Diproses] " + asp.getId()
                                + " | " + judul
                                + " | Skor: " + String.format("%.2f", asp.getTotalScore())
                                + " | " + asp.getPriority().getLabel());
                    }
                }

                System.out.println();
            }
        }
    }
    // #endregion

    // #region DATA PERSISTENCE
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

        saveComments();
    }

    // Format per baris: aspirationId|username|comment|timestamp
    static void saveComments() {
        try {
            new File(DATA_DIR).mkdirs();
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(DATA_DIR + "/comments.txt"), "UTF-8"))) {
                for (Aspiration asp : aspirationMap.values()) {
                    for (CommentAspiration c : asp.getComments()) {
                        pw.println(escape(asp.getId())        + "|"
                                 + escape(c.getUsername())    + "|"
                                 + escape(c.getComment())     + "|"
                                 + escape(c.getTimestamp()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  [WARN] Gagal menyimpan komentar: " + e.getMessage());
        }
    }

    static void loadData() {
        File counterFile = new File(DATA_DIR + "/counter.txt");
        if (!counterFile.exists()) {
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

            loadComments();

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
            saveData();
        }
    }

    static void loadComments() {
        File f = new File(DATA_DIR + "/comments.txt");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 4) continue;
                String aspId     = unescape(p[0]);
                String username  = unescape(p[1]);
                String comment   = unescape(p[2]);
                String timestamp = unescape(p[3]);
                Aspiration asp = aspirationMap.get(aspId);
                if (asp != null) {
                    asp.addComment(new CommentAspiration(username, comment, timestamp));
                }
            }
        } catch (Exception e) {
            System.out.println("  [WARN] Gagal memuat komentar: " + e.getMessage());
        }
    }

    // #endregion

    // #region INVERTED INDEX
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

    // #endregion

    // #region UTILITIES
    static String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max) + ".." : s;
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
    // #endregion
}

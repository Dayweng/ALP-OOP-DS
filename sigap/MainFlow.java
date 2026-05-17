package sigap;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import sigap.enums.Priority;
import sigap.model.*;


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
        System.out.println("  Pemerintah Kabupaten Sidoarjo");
        System.out.println("  Versi 1.0 - Phase 1 (Register, Login, Tambah Aspirasi)");
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
        asp1.setPriority(Priority.HIGH);

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
                case 3:
                case 4:
                    
                    System.out.println();
                    System.out.println("    Fitur ini akan tersedia di Phase 2.");
                    System.out.println("     Silakan tunggu update berikutnya.");
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
        System.out.println("║         TAMBAH ASPIRASI BARU             ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Sampaikan aspirasi Anda kepada          ║");
        System.out.println("║  pemerintah daerah Sidoarjo.             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

     
        System.out.print("  Judul Aspirasi  : ");
        String title = sc.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("   Judul tidak boleh kosong. Dibatalkan.");
            return;
        }

        
        System.out.print("  Deskripsi       : ");
        String description = sc.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("   Deskripsi tidak boleh kosong. Dibatalkan.");
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

        System.out.println("  Kategori dipilih: " + category);

        System.out.print("  Lokasi          : ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("   Lokasi tidak boleh kosong. Dibatalkan.");
            return;
        }

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
        System.out.println("  Aspirasi Anda akan segera diverifikasi oleh Admin.");
        System.out.println("  Simpan ID Aspirasi Anda: " + newId);
    }


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
                    System.out.println("    Fitur ini akan tersedia di Phase 2.");
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
            System.out.println("   Input harus berupa angka!");
            return -1;
        }
    }
}

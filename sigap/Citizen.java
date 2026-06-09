package sigap;

import java.util.HashMap;
import java.util.LinkedList;

public class Citizen extends User {
    HashMap<String, Aspiration> aspirasiCache;

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
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DASHBOARD WARGA NEGARA");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Halo, " + getNama() + "!");
        System.out.println("  Role    : Warga / Citizen");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  [1] Tambah Aspirasi Baru");
        System.out.println("  [2] Lihat Aspirasi");
        System.out.println("  [3] Upvote Aspirasi");
        System.out.println("  [4] Cari Aspirasi");
        System.out.println("  [5] Beri Komentar");
        System.out.println("  [0] Logout");
        System.out.println("──────────────────────────────────────────────────");
    }
    public Aspiration buatAspirasi(String id, String title,
                                   String description, String category,
                                   String location) {

        return new Aspiration(id, title, description, category, location, this.getUsername());
    }

    public void lihatAspirasi() {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  DAFTAR ASPIRASI PUBLIK");
        System.out.println("──────────────────────────────────────────────────");

        if (aspirasiCache == null || aspirasiCache.isEmpty()) {
            System.out.println("  Belum ada aspirasi yang tersedia.");
            System.out.println("──────────────────────────────────────────────────");
            return;
        }

        LinkedList<Aspiration> mine  = new LinkedList<>();
        LinkedList<Aspiration> other = new LinkedList<>();
        for (Aspiration asp : aspirasiCache.values()) {
            if (asp.getAuthor().equals(this.getUsername())) mine.add(asp);
            else                                            other.add(asp);
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
}

package sigap.model;

import sigap.enums.Priority;
import sigap.enums.Status;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * ============================================================
 * Class: Aspiration
 * ============================================================
 * Merepresentasikan satu aspirasi / laporan dari masyarakat.
 *
 * Implements Comparable<Aspiration> agar:
 *  → PriorityQueue bisa mengurutkan aspirasi secara otomatis
 *  → Aspirasi ber-prioritas HIGH diproses lebih dulu
 *
 * Struktur data yang digunakan:
 *  - LinkedList<String>           : daftar username yang sudah upvote
 *  - LinkedList<CommentAspiration>: daftar komentar aspirasi
 * ============================================================
 */
public class Aspiration implements Comparable<Aspiration> {

    // ======================================================
    // ATRIBUT
    // ======================================================
    private String   id;                // ID unik, contoh: ASP001
    private String   title;             // judul aspirasi
    private String   description;       // deskripsi lengkap
    private String   category;          // kategori: Infrastruktur, Kesehatan, dll.
    private String   location;          // lokasi terkait aspirasi
    private Status   status;            // status saat ini (Enum Status)
    private Priority priority;          // tingkat prioritas (Enum Priority)
    private int      upvotes;           // jumlah upvote yang diterima
    private String   author;            // username Citizen pembuat
    private String   institutionTarget; // nama institusi tujuan distribusi
    private String   createdAt;         // waktu aspirasi dibuat

    /**
     * Daftar username yang sudah memberikan upvote.
     * Menggunakan LinkedList agar mudah di-iterate dan dicek.
     * Satu user hanya boleh upvote SATU kali per aspirasi.
     */
    private LinkedList<String> upvotedUsers;

    /**
     * Daftar komentar pada aspirasi ini.
     * Digunakan di fitur komentar (Phase 2/3).
     */
    private LinkedList<CommentAspiration> comments;

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Membuat aspirasi baru.
     * Status awal selalu PENDING, prioritas awal LOW.
     *
     * @param id          ID unik aspirasi (generate dari MainFlow)
     * @param title       judul aspirasi
     * @param description deskripsi aspirasi
     * @param category    kategori aspirasi
     * @param location    lokasi aspirasi
     * @param author      username Citizen pembuat
     */
    public Aspiration(String id, String title, String description,
                      String category, String location, String author) {
        this.id                = id;
        this.title             = title;
        this.description       = description;
        this.category          = category;
        this.location          = location;
        this.author            = author;

        // Nilai default saat aspirasi pertama kali dibuat
        this.status            = Status.PENDING;  // selalu mulai dari PENDING
        this.priority          = Priority.LOW;    // default LOW, admin bisa ubah
        this.upvotes           = 0;
        this.institutionTarget = "-";             // belum didistribusikan

        // Inisialisasi list kosong
        this.upvotedUsers = new LinkedList<>();
        this.comments     = new LinkedList<>();

        // Timestamp otomatis saat dibuat
        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    }

    // ======================================================
    // COMPARABLE — untuk PriorityQueue
    // ======================================================
    /**
     * Menentukan urutan aspirasi di dalam PriorityQueue.
     *
     * Java PriorityQueue adalah Min-Heap (nilai terkecil keluar duluan).
     * Karena kita ingin HIGH (ordinal=2) keluar duluan, kita BALIK
     * perbandingannya:
     *   return other.ordinal - this.ordinal
     *
     * Contoh:
     *   this  = HIGH (2), other = LOW  (0) → compare(0, 2) = -1 → this lebih kecil → this duluan ✓
     *   this  = LOW  (0), other = HIGH (2) → compare(2, 0) = +1 → other lebih kecil → other duluan ✓
     */
    @Override
    public int compareTo(Aspiration other) {
        return Integer.compare(other.priority.ordinal(), this.priority.ordinal());
    }

    // ======================================================
    // METHOD UPVOTE
    // ======================================================
    /**
     * Tambahkan upvote dari seorang user.
     * Satu user hanya boleh upvote SEKALI per aspirasi.
     *
     * @param username username yang memberi upvote
     * @return true jika upvote berhasil, false jika sudah pernah upvote
     */
    public boolean addUpvote(String username) {
        // Cek apakah username sudah ada di list upvotedUsers
        if (upvotedUsers.contains(username)) {
            return false; // sudah pernah upvote → tolak
        }
        upvotedUsers.add(username); // tambah ke daftar
        upvotes++;                  // tambah counter
        return true;
    }

    // ======================================================
    // METHOD TAMPIL DETAIL
    // ======================================================
    /**
     * Tampilkan informasi lengkap aspirasi di terminal.
     * Dipanggil saat user ingin melihat detail satu aspirasi.
     */
    public void displayDetail() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║              DETAIL ASPIRASI                 ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("  ID Aspirasi  : " + id);
        System.out.println("  Judul        : " + title);
        System.out.println("  Kategori     : " + category);
        System.out.println("  Lokasi       : " + location);
        System.out.println("  Status       : " + status.getLabel());
        System.out.println("  Prioritas    : " + priority.getLabel());
        System.out.println("  Upvotes      : " + upvotes + " suara");
        System.out.println("  Penulis      : " + author);
        System.out.println("  Institusi    : " + institutionTarget);
        System.out.println("  Dibuat       : " + createdAt);
        System.out.println("────────────────────────────────────────────────");
        System.out.println("  Deskripsi    :");
        System.out.println("  " + description);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    /**
     * Tampilkan ringkasan aspirasi dalam satu baris.
     * Berguna untuk menampilkan daftar banyak aspirasi.
     */
    public void displaySummary() {
        System.out.printf("  %-8s | %-30s | %-15s | %-8s | %d votes%n",
                id,
                (title.length() > 28 ? title.substring(0, 28) + ".." : title),
                category,
                status.getLabel(),
                upvotes);
    }

    // ======================================================
    // GETTER & SETTER
    // ======================================================
    public String   getId()                { return id; }
    public String   getTitle()             { return title; }
    public String   getDescription()       { return description; }
    public String   getCategory()          { return category; }
    public String   getLocation()          { return location; }
    public Status   getStatus()            { return status; }
    public Priority getPriority()          { return priority; }
    public int      getUpvotes()           { return upvotes; }
    public String   getAuthor()            { return author; }
    public String   getInstitutionTarget() { return institutionTarget; }
    public String   getCreatedAt()         { return createdAt; }

    public LinkedList<String>             getUpvotedUsers() { return upvotedUsers; }
    public LinkedList<CommentAspiration>  getComments()     { return comments; }

    // Setter untuk digunakan Admin (Phase 2 & 3)
    public void setStatus(Status status)                   { this.status   = status; }
    public void setPriority(Priority priority)             { this.priority = priority; }
    public void setInstitutionTarget(String instTarget)   { this.institutionTarget = instTarget; }
}

package sigap.model;

/**
 * ============================================================
 * Abstract Class: User
 * ============================================================
 * Kelas dasar (parent) untuk semua jenis pengguna SIGAP.
 * Karena bersifat abstract, kelas ini TIDAK bisa di-instansiasi
 * langsung → harus melalui subclass (Citizen, Admin, dll.).
 *
 * Konsep OOP yang diterapkan:
 *  - Abstract Class    : User tidak bisa dibuat langsung
 *  - Encapsulation     : atribut private, diakses via getter/setter
 *  - Inheritance       : Citizen, Admin, InstitutionAdmin extends User
 *  - Polymorphism      : showDashboard() berbeda di tiap subclass
 * ============================================================
 */
public abstract class User {

    // ======================================================
    // ATRIBUT (private = encapsulation)
    // ======================================================
    private String username;   // username unik untuk login
    private String password;   // password pengguna
    private String nama;       // nama lengkap pengguna

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Constructor utama User.
     * Dipanggil dari subclass menggunakan super(...)
     *
     * @param username username unik
     * @param password password
     * @param nama     nama lengkap
     */
    public User(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama     = nama;
    }

    // ======================================================
    // ABSTRACT METHOD
    // Wajib diimplementasikan oleh setiap subclass.
    // ======================================================

    /**
     * Tampilkan dashboard sesuai peran masing-masing user.
     * Setiap subclass punya tampilan dashboard berbeda (Polymorphism).
     */
    public abstract void showDashboard();

    /**
     * Kembalikan peran/role user sebagai String.
     * Contoh: "CITIZEN", "ADMIN", "INSTITUTION_ADMIN"
     */
    public abstract String getRole();

    // ======================================================
    // CONCRETE METHOD (sudah ada implementasinya di sini)
    // ======================================================

    /**
     * Memvalidasi apakah password yang dimasukkan cocok.
     * Digunakan saat proses login.
     *
     * @param inputPassword password yang diketik user
     * @return true jika cocok, false jika salah
     */
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    /**
     * Menampilkan pesan perpisahan dan "keluar" dari sesi.
     * Dipanggil ketika user memilih opsi Logout.
     */
    public void logout() {
        System.out.println("\n  Sampai jumpa, " + nama + "! Terima kasih telah menggunakan SIGAP.");
    }

    // ======================================================
    // GETTER & SETTER (Encapsulation)
    // ======================================================
    public String getUsername() { return username; }
    public String getNama()     { return nama; }
    public String getPassword() { return password; }   // dipakai internal saja

    public void setNama(String nama)         { this.nama     = nama; }
    public void setPassword(String password) { this.password = password; }

    /**
     * Representasi teks dari objek User.
     * Berguna untuk debugging.
     */
    @Override
    public String toString() {
        return "[" + getRole() + "] " + nama + " (" + username + ")";
    }
}

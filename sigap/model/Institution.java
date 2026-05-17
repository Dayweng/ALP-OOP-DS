package sigap.model;

import java.util.PriorityQueue;

/**
 * ============================================================
 * Class: Institution
 * ============================================================
 * Merepresentasikan sebuah institusi pemerintah (bukan User).
 * Setiap institusi memiliki antrian aspirasi berbasis prioritas.
 *
 * Penggunaan PriorityQueue:
 *  - Aspirasi dengan Priority.HIGH keluar lebih dulu
 *  - Menggunakan Comparable yang sudah diimplementasi di Aspiration
 *  - PriorityQueue Java = Min-Heap, tapi karena compareTo dibalik,
 *    hasilnya adalah Max-Priority (HIGH duluan)
 * ============================================================
 */
public class Institution {

    // Nama institusi, contoh: "Dinas Pekerjaan Umum"
    private String namaInstitution;

    /**
     * Antrian aspirasi berbasis prioritas.
     * Aspiration dengan Priority HIGH akan di-poll() lebih dulu.
     * Menggunakan Comparable<Aspiration> yang sudah didefinisikan.
     */
    private PriorityQueue<Aspiration> queue;

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    public Institution(String namaInstitution) {
        this.namaInstitution = namaInstitution;
        this.queue = new PriorityQueue<>();  // Aspiration harus implements Comparable
    }

    // ======================================================
    // OPERASI ANTRIAN
    // ======================================================

    /**
     * Tambahkan aspirasi ke antrian institusi ini.
     * Aspirasi otomatis diurutkan berdasarkan prioritas.
     *
     * @param aspiration aspirasi yang akan dimasukkan ke antrian
     */
    public void addAspiration(Aspiration aspiration) {
        queue.add(aspiration);
    }

    /**
     * Ambil dan hapus aspirasi dengan prioritas TERTINGGI dari antrian.
     * Mengembalikan null jika antrian kosong.
     *
     * @return Aspiration dengan prioritas tertinggi, atau null
     */
    public Aspiration prosesAspirasi() {
        return queue.poll();
    }

    /**
     * Lihat aspirasi prioritas tertinggi tanpa menghapusnya.
     *
     * @return Aspiration berikutnya, atau null
     */
    public Aspiration lihatAntriTeratas() {
        return queue.peek();
    }

    /**
     * Kembalikan jumlah aspirasi yang menunggu di antrian.
     */
    public int getJumlahAntrian() {
        return queue.size();
    }

    // ======================================================
    // GETTER & SETTER
    // ======================================================
    public String getNamaInstitution() { return namaInstitution; }
    public void setNamaInstitution(String namaInstitution) {
        this.namaInstitution = namaInstitution;
    }
    public PriorityQueue<Aspiration> getQueue() { return queue; }
}

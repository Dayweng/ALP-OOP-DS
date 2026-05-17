package sigap.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================
 * Class: CommentAspiration
 * ============================================================
 * Merepresentasikan satu komentar yang diberikan pada sebuah
 * aspirasi. Komentar menyimpan username pemberi komentar,
 * isi komentar, dan waktu dibuat.
 *
 * Digunakan oleh Aspiration dalam LinkedList<CommentAspiration>.
 * ============================================================
 */
public class CommentAspiration {

    private String username;   // siapa yang berkomentar
    private String comment;    // isi komentar
    private String timestamp;  // waktu komentar dibuat

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    /**
     * Membuat komentar baru. Timestamp diisi otomatis.
     *
     * @param username username pemberi komentar
     * @param comment  isi komentar
     */
    public CommentAspiration(String username, String comment) {
        this.username  = username;
        this.comment   = comment;
        // Format waktu otomatis saat objek dibuat
        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    // ======================================================
    // GETTER
    // ======================================================
    public String getUsername()  { return username; }
    public String getComment()   { return comment; }
    public String getTimestamp() { return timestamp; }

    /**
     * Format tampilan komentar di terminal.
     * Contoh: [25-05-2025 14:30:00] budi: Setuju sekali!
     */
    @Override
    public String toString() {
        return "[" + timestamp + "] " + username + ": " + comment;
    }
}

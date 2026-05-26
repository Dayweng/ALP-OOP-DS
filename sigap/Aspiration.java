package sigap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import sigap.enums.Priority;
import sigap.enums.Status;


public class Aspiration implements Comparable<Aspiration> {

    private String   id;
    private String   title;
    private String   description;
    private String   category;
    private String   location;
    private Status   status;
    private int      upvotes;
    private String   author;
    private String   institutionTarget;
    private String   createdAt;
    private LinkedList<String> upvotedUsers;
    private LinkedList<CommentAspiration> comments;

    private double  sAuth;
    private double  sSafe;
    private double  totalScore;
    private boolean scoreLocked;
    private boolean distributed;
    private String  closingStatement;


    public Aspiration(String id, String title, String description,
                      String category, String location, String author) {
        this.id                = id;
        this.title             = title;
        this.description       = description;
        this.category          = category;
        this.location          = location;
        this.author            = author;

        this.status            = Status.PENDING;
        this.upvotes           = 0;
        this.institutionTarget = "-";

        this.upvotedUsers = new LinkedList<>();
        this.comments     = new LinkedList<>();

        this.sAuth            = 0;
        this.sSafe            = 0;
        this.totalScore       = 0;
        this.scoreLocked      = false;
        this.distributed      = false;
        this.closingStatement = "-";

        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    }

    @Override
    public int compareTo(Aspiration other) {
        return Double.compare(other.totalScore, this.totalScore);
    }

    public double calcSScale() {
        return Math.min(10.0, 1.0 + upvotes / 5.0);
    }

    public double calcSCat() {
        switch (category) {
            case "Kesehatan":     return 10.0;
            case "Infrastruktur": return 8.0;
            case "Lingkungan":    return 6.0;
            case "Pendidikan":    return 5.0;
            default:              return 2.0;
        }
    }

    public double calcSSys() {
        try {
            Date created = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(createdAt);
            long days = (new Date().getTime() - created.getTime()) / (1000L * 60 * 60 * 24);
            return Math.min(10.0, 1.0 + days / 9.0);
        } catch (ParseException e) {
            return 1.0;
        }
    }

    public double previewScore(double sAuth, double sSafe) {
        return (sAuth          * 0.30)
             + (sSafe          * 0.25)
             + (calcSScale()   * 0.15)
             + (calcSCat()     * 0.15)
             + (calcSSys()     * 0.15);
    }

    public boolean lockScore(double sAuth, double sSafe) {
        if (scoreLocked) return false;
        this.sAuth       = sAuth;
        this.sSafe       = sSafe;
        this.totalScore  = previewScore(sAuth, sSafe);
        this.scoreLocked = true;
        return true;
    }

    public boolean addUpvote(String username) {
        if (upvotedUsers.contains(username)) return false;
        upvotedUsers.add(username);
        upvotes++;
        return true;
    }

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
        System.out.println("  Prioritas    : " + getPriority().getLabel()
                + (scoreLocked
                        ? String.format("  (Skor: %.2f / 10.0)", totalScore)
                        : "  (Belum dinilai)"));
        System.out.println("  Upvotes      : " + upvotes + " suara");
        System.out.println("  Penulis      : " + author);
        System.out.println("  Institusi    : " + institutionTarget);
        System.out.println("  Dibuat       : " + createdAt);
        System.out.println("────────────────────────────────────────────────");
        System.out.println("  Deskripsi    :");
        System.out.println("  " + description);
        if (status == Status.DONE && !closingStatement.equals("-")) {
            System.out.println("────────────────────────────────────────────────");
            System.out.println("  Closing Statement:");
            System.out.println("  " + closingStatement);
        }
        if (scoreLocked) {
            System.out.println("────────────────────────────────────────────────");
            System.out.println("  Rincian Skor  [TERKUNCI — tidak dapat diubah]");
            System.out.printf("  S_auth  (30%%) : %.1f   [Otoritas Admin]%n",            sAuth);
            System.out.printf("  S_safe  (25%%) : %.1f   [Keselamatan / Kesehatan]%n",   sSafe);
            System.out.printf("  S_scale (15%%) : %.1f   [Skala Dampak — %d upvotes]%n", calcSScale(), upvotes);
            System.out.printf("  S_cat   (15%%) : %.1f   [Bobot Kategori — %s]%n",       calcSCat(), category);
            System.out.printf("  S_sys   (15%%) : %.1f   [Aging — waktu tunggu]%n",      calcSSys());
            System.out.println("  ──────────────────────────────────────────────");
            System.out.printf("  Total Score   : %.2f / 10.0%n", totalScore);
        }
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    public void displaySummary() {
        System.out.printf("  %-8s | %-30s | %-15s | %-8s | %d votes%n",
                id,
                (title.length() > 28 ? title.substring(0, 28) + ".." : title),
                category,
                status.getLabel(),
                upvotes);
    }

    public String   getId()                { return id; }
    public String   getTitle()             { return title; }
    public String   getDescription()       { return description; }
    public String   getCategory()          { return category; }
    public String   getLocation()          { return location; }
    public Status   getStatus()            { return status; }
    public int      getUpvotes()           { return upvotes; }
    public String   getAuthor()            { return author; }
    public String   getInstitutionTarget() { return institutionTarget; }
    public String   getCreatedAt()         { return createdAt; }
    public double   getTotalScore()        { return totalScore; }
    public double   getSAuth()             { return sAuth; }
    public double   getSSafe()             { return sSafe; }
    public boolean  isScoreLocked()        { return scoreLocked; }

    public LinkedList<String>            getUpvotedUsers() { return upvotedUsers; }
    public LinkedList<CommentAspiration> getComments()     { return comments; }

    public Priority getPriority() {
        if (!scoreLocked) return Priority.LOW;
        if (totalScore >= 7.0) return Priority.HIGH;
        if (totalScore >= 4.0) return Priority.MEDIUM;
        return Priority.LOW;
    }

    public boolean isDistributed()                           { return distributed; }
    public String  getClosingStatement()                     { return closingStatement; }
    public void    setStatus(Status status)                  { this.status            = status; }
    public void    setInstitutionTarget(String instTarget)   { this.institutionTarget = instTarget; }
    public void    setDistributed(boolean distributed)       { this.distributed       = distributed; }
    public void    setClosingStatement(String stmt)          { this.closingStatement  = stmt; }
}

package sigap.model;

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
    private Priority priority;          
    private int      upvotes;          
    private String   author;            
    private String   institutionTarget; 
    private String   createdAt;         
    private LinkedList<String> upvotedUsers;
    private LinkedList<CommentAspiration> comments;


    public Aspiration(String id, String title, String description,
                      String category, String location, String author) {
        this.id                = id;
        this.title             = title;
        this.description       = description;
        this.category          = category;
        this.location          = location;
        this.author            = author;

        this.status            = Status.PENDING;  
        this.priority          = Priority.LOW;    
        this.upvotes           = 0;
        this.institutionTarget = "-";            

       
        this.upvotedUsers = new LinkedList<>();
        this.comments     = new LinkedList<>();

        
        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    }

    @Override
    public int compareTo(Aspiration other) {
        return Integer.compare(other.priority.ordinal(), this.priority.ordinal());
    }

    
    public boolean addUpvote(String username) {
        
        if (upvotedUsers.contains(username)) {
            return false; 
        }
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
    public Priority getPriority()          { return priority; }
    public int      getUpvotes()           { return upvotes; }
    public String   getAuthor()            { return author; }
    public String   getInstitutionTarget() { return institutionTarget; }
    public String   getCreatedAt()         { return createdAt; }

    public LinkedList<String>             getUpvotedUsers() { return upvotedUsers; }
    public LinkedList<CommentAspiration>  getComments()     { return comments; }

   
    public void setStatus(Status status)                   { this.status   = status; }
    public void setPriority(Priority priority)             { this.priority = priority; }
    public void setInstitutionTarget(String instTarget)   { this.institutionTarget = instTarget; }
}

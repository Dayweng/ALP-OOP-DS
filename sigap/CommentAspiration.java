package sigap;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CommentAspiration {

    private String username;   
    private String comment;    
    private String timestamp; 


    // Used when creating a new comment at runtime
    public CommentAspiration(String username, String comment) {
        this.username  = username;
        this.comment   = comment;
        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    // Used when restoring a comment from file (timestamp already stored)
    public CommentAspiration(String username, String comment, String timestamp) {
        this.username  = username;
        this.comment   = comment;
        this.timestamp = timestamp;
    }

    public String getUsername()  { return username; }
    public String getComment()   { return comment; }
    public String getTimestamp() { return timestamp; }

   
    @Override
    public String toString() {
        return "[" + timestamp + "] " + username + ": " + comment;
    }
}

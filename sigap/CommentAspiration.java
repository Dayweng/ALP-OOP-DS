package sigap;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CommentAspiration {

    private String username;   
    private String comment;    
    private String timestamp; 


    public CommentAspiration(String username, String comment) {
        this.username  = username;
        this.comment   = comment;
        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public String getUsername()  { return username; }
    public String getComment()   { return comment; }
    public String getTimestamp() { return timestamp; }

   
    @Override
    public String toString() {
        return "[" + timestamp + "] " + username + ": " + comment;
    }
}

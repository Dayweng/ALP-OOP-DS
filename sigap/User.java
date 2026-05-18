package sigap;


public abstract class User {

    
    private String username;  
    private String password;  
    private String nama;      


    public User(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama     = nama;
    }

    
    public abstract void showDashboard();
    public abstract String getRole();

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void logout() {
        System.out.println("\n  Sampai jumpa, " + nama + "! Terima kasih telah menggunakan SIGAP.");
    }

    public String getUsername() { return username; }
    public String getNama()     { return nama; }
    public String getPassword() { return password; }  

    public void setNama(String nama)         { this.nama     = nama; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "[" + getRole() + "] " + nama + " (" + username + ")";
    }
}

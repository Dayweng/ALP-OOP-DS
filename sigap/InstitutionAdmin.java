package sigap;
public class InstitutionAdmin extends User {
    private String institutionName;
    public InstitutionAdmin(String username, String password,
                             String nama, String institutionName) {
        super(username, password, nama);
        this.institutionName = institutionName;
    }
    @Override
    public String getRole() {
        return "INSTITUTION_ADMIN";
    }
    @Override
    public void showDashboard() {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("  DASHBOARD ADMIN INSTITUSI");
        System.out.println("--------------------------------------------------");
        System.out.println("  Halo, " + getNama() + "!");
        System.out.println("  Institusi : " + institutionName);
        System.out.println("--------------------------------------------------");
        System.out.println("  [1] Proses Laporan");
        System.out.println("  [2] Update Status Laporan");
        System.out.println("  [3] Registrasi Admin Institusi");
        System.out.println("  [0] Logout");
        System.out.println("--------------------------------------------------");
    }
    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}

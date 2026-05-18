package sigap;

import java.util.PriorityQueue;


public class Institution {

    private String namaInstitution;
    private PriorityQueue<Aspiration> queue;
    public Institution(String namaInstitution) {
        this.namaInstitution = namaInstitution;
        this.queue = new PriorityQueue<>();  
    }

    public void addAspiration(Aspiration aspiration) {
        queue.add(aspiration);
    }
    public Aspiration prosesAspirasi() {
        return queue.poll();
    }
    public Aspiration lihatAntriTeratas() {
        return queue.peek();
    }

 
    public int getJumlahAntrian() {
        return queue.size();
    }
    public String getNamaInstitution() { return namaInstitution; }
    public void setNamaInstitution(String namaInstitution) {
        this.namaInstitution = namaInstitution;
    }
    public PriorityQueue<Aspiration> getQueue() { return queue; }
}

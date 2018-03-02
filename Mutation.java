public class Mutation {
    int chrom = 0;
    int pos = 0;
    String ref;
    String alt;

    public Mutation(int chrom , int pos, String ref, String alt){
        this.chrom = chrom;
        this.pos = pos;
        this.ref = ref;
        this.alt = alt;
    }
}

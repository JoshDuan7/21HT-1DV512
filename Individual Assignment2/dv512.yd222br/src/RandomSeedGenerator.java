import java.util.Random;

public class RandomSeedGenerator {
  public static void main(String[] args) {
    Random rand=new Random();
    String seedNum="";
    for(int i=0;i<8;i++){
      seedNum+=rand.nextInt(10);
    }
    System.out.println(""+seedNum);
  }
}

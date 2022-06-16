/*
 * Group 10
 * Fabian Dacic (fd222fr)
 * Yuyao Duan (yd222br)
 * Fredric Eriksson Sepúlveda (fe222pa)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class OpenPipe {

  public static void main(String args[]) throws IOException, InterruptedException {

    while(true) {
      ZoneId z = ZoneId.of("Europe/Paris");

      System.out.println("<PID " + ProcessHandle.current().pid() + ">" + "<" + ZonedDateTime.now(z).getHour() + ":" +
              ZonedDateTime.now(z).getMinute() + ":" + ZonedDateTime.now(z).getSecond()
              + "> Process started");
      try {
        File testNamedPipe = new File ( "test-named-pipe" );
        RandomAccessFile pipe = new RandomAccessFile(testNamedPipe, "r");
        System.out.println("<PID " + ProcessHandle.current().pid() + ">" + "<" + ZonedDateTime.now(z).getHour() + ":" +
                ZonedDateTime.now(z).getMinute() + ":" + ZonedDateTime.now(z).getSecond()
                + "> Pipe opened");

        String line = "";

        while(true) {
          line = pipe.readLine();

          if( line != null ) {
            System.out.println("<PID " + ProcessHandle.current().pid() + ">" + "<" + ZonedDateTime.now(z).getHour() +
                    ":" + ZonedDateTime.now(z).getMinute() + ":" + ZonedDateTime.now(z).getSecond()
                    + "> This is text read from the pipe: ");
            System.out.println(line);
          } else {
            pipe.close();
            System.out.println("<PID " + ProcessHandle.current().pid() + ">" + "<" + ZonedDateTime.now(z).getHour()
                    + ":" + ZonedDateTime.now(z).getMinute() + ":" + ZonedDateTime.now(z).getSecond()
                    + "> Pipe closed");
            Thread.sleep(3000);
            break;
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
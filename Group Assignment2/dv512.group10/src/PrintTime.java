/*
 * Group 10
 * Fabian Dacic (fd222fr)
 * Yuyao Duan (yd222br)
 * Fredric Eriksson Sepúlveda (fe222pa)
 */

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PrintTime {

  private void createFolder() {
    try {
      String strDir = "test-directory";
      boolean success = (new File(strDir)).mkdir();

      if (success) {
        System.out.printf("Directory: %s created", strDir);
      }
    } catch (Exception e) {
      System.err.println("Error has occurred: " + e.getMessage());
    }
  }

  private File createFile(String timeString) throws IOException {
    File timeFile = new File(String.format(String.format("test-directory/%s", timeString)));

    timeFile.getParentFile().mkdirs();
    timeFile.createNewFile();

    return timeFile;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    FileWriter fr = null;
    PrintTime pt = new PrintTime();
    pt.createFolder();

    for (int i = 0; i < 500; i++) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss-SS");
      LocalTime time = LocalTime.now();
      String timeString = time.format(formatter);
      File tf = pt.createFile(timeString);

      try {
        fr = new FileWriter(tf);

        for(int j = 0; j < 10000; j++) {
          fr.write(timeString + "\n");
        }

      } catch (IOException e) {
        e.printStackTrace();

      } finally {
        try {
          fr.flush();
          fr.close();
          Thread.sleep(10);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}


/*
 * Group 10
 * Fabian Dacic (fd222fr)
 * Yuyao Duan (yd222br)
 * Fredric Eriksson Sepúlveda (fe222pa)
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  Scanner sc = new Scanner(System.in);
  boolean exit = false;

  private void menu() {
    System.out.println("\nWELCOME TO JAVA + FREEBSD app\n");
    System.out.println("---------Please choose one of the options below---------");
    System.out.println("1. Invoke id");
    System.out.println("2. Switch to /etc/ and find . -name 'rc*'");
    System.out.println("3. Run hostname freebsd-vm-group10-upd");
    System.out.println("0. Exit");
  }

  private int getOption() {
    int userInput = -1;

    while (userInput < 0 || userInput > 3) {
      try {
        System.out.println("\nPlease choose the desired action!: ");
        userInput = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException nfe) {
        System.out.println("Something went wrong. An integer please!");
      }
    }
    return userInput;
  }

  private void performAction(int userInput) {
    switch (userInput) {
      case 0:
        exit = true;
        break;
      case 1:
        System.out.println("Invoking id...");
        userCommand("id");
        break;
      case 2:
        System.out.println("Switching to /etc/ and finding . -name 'rc*'...");
        secondPart();
        break;
      case 3:
        System.out.println("3. Running hostname freebsd-vm-group10-upd...");
        thirdPart();
        break;
      default:
        System.out.println("Error! Please follow the instructions.");
    }
  }

  private void userCommand(String command) {
    ProcessBuilder processBuilder = new ProcessBuilder().command(command);
    String result = "";

    try {
      Process proBuild = processBuilder.start();

      InputStreamReader inputStreamReader = new InputStreamReader(proBuild.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String output = "";

      while ((output = bufferedReader.readLine()) != null) {
        result = output;
      }

      int exitCode = proBuild.waitFor();
      System.out.println("The result is: " + result);
      System.out.println("The exit code is: " + exitCode);

      bufferedReader.close();
      proBuild.destroy();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void secondPart() {
    List<String> commands = new ArrayList<>();
    commands.add("find");
    commands.add(".");
    commands.add("-name");
    commands.add("rc*");
    List<String> resultOfRc = new ArrayList<>();
    System.out.println("Switch to /etc...");
    ProcessBuilder pb = new ProcessBuilder();
    pb.directory(new File("/etc/"));
    System.out.println("Running find . -name rc*");
    pb.command(commands);

    try {
      Process proBuild = pb.start();

      InputStreamReader inputStreamReader = new InputStreamReader(proBuild.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      BufferedReader error = new BufferedReader(new InputStreamReader(proBuild.getErrorStream()));

      String output = "";
      String errorOutput = "";

      while ((output = bufferedReader.readLine()) != null) {
        resultOfRc.add(output);
      }

      int exitCode = proBuild.waitFor();

      System.out.println("Result is: ");
      for (String s : resultOfRc) {
        System.out.println(s);
      }
      System.out.println("The exit code is: " + exitCode);

      if (exitCode == 1 ) {
        while ((errorOutput = error.readLine()) != null) {
          System.out.println("Error occurred: " + errorOutput);
        }
      }

      bufferedReader.close();
      proBuild.destroy();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }

  private void thirdPart() {
    List<String> commands = new ArrayList<>();
    commands.add("hostname");
    commands.add("freebsd-vm-group10-upd");
    ProcessBuilder pb = new ProcessBuilder();
    List<String> resultCommands = new ArrayList<>();
    pb.command(commands);
    try {
      Process proBuild = pb.start();
      InputStreamReader inputStreamReader = new InputStreamReader(proBuild.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      BufferedReader error = new BufferedReader(new InputStreamReader(proBuild.getErrorStream()));
      String output = "";
      String errorOutput = "";

      while ((output = bufferedReader.readLine()) != null) {
        resultCommands.add(output);
      }

      int exitCode = proBuild.waitFor();
      System.out.println("The exit code is: " + exitCode);

      if (exitCode == 0) {
        System.out.println("The result is: ");
        for (String s : resultCommands) {
          System.out.println(s);
        }
      } else {
        while ((errorOutput = error.readLine()) != null) {
          System.out.println("Error occurred: " + errorOutput);
        }
      }

      bufferedReader.close();
      proBuild.destroy();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    Main main = new Main();

    while (!main.exit) {
      main.menu();
      int userInput = main.getOption();
      main.performAction(userInput);
    }
  }
}

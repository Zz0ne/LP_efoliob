package Helpers;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class MyScanner {
    private final Scanner scanner;

    public MyScanner() {
        this.scanner = new Scanner(System.in);
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public int nextInt() {
        int value = -1;

        try {
            value = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            scanner.nextLine();
            return value;
        }
        return value;
    }

    public float nextFloat() {
        float value = -1;
        try {
            value = scanner.nextFloat();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            scanner.nextLine();
            return value;
        }
        return value;
    }

    public void close() {
        scanner.close();
    }
}


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
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public float nextFloat() {
        float value = scanner.nextFloat();
        scanner.nextLine();
        return value;
    }

    public void close() {
        scanner.close();
    }
}


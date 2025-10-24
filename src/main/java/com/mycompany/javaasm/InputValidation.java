package com.mycompany.javaasm;

import java.util.List;
import java.util.Scanner;

public class InputValidation {
    public static int getInt(Scanner sc, int min, int max) {
        int num;
        while (true) {
            try {
                num = Integer.parseInt(sc.nextLine());
                if (num < min || num > max) throw new NumberFormatException();
                return num;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number (" + min + "-" + max + "): ");
            }
        }
    }

    public static int getPortIndex(Scanner sc, List<String> ports, String prompt) {
        int idx;
        while (true) {
            System.out.print(prompt);
            try {
                idx = Integer.parseInt(sc.nextLine()) - 1;
                if (idx < 0 || idx >= ports.size()) throw new NumberFormatException();
                return idx;
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Try again.");
            }
        }
    }

    public static int getPortIndex(Scanner sc, List<String> ports, String prompt, int excludeIndex) {
        int idx;
        while (true) {
            idx = getPortIndex(sc, ports, prompt);
            if (idx == excludeIndex) {
                System.out.println("Please choose a different port.");
                continue;
            }
            return idx;
        }
    }
}

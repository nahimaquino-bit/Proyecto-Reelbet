package com.reelbet.validation;

public class CedulaValidator {

    public static boolean isValid(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        try {
            int provinceCode = Integer.parseInt(cedula.substring(0, 2));
            if (provinceCode < 1 || provinceCode > 24) {
                return false;
            }

            int thirdDigit = Integer.parseInt(cedula.substring(2, 3));
            if (thirdDigit >= 6) {
                return false;
            }

            int[] digits = new int[10];
            for (int i = 0; i < 10; i++) {
                digits[i] = Integer.parseInt(cedula.substring(i, i + 1));
            }

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                int value = digits[i];
                if (i % 2 == 0) {
                    value *= 2;
                    if (value > 9) value -= 9;
                }
                sum += value;
            }

            int nearestTen = ((sum + 9) / 10) * 10;
            int checkDigit = nearestTen - sum;
            if (checkDigit == 10) checkDigit = 0;

            return checkDigit == digits[9];
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

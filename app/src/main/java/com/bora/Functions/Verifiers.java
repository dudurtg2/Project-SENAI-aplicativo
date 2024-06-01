package com.bora.Functions;

public class Verifiers {
    public static boolean verifierCPF(String cpf) {

        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit == 10 || firstDigit == 11) {
            firstDigit = 0;
        }
        if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
            return false;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit == 10 || secondDigit == 11) {
            secondDigit = 0;
        }
        if (Character.getNumericValue(cpf.charAt(10)) != secondDigit) {
            return false;
        }
        return true;
    }
    public static boolean verifierRG(String rg) {
        rg = rg.replaceAll("[^\\d]", "");

        if (rg.length() < 6) {
            return false;
        }

        return true;
    }
}

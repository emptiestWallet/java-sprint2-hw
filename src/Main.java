import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReportEngine reportEngine = new ReportEngine();

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();

            if (command == 1) {
                System.out.println("Считывание всех месячных отчётов...");
                reportEngine.readMonthlyReports();
            } else if (command == 2) {
                System.out.println("Считывание годового отчёта...");
                reportEngine.readYearlyReport();
            } else if (command == 3) {
                System.out.println("Сверка отчётов...");
                reportEngine.verifyReports();
            } else if (command == 4) {
                System.out.println("Вывод информации обо всех месячных отчётах...");
                reportEngine.printAllMonthlyReports();
            } else if (command == 5) {
                System.out.println("Вывод информации о годовом отчёте...");
                reportEngine.printYearlyReport();
            } else if (command == 0) {
                int numberCombination = scanner.nextInt();
                if (numberCombination == 335566) {
                    System.out.println("Выход из программы...");
                    break;
                } else {
                    System.out.println("Введена неверная комбинация цифр для завершения работы приложения");
                }
            } else {
                System.out.println("Некорректный выбор. Попробуйте снова.");
                return;
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("Что Вы хотите сделать?\n" +
                "1. Считать все месячные отчёты.\n" +
                "2. Считать годовой отчёт.\n" +
                "3. Сверить отчёты.\n" +
                "4. Вывести информацию обо всех месячных отчётах.\n" +
                "5. Вывести информацию о годовом отчёте.\n" +
                "0. Выход.");
    }
}


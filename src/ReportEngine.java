import jdk.jfr.consumer.RecordedEvent;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportEngine {

    HashMap<String, MonthlyReport> monthlyReports = new HashMap<>();
    HashMap<String, YearlyReport> yearlyReports = new HashMap<>();
    public void readMonthlyReports() {
        for (int i = 1; i < 4; i++) {
            String fileName = "m.20210" + i + ".csv";
            List<String> lines = readAllLines(fileName);

            if (lines.isEmpty()) {
                System.out.println("Файл " + fileName + " пустой!\n" + "Возникла ошибка.");
                return;
            }

            ArrayList<Record> expenses = new ArrayList<>();
            ArrayList<Record> incomes = new ArrayList<>();

            for (int j = 1; j < lines.size(); j++) {
                String line = lines.get(j);
                String[] values = line.split(",");
                Record record = lineToRecordMonth(values);

                if (record.getExpense()) {
                    expenses.add(record);
                } else {
                    incomes.add(record);
                }
            }

            monthlyReports.put("0" + i, new MonthlyReport(expenses, incomes));
        }
    }

    public void readYearlyReport() {
        String fileName = "y.2021.csv";
        List<String> lines = readAllLinesYear(fileName);

        if (lines.isEmpty()) {
            System.out.println("Файл " + fileName + " пустой!\n" + "Возникла ошибка.");
            return;
        }

        YearlyReport yearlyReport = null;
        String currentMonth = null;
        ArrayList<Record> expenses = new ArrayList<>();
        ArrayList<Record> incomes = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] values = line.split(",");
            Record record = lineToRecordYear(values);

            String month = record.getName();
            if (!month.equals(currentMonth)) {
                if (yearlyReport != null) {
                    yearlyReports.put(currentMonth, yearlyReport);
                }
                currentMonth = month;
                expenses = new ArrayList<>();
                incomes = new ArrayList<>();
                yearlyReport = new YearlyReport(expenses, incomes);
            }

            if (record.getExpense()) {
                expenses.add(record);
            } else {
                incomes.add(record);
            }
        }

        if (yearlyReport != null) {
            yearlyReports.put(currentMonth, yearlyReport);
        }
    }

    private Record lineToRecordMonth(String[] values) {
        return new Record(values[0], Boolean.parseBoolean(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
    }

    private Record lineToRecordYear(String[] values) {
        return new Record(values[0], Integer.parseInt(values[1]), Boolean.parseBoolean(values[2]));
    }

    public List<String> readAllLines(String fileName) {
        String filePath = "resources/months/" + fileName;
        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл " + fileName + ". Возможно, файл отсутствует в нужной директории.");
            return new ArrayList<>();
        }
    }

    public List<String> readAllLinesYear(String fileName) {
        String filePath = "resources/years/" + fileName;
        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл " + fileName + ". Возможно, файл отсутствует в нужной директории.");
            return new ArrayList<>();
        }
    }

    public void printAllMonthlyReports() {
        if (monthlyReports.isEmpty()) {
            System.out.println("Список не считан. Перед выполнением операции считайте список.");
            return;
        }

        for (String month : monthlyReports.keySet()) {
            MonthlyReport report = monthlyReports.get(month);

            System.out.println("Месяц: " + month);

            ReportEngine.Record mostProfitableItem = null;
            int maxProfit = 0;
            for (ReportEngine.Record record : report.incomes) {
                int profit = record.price * record.quantity;
                if (profit > maxProfit) {
                    maxProfit = profit;
                    mostProfitableItem = record;
                }
            }

            if (mostProfitableItem != null) {
                System.out.println("Самый прибыльный товар - " + mostProfitableItem.name + ", его стоимость составляет " + maxProfit);
            }

            ReportEngine.Record largestExpense = null;
            int maxExpense = 0;
            for (ReportEngine.Record record : report.expenses) {
                int expense = record.quantity * record.price;
                if (expense > maxExpense) {
                    maxExpense = expense;
                    largestExpense = record;
                }
            }

            if (largestExpense != null) {
                System.out.println("Самая большая трата - " + largestExpense.name + ", его стоимость составляет " + maxExpense);
            }

            System.out.println();
        }
    }

    public void printYearlyReport() {
        if (yearlyReports.isEmpty()) {
            System.out.println("Список не считан. Перед выполнением операции считайте список.");
            return;
        }

        System.out.println("Год: 2021");

        for (YearlyReport report : yearlyReports.values()) {

            ArrayList<Record> expenses = report.expenses;
            ArrayList<Record> incomes = report.incomes;

            for (int i = 0; i < Math.min(expenses.size(), incomes.size()); i++) {
                Record incomeRecord = incomes.get(i);
                int incomeAmount = incomeRecord.price;

                Record expenseRecord = expenses.get(i);
                int expenseAmount = expenseRecord.price;

                int profit = incomeAmount - expenseAmount;

                System.out.println("Доход за месяц " + incomeRecord.name + " - " + profit);
            }
        }

        double averageExpense = calculateAverageAmount(yearlyReports, true);
        double averageIncome = calculateAverageAmount(yearlyReports, false);

        System.out.println("Средний расход за год: " + averageExpense);
        System.out.println("Средний доход за год: " + averageIncome);
    }

    private int calculateTotalAmount(ArrayList<Record> records, boolean isExpense) {
        int totalAmount = 0;
        for (Record record : records) {
            if (record.isExpense == isExpense) {
                totalAmount += record.price;
            }
        }
        return totalAmount;
    }

    private double calculateAverageAmount(HashMap<String, YearlyReport> yearlyReports, boolean isExpense) {
        int totalAmount = 0;
        int totalMonths = yearlyReports.size();

        for (YearlyReport report : yearlyReports.values()) {
            totalAmount += calculateTotalAmount(report.expenses, isExpense);
            totalAmount += calculateTotalAmount(report.incomes, isExpense);
        }

        return (double) totalAmount / (totalMonths * 2);
    }

    Integer calculateMonthlyExpenses(MonthlyReport monthlyReport) {
        int sum = 0;
        for (int i = 0; i < monthlyReport.expenses.size(); i++) {
            ReportEngine.Record record = monthlyReport.expenses.get(i);
            sum += record.price * record.quantity;
        }

        return sum;
    }

    Integer calculateMonthlyIncomes(MonthlyReport monthlyReport) {
        int sum = 0;
        for (int i = 0; i < monthlyReport.incomes.size(); i++) {
            ReportEngine.Record record = monthlyReport.incomes.get(i);
            sum += record.price * record.quantity;
        }

        return sum;
    }

    Integer calculateYearlyExpenses(YearlyReport yearlyReport, String monthNumber) {
        int sum = 0;
        for (int i = 0; i < yearlyReport.expenses.size(); i++) {
            ReportEngine.Record record = yearlyReport.expenses.get(i);

            if (record.name.equals(monthNumber)) {
                sum += record.getPrice();
            }
        }

        return sum;
    }

    Integer calculateYearlyIncomes(YearlyReport yearlyReport, String monthNumber) {
        int sum = 0;
        for (int i = 0; i < yearlyReport.incomes.size(); i++) {
            ReportEngine.Record record = yearlyReport.incomes.get(i);

            if (record.name.equals(monthNumber)) {
                sum += record.getPrice();
            }
        }

        return sum;
    }

    Boolean verifyReports() {
        if (yearlyReports.size() == 0 || monthlyReports.size() == 0) {
            System.out.println("Ошибка!");
            return false;
        }

        if (yearlyReports.size() != monthlyReports.size()) {
            System.out.println("Ошибка!");
            return false;
        }

        for (int i = 1; i < monthlyReports.size(); i++) {
            String key = "0" + i;
            MonthlyReport monthlyReport = monthlyReports.get(key);
            YearlyReport  yearlyReport  = yearlyReports.get(key);

            if (!calculateYearlyIncomes(yearlyReport, key).equals(calculateMonthlyIncomes(monthlyReport))) {
                System.out.println("Выявлено несоответствие данных по доходам в месяце №" + key);
                return false;
            }

            if (!calculateYearlyExpenses(yearlyReport, key).equals(calculateMonthlyExpenses(monthlyReport))) {
                System.out.println("Выявлено несоответствие данных по расходам в месяце №" + key);
                return false;
            }
        }

        System.out.println("Данные успешно сверены. Несоответствий не выявлено!");
        return true;
    }

    static class Record {
        String name;
        Integer price;
        Integer quantity;
        Boolean isExpense;


        public Record(String name, Integer price, Boolean isExpense) {
            this.name = name;
            this.price = price;
            this.isExpense = isExpense;
        }

        public Record(String name, Boolean isExpense, Integer quantity, Integer price) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.isExpense = isExpense;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public Boolean getExpense() {
            return isExpense;
        }
    }
}

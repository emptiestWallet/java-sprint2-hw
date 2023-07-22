import java.util.ArrayList;

public class YearlyReport {

    ArrayList<Record> expenses = new ArrayList<>();
    ArrayList<Record> incomes = new ArrayList<>();

    public YearlyReport(ArrayList<Record> expenses, ArrayList<Record> incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
    }
}

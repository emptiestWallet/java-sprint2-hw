import java.util.ArrayList;

public class YearlyReport {

    ArrayList<ReportEngine.Record> expenses = new ArrayList<>();
    ArrayList<ReportEngine.Record> incomes = new ArrayList<>();

    public YearlyReport(ArrayList<ReportEngine.Record> expenses, ArrayList<ReportEngine.Record> incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
    }
}

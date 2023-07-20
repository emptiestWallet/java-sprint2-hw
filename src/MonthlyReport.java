import java.util.ArrayList;

public class MonthlyReport {
    ArrayList<ReportEngine.Record> expenses;
    ArrayList<ReportEngine.Record> incomes;

    public MonthlyReport(ArrayList<ReportEngine.Record> expenses, ArrayList<ReportEngine.Record> incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
    }


}

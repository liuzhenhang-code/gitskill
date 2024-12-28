package project_manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 账单记录类
class BillRecord {
    private Date date;
    private double amount;
    private String category;
    private String remark;

    public BillRecord(Date date, double amount, String category, String remark) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.remark = remark;
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getRemark() {
        return remark;
    }

    // 判断是收入还是支出
    public boolean isIncome() {
        return amount > 0;
    }
}

// 账单管理类
class BillManager {
    private List<BillRecord> incomeList = new ArrayList<>();
    private List<BillRecord> expenseList = new ArrayList<>();
    private double monthlyBudget = 0;
    private double monthlyExpenses = 0;

    public void addIncome(String dateStr, double amount, String category, String remark) {
        if (amount <= 0) {
            System.out.println("Income amount must be positive.");
            return;
        }
        Date date = parseDate(dateStr);
        if (date == null) {
            System.out.println("Invalid date format.");
            return;
        }
        incomeList.add(new BillRecord(date, amount, category, remark));
    }

    public void addExpense(String dateStr, double amount, String category, String remark) {
        if (amount <= 0) {
            System.out.println("Expense amount must be positive.");
            return;
        }
        Date date = parseDate(dateStr);
        if (date == null) {
            System.out.println("Invalid date format.");
            return;
        }
        expenseList.add(new BillRecord(date, amount, category, remark));
        monthlyExpenses += amount;
    }

    public void setMonthlyBudget(double budget) {
        this.monthlyBudget = budget;
        this.monthlyExpenses = 0;
    }

    public double getRemainingBudget() {
        return monthlyBudget - monthlyExpenses;
    }

    public void displayRecords() {
        System.out.println("Income Records:");
        for (BillRecord record : incomeList) {
            displayRecord(record);
        }
        System.out.println("Expense Records:");
        for (BillRecord record : expenseList) {
            displayRecord(record);
        }
    }

    public void queryRecords(String category) {
        System.out.println("Queried Records:");
        for (BillRecord record : incomeList) {
            if (record.getCategory().equals(category)) {
                displayRecord(record);
            }
        }
        for (BillRecord record : expenseList) {
            if (record.getCategory().equals(category)) {
                displayRecord(record);
            }
        }
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private void displayRecord(BillRecord record) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Date: " + dateFormat.format(record.getDate()) +
                ", Amount: " + record.getAmount() +
                ", Category: " + record.getCategory() +
                ", Remark: " + record.getRemark());
    }

    public Map<String, Double> monthlyStatistics() {
        Map<String, Double> stats = new HashMap<>();
        for (BillRecord record : incomeList) {
            stats.merge(record.getCategory(), record.getAmount(), Double::sum);
        }
        for (BillRecord record : expenseList) {
            stats.merge(record.getCategory(), -record.getAmount(), Double::sum);
        }
        return stats;
    }
}

// 主类
public class PersonalFinanceManager {
    private BillManager billManager = new BillManager();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    billManager.displayRecords();
                    break;
                case 4:
                    queryRecords();
                    break;
                case 5:
                    setBudget();
                    break;
                case 6:
                    displayStatistics();
                    break;
                case 7:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\nPersonal Finance Manager");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. Display Records");
        System.out.println("4. Query Records");
        System.out.println("5. Set Monthly Budget");
        System.out.println("6. Display Monthly Statistics");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addIncome() {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter remark: ");
        String remark = scanner.nextLine();
        billManager.addIncome(dateStr, amount, category, remark);
    }

    private void addExpense() {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter remark: ");
        String remark = scanner.nextLine();
        billManager.addExpense(dateStr, amount, category, remark);
    }

    private void queryRecords() {
        System.out.print("Enter category to query: ");
        String category = scanner.nextLine();
        billManager.queryRecords(category);
    }

    private void setBudget() {
        System.out.print("Enter monthly budget: ");
        double budget = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        billManager.setMonthlyBudget(budget);
    }

    private void displayStatistics() {
        Map<String, Double> stats = billManager.monthlyStatistics();
        stats.forEach((category, amount) -> System.out.println(category + ": " + amount));
    }

    public static void main(String[] args) {
        PersonalFinanceManager manager = new PersonalFinanceManager();
        manager.start();
    }
}
//注释1

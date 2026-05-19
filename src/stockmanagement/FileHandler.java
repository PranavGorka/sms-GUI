package stockmanagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String DATA_DIR  = "data";
    private static final String FILE_PATH = DATA_DIR + File.separator + "stocks.csv";

    public static void saveStocks(List<Stock> stocks) {
        new File(DATA_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("id,companyName,symbol,quantity,purchasePrice,currentPrice,sector");
            for (Stock s : stocks) pw.println(s.toCSV());
        } catch (IOException e) {
            System.err.println("[ERROR] Could not save: " + e.getMessage());
        }
    }

    public static List<Stock> loadStocks() {
        List<Stock> list = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                line = line.trim();
                if (!line.isEmpty()) {
                    try { list.add(Stock.fromCSV(line)); }
                    catch (Exception ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Could not load: " + e.getMessage());
        }
        return list;
    }

    public static void exportReport(List<Stock> stocks) {
        new File(DATA_DIR).mkdirs();
        String reportPath = DATA_DIR + File.separator + "report.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(reportPath))) {
            pw.println("========== STOCK PORTFOLIO REPORT ==========");
            pw.printf("%-4s %-20s %-8s %-8s %12s %12s %-15s %12s%n",
                "ID","Company","Symbol","Qty","Buy Price","Curr Price","Sector","P/L");
            pw.println("-".repeat(100));
            double totalInvest = 0, totalValue = 0;
            for (Stock s : stocks) {
                pw.printf("%-4d %-20s %-8s %-8d %12.2f %12.2f %-15s %+12.2f%n",
                    s.getId(), s.getCompanyName(), s.getSymbol(), s.getQuantity(),
                    s.getPurchasePrice(), s.getCurrentPrice(), s.getSector(), s.getProfitLoss());
                totalInvest += s.getPurchasePrice() * s.getQuantity();
                totalValue  += s.getTotalValue();
            }
            pw.println("-".repeat(100));
            pw.printf("Total Investment : %.2f%n", totalInvest);
            pw.printf("Current Value    : %.2f%n", totalValue);
            pw.printf("Net P/L          : %+.2f%n", totalValue - totalInvest);
            pw.println("============================================");
        } catch (IOException e) {
            System.err.println("[ERROR] Report export failed: " + e.getMessage());
        }
    }
}

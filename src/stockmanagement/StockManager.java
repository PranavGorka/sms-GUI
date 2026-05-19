package stockmanagement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StockManager {

    private List<Stock> stocks = new ArrayList<>();
    private int nextId = 1;

    public StockManager() {
        stocks = FileHandler.loadStocks();
        if (!stocks.isEmpty()) {
            nextId = stocks.stream().mapToInt(Stock::getId).max().orElse(0) + 1;
        }
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stocks);
    }

    public Stock addStock(String company, String symbol, int qty,
                          double buyPrice, double currPrice, String sector) {
        Stock s = new Stock(nextId++, company, symbol, qty, buyPrice, currPrice, sector);
        stocks.add(s);
        FileHandler.saveStocks(stocks);
        return s;
    }

    public boolean updateStock(int id, String company, String symbol, int qty,
                               double buyPrice, double currPrice, String sector) {
        for (Stock s : stocks) {
            if (s.getId() == id) {
                s.setCompanyName(company);
                s.setSymbol(symbol);
                s.setQuantity(qty);
                s.setPurchasePrice(buyPrice);
                s.setCurrentPrice(currPrice);
                s.setSector(sector);
                FileHandler.saveStocks(stocks);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStock(int id) {
        boolean removed = stocks.removeIf(s -> s.getId() == id);
        if (removed) FileHandler.saveStocks(stocks);
        return removed;
    }

    public Stock findById(int id) {
        return stocks.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public List<Stock> search(String keyword) {
        String kw = keyword.toLowerCase();
        return stocks.stream()
            .filter(s -> s.getCompanyName().toLowerCase().contains(kw)
                      || s.getSymbol().toLowerCase().contains(kw)
                      || s.getSector().toLowerCase().contains(kw))
            .collect(Collectors.toList());
    }

    public List<Stock> sortBy(String field) {
        List<Stock> sorted = new ArrayList<>(stocks);
        switch (field) {
            case "name":    sorted.sort(Comparator.comparing(Stock::getCompanyName));   break;
            case "price":   sorted.sort(Comparator.comparingDouble(Stock::getCurrentPrice)); break;
            case "qty":     sorted.sort(Comparator.comparingInt(Stock::getQuantity));   break;
            case "pl":      sorted.sort(Comparator.comparingDouble(Stock::getProfitLoss)); break;
        }
        return sorted;
    }

    // Summary stats
    public double getTotalInvestment() {
        return stocks.stream().mapToDouble(s -> s.getPurchasePrice() * s.getQuantity()).sum();
    }
    public double getTotalCurrentValue() {
        return stocks.stream().mapToDouble(Stock::getTotalValue).sum();
    }
    public double getTotalPL() {
        return getTotalCurrentValue() - getTotalInvestment();
    }
    public long getGainerCount() {
        return stocks.stream().filter(s -> s.getProfitLoss() > 0).count();
    }
    public long getLoserCount() {
        return stocks.stream().filter(s -> s.getProfitLoss() < 0).count();
    }
    public int getStockCount() { return stocks.size(); }

    public void exportReport() {
        FileHandler.exportReport(stocks);
    }
}

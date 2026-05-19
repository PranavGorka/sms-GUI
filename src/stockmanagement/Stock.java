package stockmanagement;

public class Stock {

    private int id;
    private String companyName;
    private String symbol;
    private int quantity;
    private double purchasePrice;
    private double currentPrice;
    private String sector;

    public Stock(int id, String companyName, String symbol, int quantity,
                 double purchasePrice, double currentPrice, String sector) {
        this.id = id;
        this.companyName = companyName;
        this.symbol = symbol.toUpperCase();
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.sector = sector;
    }

    public int getId()               { return id; }
    public String getCompanyName()   { return companyName; }
    public String getSymbol()        { return symbol; }
    public int getQuantity()         { return quantity; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getCurrentPrice()  { return currentPrice; }
    public String getSector()        { return sector; }

    public void setCompanyName(String v)   { this.companyName = v; }
    public void setSymbol(String v)        { this.symbol = v.toUpperCase(); }
    public void setQuantity(int v)         { this.quantity = v; }
    public void setPurchasePrice(double v) { this.purchasePrice = v; }
    public void setCurrentPrice(double v)  { this.currentPrice = v; }
    public void setSector(String v)        { this.sector = v; }

    public double getProfitLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }

    public double getTotalValue() {
        return currentPrice * quantity;
    }

    public String toCSV() {
        return id + "," + companyName + "," + symbol + "," + quantity
               + "," + purchasePrice + "," + currentPrice + "," + sector;
    }

    public static Stock fromCSV(String line) {
        String[] p = line.split(",", 7);
        return new Stock(Integer.parseInt(p[0].trim()), p[1].trim(),
            p[2].trim(), Integer.parseInt(p[3].trim()),
            Double.parseDouble(p[4].trim()), Double.parseDouble(p[5].trim()),
            p[6].trim());
    }
}

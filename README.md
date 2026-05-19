# 📈 Stock Management System — GUI (Java AWT)

A fully functional **GUI-based Stock Management System** built using **Java AWT**, developed as Assignment 2 (GUI CRUD Application) for 4th Semester — Java Programming Practical.

---

## 🚀 Features

| Feature | Description |
|---|---|
| ➕ Add Stock | Add stocks with company name, symbol, quantity, buy price, current price, sector |
| ✏ Update Stock | Modify any existing stock record by selecting from the list |
| 🗑 Delete Stock | Remove a stock with confirmation dialog |
| ✖ Clear Form | Reset all input fields |
| 🔍 Search | Search by company name, symbol, or sector |
| 📊 Sort | Sort by Name, Price, Quantity, or P/L |
| 📄 Export Report | Export portfolio summary to `data/report.txt` |
| 💾 CSV Persistence | Auto-saves to `data/stocks.csv` on every operation |
| 📈 Dashboard | Summary cards showing live portfolio stats |
| 🧭 Multi-tab Navigation | Dashboard / Manage / Search & Sort / Portfolio Summary |

---

## 📂 Directory Structure

```
StockManagementSystemGUI/
├── src/
│   └── stockmanagement/
│       ├── Main.java          ← GUI entry point (Java AWT)
│       ├── Stock.java         ← Data model
│       ├── StockManager.java  ← Business logic (CRUD, Search, Sort)
│       └── FileHandler.java   ← CSV file persistence
├── data/
│   ├── stocks.csv             ← Auto-generated on first run
│   └── report.txt             ← Generated on Export
├── out/                       ← Compiled .class files
├── StockManagementSystemGUI.jar  ← Runnable JAR
├── manifest.txt
└── README.md
```

---

## ▶️ How to Run

### Option 1 — Run the JAR (easiest)
```bash
java -jar StockManagementSystemGUI.jar
```

### Option 2 — Compile and Run from Source (VS Code)
```bash
# Step 1: Compile
javac -d out src/stockmanagement/*.java

# Step 2: Run
java -cp out stockmanagement.Main
```

### Option 3 — VS Code
- Open the `StockManagementSystemGUI` folder in VS Code
- Install **Extension Pack for Java**
- Open `src/stockmanagement/Main.java`
- Click ▶ **Run Java** above `main()`

---

## 🗃 CSV Format

```csv
id,companyName,symbol,quantity,purchasePrice,currentPrice,sector
1,Reliance Industries,RELIANCE,100,2400.50,2650.00,Energy
2,Infosys Limited,INFY,200,1450.00,1580.75,Technology
```

---

## 📋 Requirements

- Java JDK 11 or higher
- No external libraries — pure Java AWT + standard library

---

## 📚 Course Info

| Field | Value |
|---|---|
| Assignment | Assignment 2 — GUI-Based CRUD Application |
| Subject | Java Programming — Practical |
| Semester | 4th Semester, CSE |
| GUI Framework | Java AWT |
| Storage | CSV File |

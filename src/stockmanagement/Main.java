package stockmanagement;

import java.awt.*;
import java.awt.event.*;

public class Main extends Frame {

    // ── Colours ──────────────────────────────────────────────────────────────
    static final Color BG_DARK      = new Color(15, 23, 42);
    static final Color BG_PANEL     = new Color(30, 41, 59);
    static final Color BG_CARD      = new Color(51, 65, 85);
    static final Color ACCENT_GREEN = new Color(16, 185, 129);
    static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    static final Color ACCENT_RED   = new Color(239, 68, 68);
    static final Color ACCENT_AMBER = new Color(245, 158, 11);
    static final Color TEXT_WHITE   = new Color(248, 250, 252);
    static final Color TEXT_MUTED   = new Color(148, 163, 184);
    static final Color BTN_ADD      = new Color(16, 185, 129);
    static final Color BTN_UPD      = new Color(59, 130, 246);
    static final Color BTN_DEL      = new Color(239, 68, 68);
    static final Color BTN_CLR      = new Color(100, 116, 139);
    static final Color BTN_SRCH     = new Color(139, 92, 246);
    static final Color BTN_EXP      = new Color(245, 158, 11);

    // ── State ─────────────────────────────────────────────────────────────────
    private StockManager manager = new StockManager();

    // ── Input fields ─────────────────────────────────────────────────────────
    private TextField tfId, tfCompany, tfSymbol, tfQty, tfBuyPrice, tfCurrPrice, tfSector, tfSearch;

    // ── Summary labels ───────────────────────────────────────────────────────
    private Label lblTotalStocks, lblTotalInvest, lblCurrVal, lblNetPL, lblGainers, lblLosers;

    // ── Table ─────────────────────────────────────────────────────────────────
    private java.awt.List lstDashboard; // Dashboard tab — its own list instance
    private java.awt.List lstStocks;    // Manage Stocks tab — its own list instance
    private java.awt.List lstSearch;    // Search & Sort tab — its own list instance

    // ── Status bar ────────────────────────────────────────────────────────────
    private Label lblStatus;

    // ── Active card panel (for switching views) ───────────────────────────────
    private CardLayout cardLayout;
    private Panel cardContainer;

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Main());
    }

    public Main() {
        super("📈 Stock Management System — GUI");
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));
        setSize(1100, 750);
        setLocationRelativeTo(null);

        // Window close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        buildUI();
        setVisible(true);
        refreshTable(manager.getAllStocks());
        updateSummary();
        setStatus("Welcome to Stock Management System  |  " + manager.getStockCount() + " stocks loaded.", ACCENT_GREEN);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  BUILD UI
    // ═════════════════════════════════════════════════════════════════════════
    private void buildUI() {
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildSidePanel(), BorderLayout.WEST);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Top bar ──────────────────────────────────────────────────────────────
    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        bar.setBackground(BG_PANEL);

        // Logo / title
        Label title = new Label("  📈  STOCK MANAGEMENT SYSTEM", Label.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(ACCENT_GREEN);
        title.setBackground(BG_PANEL);

        // Right-side nav tabs
        Panel tabs = new Panel(new FlowLayout(FlowLayout.RIGHT, 4, 8));
        tabs.setBackground(BG_PANEL);
        String[] tabNames = {"Dashboard", "Manage Stocks", "Search & Sort", "Portfolio Summary"};
        for (String t : tabNames) {
            Button b = makeNavBtn(t);
            b.addActionListener(e -> cardLayout.show(cardContainer, t));
            tabs.add(b);
        }

        bar.add(title, BorderLayout.WEST);
        bar.add(tabs,  BorderLayout.EAST);

        // Bottom border line
        Panel border = new Panel();
        border.setBackground(ACCENT_GREEN);
        border.setPreferredSize(new Dimension(0, 2));
        bar.add(border, BorderLayout.SOUTH);

        return bar;
    }

    private Button makeNavBtn(String label) {
        Button b = new Button(label);
        b.setFont(new Font("Arial", Font.PLAIN, 12));
        b.setForeground(TEXT_WHITE);
        b.setBackground(BG_CARD);
        b.setPreferredSize(new Dimension(140, 34));
        return b;
    }

    // ── Side panel (summary cards) ────────────────────────────────────────────
    private Panel buildSidePanel() {
        Panel side = new Panel(new GridLayout(7, 1, 0, 6));
        side.setBackground(BG_DARK);
        side.setPreferredSize(new Dimension(210, 0));

        // padding top
        Panel pad = new Panel(); pad.setBackground(BG_DARK); side.add(pad);

        lblTotalStocks = summaryCard(side, "Total Stocks",   "0",       TEXT_WHITE);
        lblTotalInvest = summaryCard(side, "Invested (₹)",   "0.00",    ACCENT_BLUE);
        lblCurrVal     = summaryCard(side, "Current (₹)",    "0.00",    ACCENT_GREEN);
        lblNetPL       = summaryCard(side, "Net P/L (₹)",    "0.00",    ACCENT_AMBER);
        lblGainers     = summaryCard(side, "Gainers 📈",      "0",       ACCENT_GREEN);
        lblLosers      = summaryCard(side, "Losers 📉",       "0",       ACCENT_RED);

        return side;
    }

    private Label summaryCard(Panel parent, String title, String init, Color valColor) {
        Panel card = new Panel(new BorderLayout(4, 2));
        card.setBackground(BG_CARD);

        Label lTitle = new Label(title, Label.CENTER);
        lTitle.setFont(new Font("Arial", Font.PLAIN, 10));
        lTitle.setForeground(TEXT_MUTED);
        lTitle.setBackground(BG_CARD);

        Label lVal = new Label(init, Label.CENTER);
        lVal.setFont(new Font("Arial", Font.BOLD, 14));
        lVal.setForeground(valColor);
        lVal.setBackground(BG_CARD);

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lVal,   BorderLayout.CENTER);
        parent.add(card);
        return lVal;
    }

    // ── Center card container ─────────────────────────────────────────────────
    private Panel buildCenter() {
        cardLayout    = new CardLayout();
        cardContainer = new Panel(cardLayout);
        cardContainer.setBackground(BG_DARK);

        // Each tab gets its own AWT List — AWT components can only have one parent,
        // so sharing a single instance across tabs causes the second add() to steal
        // it from the first panel, leaving Dashboard with an empty hole.
        lstDashboard = new java.awt.List(20, false);
        lstDashboard.setBackground(BG_CARD);
        lstDashboard.setForeground(TEXT_WHITE);
        lstDashboard.setFont(new Font("Monospaced", Font.PLAIN, 11));

        lstStocks = new java.awt.List(20, false);
        lstStocks.setBackground(BG_CARD);
        lstStocks.setForeground(TEXT_WHITE);
        lstStocks.setFont(new Font("Monospaced", Font.PLAIN, 11));

        lstSearch = new java.awt.List(20, false);
        lstSearch.setBackground(BG_CARD);
        lstSearch.setForeground(TEXT_WHITE);
        lstSearch.setFont(new Font("Monospaced", Font.PLAIN, 11));

        cardContainer.add(buildDashboard(),       "Dashboard");
        cardContainer.add(buildManagePanel(),      "Manage Stocks");
        cardContainer.add(buildSearchSortPanel(),  "Search & Sort");
        cardContainer.add(buildSummaryPanel(),     "Portfolio Summary");

        cardLayout.show(cardContainer, "Dashboard");
        return cardContainer;
    }

    // ─── Dashboard ─────────────────────────────────────────────────────────────
    private Panel buildDashboard() {
        Panel p = new Panel(new BorderLayout(8, 8));
        p.setBackground(BG_DARK);

        Label welcome = new Label("  Welcome to Stock Management System  |  Click a row, then go to Manage Stocks to edit/delete", Label.LEFT);
        welcome.setFont(new Font("Arial", Font.BOLD, 13));
        welcome.setForeground(ACCENT_GREEN);
        welcome.setBackground(BG_DARK);

        // Dashboard gets its own list; double-click loads the stock into the form
        // and switches to the Manage Stocks tab.
        lstDashboard.addActionListener(e -> loadSelectedToForm());
        Panel listPanel = buildStockListPanel("All Stocks", lstDashboard);

        p.add(welcome,   BorderLayout.NORTH);
        p.add(listPanel, BorderLayout.CENTER);
        return p;
    }

    // ─── Manage Stocks panel ──────────────────────────────────────────────────
    private Panel buildManagePanel() {
        Panel p = new Panel(new BorderLayout(6, 6));
        p.setBackground(BG_DARK);

        // ── Input form ──
        Panel form = new Panel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 8, 5, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        // Row header
        Label hdr = new Label("  ✏  Add / Edit Stock Record", Label.LEFT);
        hdr.setFont(new Font("Arial", Font.BOLD, 13));
        hdr.setForeground(ACCENT_BLUE);
        hdr.setBackground(BG_PANEL);
        gc.gridx=0; gc.gridy=0; gc.gridwidth=4; form.add(hdr, gc); gc.gridwidth=1;

        // Fields
        String[] labels = {"ID (auto)","Company Name","Symbol","Quantity","Buy Price (₹)","Current Price (₹)","Sector"};
        tfId        = styledField("Auto"); tfId.setEditable(false);
        tfCompany   = styledField("e.g. Reliance Industries");
        tfSymbol    = styledField("e.g. RELIANCE");
        tfQty       = styledField("e.g. 100");
        tfBuyPrice  = styledField("e.g. 2400.50");
        tfCurrPrice = styledField("e.g. 2650.00");
        tfSector    = styledField("e.g. Energy");
        TextField[] fields = {tfId, tfCompany, tfSymbol, tfQty, tfBuyPrice, tfCurrPrice, tfSector};

        int row = 1;
        for (int i = 0; i < labels.length; i++) {
            int col = (i % 2 == 0) ? 0 : 2;
            if (i % 2 == 0 && i > 0) row++;
            gc.gridx = col;   gc.gridy = row; gc.weightx = 0;
            form.add(makeLabel(labels[i]), gc);
            gc.gridx = col+1; gc.weightx = 1;
            form.add(fields[i], gc);
        }
        row++;

        // Buttons row
        Panel btnRow = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnRow.setBackground(BG_PANEL);
        Button btnAdd = makeBtn("➕ Add",    BTN_ADD);
        Button btnUpd = makeBtn("✏ Update", BTN_UPD);
        Button btnDel = makeBtn("🗑 Delete", BTN_DEL);
        Button btnClr = makeBtn("✖ Clear",  BTN_CLR);
        btnRow.add(btnAdd); btnRow.add(btnUpd); btnRow.add(btnDel); btnRow.add(btnClr);

        gc.gridx=0; gc.gridy=row; gc.gridwidth=4;
        form.add(btnRow, gc);

        // ── Event handlers ──
        btnAdd.addActionListener(e -> doAdd());
        btnUpd.addActionListener(e -> doUpdate());
        btnDel.addActionListener(e -> doDelete());
        btnClr.addActionListener(e -> clearForm());

        // ── Stock list — Manage Stocks tab has its own lstStocks instance ──
        // Wire click-to-form here (lstDashboard has its own listener added in buildDashboard).
        lstStocks.addActionListener(e -> loadSelectedToForm());
        Panel listPanel = buildStockListPanel("Stock List  (click row to load into form)", lstStocks);

        p.add(form,      BorderLayout.NORTH);
        p.add(listPanel, BorderLayout.CENTER);
        return p;
    }

    // ─── Search & Sort panel ──────────────────────────────────────────────────
    private Panel buildSearchSortPanel() {
        Panel p = new Panel(new BorderLayout(6, 6));
        p.setBackground(BG_DARK);

        // Search bar
        Panel searchBar = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setBackground(BG_PANEL);

        Label lbl = makeLabel("🔍 Search:");
        tfSearch = styledField("Search by company, symbol or sector...");
        tfSearch.setPreferredSize(new Dimension(280, 28));

        Button btnSearch = makeBtn("Search",   BTN_SRCH);
        Button btnAll    = makeBtn("Show All", BTN_CLR);

        searchBar.add(lbl); searchBar.add(tfSearch); searchBar.add(btnSearch); searchBar.add(btnAll);

        // Sort buttons
        Label sortLbl = makeLabel("  Sort by:");
        sortLbl.setForeground(TEXT_MUTED);
        Button btnName  = makeBtn("Name ↑",    BTN_UPD);
        Button btnPrice = makeBtn("Price ↑",   BTN_ADD);
        Button btnQty   = makeBtn("Quantity ↑",BTN_CLR);
        Button btnPL    = makeBtn("P/L ↑",     BTN_EXP);
        searchBar.add(sortLbl); searchBar.add(btnName); searchBar.add(btnPrice);
        searchBar.add(btnQty);  searchBar.add(btnPL);

        btnSearch.addActionListener(e -> {
            String kw = tfSearch.getText().trim();
            if (kw.isEmpty()) { setStatus("Please enter a search keyword.", ACCENT_AMBER); return; }
            java.util.List<Stock> res = manager.search(kw);
            refreshTable(res);
            setStatus("Found " + res.size() + " result(s) for: \"" + kw + "\"", ACCENT_GREEN);
        });
        btnAll.addActionListener(e -> { refreshTable(manager.getAllStocks()); setStatus("Showing all stocks.", TEXT_WHITE); });
        btnName.addActionListener(e  -> { refreshTable(manager.sortBy("name"));  setStatus("Sorted by Name.", TEXT_WHITE); });
        btnPrice.addActionListener(e -> { refreshTable(manager.sortBy("price")); setStatus("Sorted by Current Price.", TEXT_WHITE); });
        btnQty.addActionListener(e   -> { refreshTable(manager.sortBy("qty"));   setStatus("Sorted by Quantity.", TEXT_WHITE); });
        btnPL.addActionListener(e    -> { refreshTable(manager.sortBy("pl"));    setStatus("Sorted by Profit/Loss.", TEXT_WHITE); });

        Panel listPanel = buildStockListPanel("Results", lstSearch);
        p.add(searchBar, BorderLayout.NORTH);
        p.add(listPanel, BorderLayout.CENTER);
        return p;
    }

    // ─── Portfolio Summary panel ──────────────────────────────────────────────
    private Panel buildSummaryPanel() {
        Panel p = new Panel(new BorderLayout(8, 8));
        p.setBackground(BG_DARK);

        Panel top = new Panel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        top.setBackground(BG_PANEL);
        Label hdr = new Label("  📊  Portfolio Summary & Export", Label.LEFT);
        hdr.setFont(new Font("Arial", Font.BOLD, 14));
        hdr.setForeground(ACCENT_AMBER);
        hdr.setBackground(BG_PANEL);
        Button btnExport = makeBtn("📄 Export Report (TXT)", BTN_EXP);
        btnExport.addActionListener(e -> {
            manager.exportReport();
            setStatus("Report exported to data/report.txt successfully!", ACCENT_GREEN);
        });
        top.add(hdr); top.add(btnExport);

        // Big summary text area
        TextArea ta = new TextArea("", 20, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setBackground(BG_CARD);
        ta.setForeground(TEXT_WHITE);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);

        // Refresh button
        Button btnRefresh = makeBtn("🔄 Refresh Summary", BTN_UPD);
        btnRefresh.addActionListener(e -> {
            ta.setText(buildSummaryText());
            updateSummary();
        });
        // Auto populate
        ta.setText(buildSummaryText());

        Panel bot = new Panel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(BG_DARK);
        bot.add(btnRefresh);

        p.add(top,        BorderLayout.NORTH);
        p.add(ta,         BorderLayout.CENTER);
        p.add(bot,        BorderLayout.SOUTH);
        return p;
    }

    private String buildSummaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(70)).append("\n");
        sb.append("           STOCK PORTFOLIO SUMMARY\n");
        sb.append("═".repeat(70)).append("\n\n");

        java.util.List<Stock> all = manager.getAllStocks();
        sb.append(String.format("  %-25s %d%n", "Total Stocks:", manager.getStockCount()));
        sb.append(String.format("  %-25s ₹%.2f%n", "Total Invested:", manager.getTotalInvestment()));
        sb.append(String.format("  %-25s ₹%.2f%n", "Current Portfolio Value:", manager.getTotalCurrentValue()));
        sb.append(String.format("  %-25s ₹%+.2f%n", "Net Profit / Loss:", manager.getTotalPL()));
        sb.append(String.format("  %-25s %d%n", "Gainers:", manager.getGainerCount()));
        sb.append(String.format("  %-25s %d%n", "Losers:", manager.getLoserCount()));
        sb.append("\n").append("─".repeat(70)).append("\n");
        sb.append(String.format("  %-4s %-20s %-8s %10s %12s %12s%n",
            "ID","Company","Symbol","Qty","Curr Price","P/L"));
        sb.append("─".repeat(70)).append("\n");
        for (Stock s : all) {
            double pl = s.getProfitLoss();
            sb.append(String.format("  %-4d %-20s %-8s %10d %12.2f %+12.2f%n",
                s.getId(), s.getCompanyName(), s.getSymbol(),
                s.getQuantity(), s.getCurrentPrice(), pl));
        }
        sb.append("═".repeat(70)).append("\n");
        return sb.toString();
    }

    // ─── Shared stock list panel — accepts the list component to embed ──────────
    private Panel buildStockListPanel(String title, java.awt.List listComponent) {
        Panel p = new Panel(new BorderLayout(4, 4));
        p.setBackground(BG_DARK);

        Label hdr = new Label(" " + title, Label.LEFT);
        hdr.setFont(new Font("Arial", Font.BOLD, 12));
        hdr.setForeground(TEXT_MUTED);
        hdr.setBackground(BG_DARK);

        Label colHdr = new Label(
            String.format(" %-4s %-20s %-8s %-8s %12s %12s %-15s %12s",
                "ID","Company","Symbol","Qty","Buy Price","Curr Price","Sector","P/L (₹)"),
            Label.LEFT);
        colHdr.setFont(new Font("Monospaced", Font.BOLD, 11));
        colHdr.setForeground(ACCENT_BLUE);
        colHdr.setBackground(BG_PANEL);

        Panel headerBox = new Panel(new BorderLayout());
        headerBox.setBackground(BG_DARK);
        headerBox.add(hdr,    BorderLayout.NORTH);
        headerBox.add(colHdr, BorderLayout.CENTER);

        p.add(headerBox,     BorderLayout.NORTH);
        p.add(listComponent, BorderLayout.CENTER);
        return p;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private Panel buildStatusBar() {
        Panel bar = new Panel(new BorderLayout());
        bar.setBackground(BG_PANEL);

        lblStatus = new Label("  Ready", Label.LEFT);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(TEXT_WHITE);
        lblStatus.setBackground(BG_PANEL);

        Label copy = new Label("Stock Management System  v2.0 — GUI  |  CSV Storage ", Label.RIGHT);
        copy.setFont(new Font("Arial", Font.PLAIN, 10));
        copy.setForeground(TEXT_MUTED);
        copy.setBackground(BG_PANEL);

        Panel line = new Panel();
        line.setBackground(ACCENT_BLUE);
        line.setPreferredSize(new Dimension(0, 2));

        bar.add(line,    BorderLayout.NORTH);
        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(copy,    BorderLayout.EAST);
        return bar;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  CRUD OPERATIONS
    // ═════════════════════════════════════════════════════════════════════════
    private void doAdd() {
        try {
            String company  = tfCompany.getText().trim();
            String symbol   = tfSymbol.getText().trim();
            String qtyStr   = tfQty.getText().trim();
            String buyStr   = tfBuyPrice.getText().trim();
            String currStr  = tfCurrPrice.getText().trim();
            String sector   = tfSector.getText().trim();

            if (company.isEmpty() || symbol.isEmpty() || qtyStr.isEmpty()
                    || buyStr.isEmpty() || currStr.isEmpty() || sector.isEmpty()) {
                setStatus("⚠  All fields are required!", ACCENT_RED); return;
            }

            int qty      = Integer.parseInt(qtyStr);
            double buy   = Double.parseDouble(buyStr);
            double curr  = Double.parseDouble(currStr);

            if (qty <= 0 || buy <= 0 || curr <= 0) {
                setStatus("⚠  Qty and prices must be positive numbers.", ACCENT_RED); return;
            }

            Stock s = manager.addStock(company, symbol, qty, buy, curr, sector);
            clearForm();
            refreshTable(manager.getAllStocks());
            updateSummary();
            setStatus("✅  Stock added: " + s.getCompanyName() + " [" + s.getSymbol() + "] (ID: " + s.getId() + ")", ACCENT_GREEN);
        } catch (NumberFormatException ex) {
            setStatus("⚠  Invalid number format in Qty / Buy Price / Current Price.", ACCENT_RED);
        }
    }

    private void doUpdate() {
        try {
            String idStr = tfId.getText().trim();
            if (idStr.isEmpty() || idStr.equals("Auto")) {
                setStatus("⚠  Select a stock from the list first.", ACCENT_AMBER); return;
            }
            int id = Integer.parseInt(idStr);
            String company = tfCompany.getText().trim();
            String symbol  = tfSymbol.getText().trim();
            int qty        = Integer.parseInt(tfQty.getText().trim());
            double buy     = Double.parseDouble(tfBuyPrice.getText().trim());
            double curr    = Double.parseDouble(tfCurrPrice.getText().trim());
            String sector  = tfSector.getText().trim();

            if (company.isEmpty() || symbol.isEmpty() || sector.isEmpty()) {
                setStatus("⚠  All fields are required.", ACCENT_RED); return;
            }

            boolean ok = manager.updateStock(id, company, symbol, qty, buy, curr, sector);
            if (ok) {
                clearForm();
                refreshTable(manager.getAllStocks());
                updateSummary();
                setStatus("✅  Stock ID " + id + " updated successfully.", ACCENT_GREEN);
            } else {
                setStatus("⚠  Stock ID " + id + " not found.", ACCENT_RED);
            }
        } catch (NumberFormatException ex) {
            setStatus("⚠  Invalid number in fields.", ACCENT_RED);
        }
    }

    private void doDelete() {
        String idStr = tfId.getText().trim();
        if (idStr.isEmpty() || idStr.equals("Auto")) {
            setStatus("⚠  Select a stock from the list to delete.", ACCENT_AMBER); return;
        }
        try {
            int id = Integer.parseInt(idStr);
            // Simple AWT dialog
            Dialog dlg = new Dialog(this, "Confirm Delete", true);
            dlg.setLayout(new BorderLayout(10, 10));
            dlg.setBackground(BG_PANEL);
            dlg.setSize(360, 140);
            dlg.setLocationRelativeTo(this);

            Label msg = new Label("  Delete Stock ID " + id + "?  This cannot be undone.", Label.CENTER);
            msg.setForeground(TEXT_WHITE); msg.setBackground(BG_PANEL);

            Panel btnP = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 8));
            btnP.setBackground(BG_PANEL);
            Button yes = makeBtn("Yes, Delete", BTN_DEL);
            Button no  = makeBtn("Cancel",      BTN_CLR);
            btnP.add(yes); btnP.add(no);

            yes.addActionListener(e2 -> {
                dlg.dispose();
                if (manager.deleteStock(id)) {
                    clearForm();
                    refreshTable(manager.getAllStocks());
                    updateSummary();
                    setStatus("🗑  Stock ID " + id + " deleted.", ACCENT_RED);
                } else {
                    setStatus("⚠  Stock ID " + id + " not found.", ACCENT_RED);
                }
            });
            no.addActionListener(e2 -> dlg.dispose());
            dlg.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) { dlg.dispose(); }
            });

            dlg.add(msg,  BorderLayout.CENTER);
            dlg.add(btnP, BorderLayout.SOUTH);
            dlg.setVisible(true);
        } catch (NumberFormatException ex) {
            setStatus("⚠  Invalid stock ID.", ACCENT_RED);
        }
    }

    private void loadSelectedToForm() {
        // Check both Dashboard and Manage Stocks lists — both wire to this handler.
        // Only one can have a selection at a time since they are separate components.
        int idx = lstDashboard.getSelectedIndex();
        java.awt.List source = (idx >= 0) ? lstDashboard : lstStocks;
        idx = (idx >= 0) ? idx : lstStocks.getSelectedIndex();
        if (idx < 0) return;
        String line = source.getItem(idx).trim();
        try {
            int id = Integer.parseInt(line.substring(0, line.indexOf(' ')).trim());
            Stock s = manager.findById(id);
            if (s == null) return;
            tfId.setText(String.valueOf(s.getId()));
            tfCompany.setText(s.getCompanyName());
            tfSymbol.setText(s.getSymbol());
            tfQty.setText(String.valueOf(s.getQuantity()));
            tfBuyPrice.setText(String.format("%.2f", s.getPurchasePrice()));
            tfCurrPrice.setText(String.format("%.2f", s.getCurrentPrice()));
            tfSector.setText(s.getSector());
            setStatus("Stock ID " + id + " loaded into form. Edit and click Update.", ACCENT_BLUE);
            // Switch to Manage Stocks if on Dashboard
            cardLayout.show(cardContainer, "Manage Stocks");
        } catch (Exception ignored) {}
    }

    private void clearForm() {
        tfId.setText("Auto");
        tfCompany.setText(""); tfSymbol.setText(""); tfQty.setText("");
        tfBuyPrice.setText(""); tfCurrPrice.setText(""); tfSector.setText("");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  REFRESH HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    // fillList fills any AWT List component (used by search tab)
    private void fillList(java.awt.List lst, java.util.List<Stock> list) {
        lst.removeAll();
        for (Stock s : list) {
            double pl = s.getProfitLoss();
            String row = String.format("%-4d %-20s %-8s %-8d %12.2f %12.2f %-15s %+12.2f",
                s.getId(), s.getCompanyName(), s.getSymbol(), s.getQuantity(),
                s.getPurchasePrice(), s.getCurrentPrice(), s.getSector(), pl);
            lst.add(row);
        }
    }

    // refreshTable keeps all three list components in sync.
    // Dashboard and Manage Stocks always show the full (or sorted/filtered) view;
    // lstSearch is also refreshed so Search & Sort stays consistent after CRUD ops.
    private void refreshTable(java.util.List<Stock> list) {
        fillList(lstDashboard, list);
        fillList(lstStocks,    list);
        fillList(lstSearch,    list);
    }

    private void updateSummary() {
        lblTotalStocks.setText(String.valueOf(manager.getStockCount()));
        lblTotalInvest.setText(String.format("%.2f", manager.getTotalInvestment()));
        lblCurrVal.setText(String.format("%.2f",     manager.getTotalCurrentValue()));
        double pl = manager.getTotalPL();
        lblNetPL.setText(String.format("%+.2f", pl));
        lblNetPL.setForeground(pl >= 0 ? ACCENT_GREEN : ACCENT_RED);
        lblGainers.setText(String.valueOf(manager.getGainerCount()));
        lblLosers.setText(String.valueOf(manager.getLoserCount()));
    }

    private void setStatus(String msg, Color c) {
        lblStatus.setText("  " + msg);
        lblStatus.setForeground(c);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  WIDGET FACTORIES
    // ═════════════════════════════════════════════════════════════════════════
    private TextField styledField(String placeholder) {
        TextField tf = new TextField(22);
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_WHITE);
        tf.setFont(new Font("Arial", Font.PLAIN, 12));
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) tf.setText("");
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) tf.setText(placeholder);
            }
        });
        return tf;
    }

    private Label makeLabel(String text) {
        Label l = new Label(text, Label.RIGHT);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        l.setForeground(TEXT_MUTED);
        l.setBackground(BG_PANEL);
        return l;
    }

    private Button makeBtn(String label, Color bg) {
        Button b = new Button(label);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(140, 32));
        return b;
    }
}

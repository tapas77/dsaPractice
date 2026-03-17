package org.example;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.*;

public class VendingMAchine {


// =============================================================================
//  VENDING MACHINE — LOW-LEVEL DESIGN
//  Design Patterns : State (core), Singleton, MVC
//  Language        : Java  |  Storage : In-Memory
//  File            : VendingMachineDemo.java
// =============================================================================

// ─────────────────────── SHARED UTILITY ──────────────────────────────────────

    class Money {
        /** Format cents as a dollar string, e.g. 175 → "1.75" */
        public static String fmt(int cents) {
            return String.format("%.2f", cents / 100.0);
        }
    }

// ──────────────────────────── ENUMS ──────────────────────────────────────────

    enum Coin {
        PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

        private final int cents;
        Coin(int c)            { this.cents = c; }
        public int getValue()  { return cents; }
    }

    enum Note {
        ONE(100), FIVE(500), TEN(1000), TWENTY(2000);

        private final int cents;
        Note(int c)            { this.cents = c; }
        public int getValue()  { return cents; }
    }

// ──────────────────────────── MODELS ─────────────────────────────────────────

    /** Immutable product definition. */
    static class Product {
        private final String id;
        private final String name;
        private final int    priceCents;

        public Product(String id, String name, int priceCents) {
            this.id         = id;
            this.name       = name;
            this.priceCents = priceCents;
        }

        public String getId()    { return id; }
        public String getName()  { return name; }
        public int    getPrice() { return priceCents; }

        @Override
        public String toString() {
            return String.format("[%s] %-16s $%s", id, name, Money.fmt(priceCents));
        }
    }

    /** One physical slot — holds a product type and its current stock count. */
    static class Slot {
        private final Product product;
        private int quantity;

        public Slot(Product product, int quantity) {
            this.product  = product;
            this.quantity = quantity;
        }

        public Product getProduct()     { return product; }
        public int     getQuantity()    { return quantity; }
        public boolean isAvailable()    { return quantity > 0; }
        public void    decrement()      { if (quantity > 0) quantity--; }
        public void    restock(int qty) { this.quantity += qty; }
    }

    /** In-memory inventory — maps slotId → Slot. */
    static class Inventory {
        private final Map<String, Slot> slots = new LinkedHashMap<>();

        public void addSlot(Product product, int quantity) {
            slots.put(product.getId(), new Slot(product, quantity));
        }

        public void restock(String id, int qty) {
            require(id).restock(qty);
        }

        public boolean isKnown(String id)     { return slots.containsKey(id); }
        public boolean isAvailable(String id) { return isKnown(id) && slots.get(id).isAvailable(); }

        public Slot    getSlot(String id)     { return slots.get(id); }
        public Product getProduct(String id)  {
            Slot s = slots.get(id);
            return s == null ? null : s.getProduct();
        }

        public void dispense(String id)       { require(id).decrement(); }
        public Map<String, Slot> all()        { return Collections.unmodifiableMap(slots); }

        private Slot require(String id) {
            Slot s = slots.get(id);
            if (s == null) throw new IllegalArgumentException("Unknown product id: " + id);
            return s;
        }
    }

// ──────────────────────── STATE INTERFACE ────────────────────────────────────

    interface VendingMachineState {
        void selectProduct(String id);
        void insertCoin(Coin coin);
        void insertNote(Note note);
        void pressDispense();
        void cancel();
        String stateName();
    }

// ─────────────────────── CONTEXT  (Singleton) ────────────────────────────────

    /**
     * VendingMachineContext — State-pattern context AND the Singleton.
     * Holds all runtime state; every public action is delegated to currentState.
     */
    static class VendingMachineContext {

        /* ── Singleton (double-checked locking) ── */
        private static volatile VendingMachineContext INSTANCE;

        public static VendingMachineContext getInstance() {
            if (INSTANCE == null) {
                synchronized (VendingMachineContext.class) {
                    if (INSTANCE == null) INSTANCE = new VendingMachineContext();
                }
            }
            return INSTANCE;
        }

        /* ── Runtime fields ── */
        private final Inventory inventory     = new Inventory();
        private int             insertedCents = 0;
        private String          selectedId    = null;
        private int             revenueCents  = 0;

        /* ── State singletons (created once) ── */
        final VendingMachineState IDLE         = new IdleState(this);
        final VendingMachineState HAS_MONEY    = new HasMoneyState(this);
        final VendingMachineState DISPENSING   = new DispensingState(this);
        final VendingMachineState OUT_OF_STOCK = new OutOfStockState(this);

        private VendingMachineState currentState = IDLE;

        private VendingMachineContext() {}

        /* ── Public user actions → delegated to state ── */
        public void selectProduct(String id) { currentState.selectProduct(id); }
        public void insertCoin(Coin coin)    { currentState.insertCoin(coin); }
        public void insertNote(Note note)    { currentState.insertNote(note); }
        public void pressDispense()          { currentState.pressDispense(); }
        public void cancel()                 { currentState.cancel(); }

        /* ── State transitions ── */
        public void setState(VendingMachineState s) { currentState = s; }
        public VendingMachineState getState()        { return currentState; }

        /* ── Amount helpers ── */
        public void addAmount(int cents)  { insertedCents += cents; }
        public int  getInserted()         { return insertedCents; }
        public void clearInserted()       { insertedCents = 0; }

        /* ── Selection helpers ── */
        public String getSelectedId()            { return selectedId; }
        public void   setSelectedId(String id)   { this.selectedId = id; }

        /* ── Inventory (read-only access for states) ── */
        public Inventory getInventory()          { return inventory; }

        /* ── Revenue ── */
        public void addRevenue(int cents)        { revenueCents += cents; }
        public int  getTotalRevenue()            { return revenueCents; }
        public int  collectRevenue()             { int r = revenueCents; revenueCents = 0; return r; }

        /* ── Reset between transactions ── */
        public void resetTransaction() {
            insertedCents = 0;
            selectedId    = null;
            currentState  = IDLE;
        }
    }

// ──────────────────────── CONCRETE STATES ────────────────────────────────────

    static class IdleState implements VendingMachineState {
        private final VendingMachineContext ctx;
        IdleState(VendingMachineContext ctx) { this.ctx = ctx; }

        @Override
        public void selectProduct(String id) {
            Inventory inv = ctx.getInventory();
            if (!inv.isKnown(id)) {
                Display.show("❌  Product ID '" + id + "' does not exist.");
                return;
            }
            ctx.setSelectedId(id);
            if (!inv.isAvailable(id)) {
                ctx.setState(ctx.OUT_OF_STOCK);
                Display.show("⚠️   '" + inv.getProduct(id).getName() + "' is OUT OF STOCK. Choose another or cancel.");
                return;
            }
            Product p = inv.getProduct(id);
            Display.show("✅  Selected: " + p.getName()
                    + "  |  Price: $" + Money.fmt(p.getPrice())
                    + "  |  Please insert money.");
            ctx.setState(ctx.HAS_MONEY);
        }

        @Override public void insertCoin(Coin c)  { Display.show("⚠️   Please select a product first."); }
        @Override public void insertNote(Note n)  { Display.show("⚠️   Please select a product first."); }
        @Override public void pressDispense()     { Display.show("⚠️   Select a product and insert money first."); }
        @Override public void cancel()            { Display.show("ℹ️   Nothing to cancel — machine is idle."); }
        @Override public String stateName()       { return "IDLE"; }
    }

    static class HasMoneyState implements VendingMachineState {
        private final VendingMachineContext ctx;
        HasMoneyState(VendingMachineContext ctx) { this.ctx = ctx; }

        @Override public void selectProduct(String id) {
            Display.show("⚠️   Product already selected. Dispense or cancel first.");
        }

        @Override
        public void insertCoin(Coin coin) {
            ctx.addAmount(coin.getValue());
            printBalance(coin.name() + " (+$" + Money.fmt(coin.getValue()) + ")");
        }

        @Override
        public void insertNote(Note note) {
            ctx.addAmount(note.getValue());
            printBalance("$" + Money.fmt(note.getValue()) + " note");
        }

        @Override
        public void pressDispense() {
            Product p = ctx.getInventory().getProduct(ctx.getSelectedId());
            if (ctx.getInserted() < p.getPrice()) {
                Display.show("❌  Insufficient funds — Inserted: $" + Money.fmt(ctx.getInserted())
                        + "  |  Need: $" + Money.fmt(p.getPrice()));
                return;
            }
            ctx.setState(ctx.DISPENSING);
            ctx.pressDispense();    // forward to DispensingState immediately
        }

        @Override
        public void cancel() {
            int refund = ctx.getInserted();
            ctx.resetTransaction();
            Display.show("🔄  Cancelled — Refunding: $" + Money.fmt(refund));
        }

        @Override public String stateName() { return "HAS_MONEY"; }

        private void printBalance(String inserted) {
            Product p = ctx.getInventory().getProduct(ctx.getSelectedId());
            Display.show("💰  " + inserted
                    + "  |  Balance: $" + Money.fmt(ctx.getInserted())
                    + "  |  Required: $" + Money.fmt(p.getPrice()));
        }
    }

    static class DispensingState implements VendingMachineState {
        private final VendingMachineContext ctx;
        DispensingState(VendingMachineContext ctx) { this.ctx = ctx; }

        @Override
        public void pressDispense() {
            Product p      = ctx.getInventory().getProduct(ctx.getSelectedId());
            int     change = ctx.getInserted() - p.getPrice();

            ctx.getInventory().dispense(ctx.getSelectedId());
            ctx.addRevenue(p.getPrice());

            Display.show("🎉  Dispensing: " + p.getName());
            if (change > 0) {
                Display.show("💸  Change returned: $" + Money.fmt(change));
            }
            ctx.resetTransaction();
            Display.show("✅  Enjoy! Machine is ready.");
        }

        @Override public void selectProduct(String id) { busy(); }
        @Override public void insertCoin(Coin c)       { busy(); }
        @Override public void insertNote(Note n)       { busy(); }
        @Override public void cancel()                 { Display.show("⚠️   Cannot cancel — dispensing in progress."); }
        @Override public String stateName()            { return "DISPENSING"; }

        private void busy() { Display.show("⚠️   Please wait — dispensing in progress."); }
    }

    static class OutOfStockState implements VendingMachineState {
        private final VendingMachineContext ctx;
        OutOfStockState(VendingMachineContext ctx) { this.ctx = ctx; }

        @Override
        public void selectProduct(String id) {
            ctx.resetTransaction();
            Display.show("ℹ️   Switching selection…");
            ctx.selectProduct(id);   // re-enter from IdleState
        }

        @Override public void insertCoin(Coin c)  { Display.show("❌  Item out of stock — cancel or choose another."); }
        @Override public void insertNote(Note n)  { Display.show("❌  Item out of stock — cancel or choose another."); }
        @Override public void pressDispense()     { Display.show("❌  Cannot dispense — item is out of stock."); }

        @Override
        public void cancel() {
            int refund = ctx.getInserted();
            ctx.resetTransaction();
            if (refund > 0) Display.show("🔄  Refunding: $" + Money.fmt(refund));
            Display.show("🔄  Cancelled — machine is idle.");
        }

        @Override public String stateName() { return "OUT_OF_STOCK"; }
    }

// ───────────────────────── VIEW LAYER ────────────────────────────────────────

    class Display {

        public static void show(String msg) {
            System.out.println("  [DISPLAY]  " + msg);
        }

        public static void showInventory(Inventory inv) {
            System.out.println();
            System.out.println("  ╔═══════════════════════════════════════════════════╗");
            System.out.println("  ║          VENDING MACHINE  —  INVENTORY            ║");
            System.out.println("  ╠═══════════════════════════════════════════════════╣");
            inv.all().forEach((id, slot) -> {
                String stock = slot.isAvailable()
                        ? "Qty: " + slot.getQuantity()
                        : "OUT OF STOCK";
                System.out.printf(
                        "  ║  %-6s  %-16s  $%-7s  %-14s║%n",
                        "[" + id + "]",
                        slot.getProduct().getName(),
                        Money.fmt(slot.getProduct().getPrice()),
                        stock
                );
            });
            System.out.println("  ╚═══════════════════════════════════════════════════╝");
            System.out.println();
        }

        public static void showStatus(VendingMachineContext ctx) {
            System.out.println("  ┌─── Machine Status ──────────────────────────────────┐");
            System.out.printf ("  │  State     : %-36s│%n", ctx.getState().stateName());
            System.out.printf ("  │  Inserted  : $%-35s│%n", Money.fmt(ctx.getInserted()));
            System.out.printf ("  │  Selected  : %-36s│%n",
                    ctx.getSelectedId() != null ? ctx.getSelectedId() : "—");
            System.out.printf ("  │  Revenue   : $%-35s│%n", Money.fmt(ctx.getTotalRevenue()));
            System.out.println("  └─────────────────────────────────────────────────────┘");
            System.out.println();
        }
    }

// ─────────────────────── CONTROLLER LAYER ────────────────────────────────────

    static class VendingMachineController {
        private final VendingMachineContext ctx;

        public VendingMachineController(VendingMachineContext ctx) { this.ctx = ctx; }

        /* ── User actions ── */
        public void selectProduct(String id)  { ctx.selectProduct(id); }
        public void insertCoin(Coin coin)     { ctx.insertCoin(coin); }
        public void insertNote(Note note)     { ctx.insertNote(note); }
        public void dispense()                { ctx.pressDispense(); }
        public void cancel()                  { ctx.cancel(); }

        /* ── Admin actions ── */
        public void addProduct(Product p, int qty) {
            ctx.getInventory().addSlot(p, qty);
            Display.show("🔧 [ADMIN] Added product: " + p.getName() + "  x" + qty);
        }

        public void restock(String id, int qty) {
            try {
                ctx.getInventory().restock(id, qty);
                Display.show("🔧 [ADMIN] Restocked [" + id + "] +" + qty + " units.");
            } catch (Exception e) {
                Display.show("❌ [ADMIN] " + e.getMessage());
            }
        }

        public void collectRevenue() {
            int r = ctx.collectRevenue();
            Display.show("💰 [ADMIN] Collected: $" + Money.fmt(r));
        }

        public void showInventory() { Display.showInventory(ctx.getInventory()); }
        public void showStatus()    { Display.showStatus(ctx); }
    }

// ──────────────────────── ENTRY POINT ────────────────────────────────────────

    public class VendingMachineDemo {

        public static void main(String[] args) {

            VendingMachineContext       machine = VendingMachineContext.getInstance();
            VendingMachineController    ctrl    = new VendingMachineController(machine);

            // ── Seed inventory ──────────────────────────────────────────────────
            ctrl.addProduct(new Product("A1", "Coke",    150), 3);
            ctrl.addProduct(new Product("A2", "Pepsi",   150), 2);
            ctrl.addProduct(new Product("B1", "Water",   100), 5);
            ctrl.addProduct(new Product("B2", "Chips",   200), 0);  // intentionally OOS
            ctrl.addProduct(new Product("C1", "Juice",   175), 1);
            ctrl.addProduct(new Product("C2", "Coffee",  125), 4);
            ctrl.showInventory();

            // ── Scenario 1 : Happy path — exact change ──────────────────────────
            banner("SCENARIO 1  —  Happy path: exact change");
            ctrl.selectProduct("B1");           // Water  $1.00
            ctrl.insertCoin(Coin.QUARTER);
            ctrl.insertCoin(Coin.QUARTER);
            ctrl.insertCoin(Coin.QUARTER);
            ctrl.insertCoin(Coin.QUARTER);
            ctrl.dispense();

            // ── Scenario 2 : Insufficient funds → top-up → dispense ─────────────
            banner("SCENARIO 2  —  Insufficient funds, top-up, then dispense");
            ctrl.selectProduct("A1");           // Coke   $1.50
            ctrl.insertCoin(Coin.QUARTER);      // $0.25
            ctrl.insertCoin(Coin.QUARTER);      // $0.50
            ctrl.dispense();                    // ❌ not enough
            ctrl.insertNote(Note.ONE);          // $1.50 total
            ctrl.dispense();                    // ✅

            // ── Scenario 3 : Overpayment → change returned ───────────────────────
            banner("SCENARIO 3  —  Overpayment → change returned");
            ctrl.selectProduct("C1");           // Juice  $1.75
            ctrl.insertNote(Note.FIVE);         // $5.00  (overpay by $3.25)
            ctrl.dispense();

            // ── Scenario 4 : Cancel mid-transaction → refund ─────────────────────
            banner("SCENARIO 4  —  Cancel with refund");
            ctrl.selectProduct("A2");           // Pepsi  $1.50
            ctrl.insertNote(Note.ONE);          // $1.00
            ctrl.insertCoin(Coin.QUARTER);      // $1.25
            ctrl.cancel();                      // refund $1.25

            // ── Scenario 5 : Out-of-stock item ───────────────────────────────────
            banner("SCENARIO 5  —  Out-of-stock product");
            ctrl.selectProduct("B2");           // Chips — OOS
            ctrl.cancel();

            // ── Scenario 6 : OOS then switch to available item ───────────────────
            banner("SCENARIO 6  —  OOS then switch to another product");
            ctrl.selectProduct("B2");           // Chips  OOS
            ctrl.selectProduct("C2");           // Coffee $1.25 — available
            ctrl.insertNote(Note.ONE);          // $1.00
            ctrl.insertCoin(Coin.QUARTER);      // $1.25
            ctrl.dispense();                    // ✅

            // ── Scenario 7 : Invalid product ID ──────────────────────────────────
            banner("SCENARIO 7  —  Invalid product ID");
            ctrl.selectProduct("Z9");

            // ── Admin : Collect revenue & restock ────────────────────────────────
            banner("ADMIN  —  Revenue collection & restock");
            ctrl.collectRevenue();
            ctrl.restock("B2", 10);             // restock the chips
            ctrl.showInventory();
            ctrl.showStatus();
        }

        private static void banner(String title) {
            System.out.println("\n  ══════════  " + title + "  ══════════");
        }
    }

}

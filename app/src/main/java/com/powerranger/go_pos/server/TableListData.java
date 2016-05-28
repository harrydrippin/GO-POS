package com.powerranger.go_pos.server;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by harryhong on 16. 5. 27..
 */
public class TableListData {
    private int tableNumber;
    private String menu;
    private int price;

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static final Comparator<TableListData> ALPHA_COMPARATOR = new Comparator<TableListData>() {

        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(TableListData lhs, TableListData rhs) {
            return sCollator.compare(lhs.tableNumber, rhs.tableNumber);
        }
    };
}

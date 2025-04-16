package utils;

import java.util.List;
import model.*;
import parser.*;

public class DataLoader {

    // Modify these methods to accept file paths as arguments
    public static List<Region> loadRegions(String filePath) throws Exception {
        return RegionParser.parse(filePath);
    }

    public static List<Nation> loadNations(String filePath) throws Exception {
        return NationParser.parse(filePath);
    }

    public static List<Customer> loadCustomers(String filePath) throws Exception {
        return CustomerParser.parse(filePath);
    }

    public static List<Order> loadOrders(String filePath) throws Exception {
        return OrdersParser.parse(filePath);
    }

    public static List<LineItem> loadLineItems(String filePath) throws Exception {
        return LineItemParser.parse(filePath);
    }

    public static List<Supplier> loadSuppliers(String filePath) throws Exception {
        return SupplierParser.parse(filePath);
    }
}

package Models;

import Controllers.DatabaseController;
import Utilities.StatementTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Michael on 7/6/2018.
 */
public class Order {

    private int orderNum;
    private int storeId;
    private int customerId;
    private ArrayList<ProductQuantity> orderedProducts;

    public Order(int orderNum, int storeId, int customerId, ArrayList<ProductQuantity> orderedProducts){
        this.storeId = storeId;
        this.customerId = customerId;
        this.orderedProducts = orderedProducts;
        this.orderNum = orderNum;
    }

    //Queries//
    //TODO Find out if we want to get all of the products ordered with the intital query or do we want just the
    //TODO ordernum, storeid, and customerId to be selectable and when selected itll show all of the products purchased

    //get all columns for a customer
    public static ArrayList<Order> getOrdersByCustIdQuery(DatabaseController dbController, StatementTemplate stmtUtil, int id){

        Statement stmt = StatementTemplate.newNullStatement();
        ResultSet rs = null;

        String selectCustomer = "SELECT * FROM Orders WHERE userId =" + id;

        //create query statement
        try {
            stmt = StatementTemplate.connStatement(stmt);
        }catch(Exception e){
            System.out.println("Error Creating Fetch Statement for Order");
        }

        //execute and get results of query
        try {
            rs = dbController.ExecuteSelectQuery(stmt, selectCustomer);
        }catch(Exception e){
            System.out.println("Error Executing Query for Order");
            e.printStackTrace();
        }

        return parseResultSet(rs);
    }

    //get all orders for a store
    public static ResultSet getOrdersByStoreIdQuery(DatabaseController dbController, StatementTemplate stmtUtil, int id){

        Statement stmt = StatementTemplate.newNullStatement();
        ResultSet rs = null;

        String selectCustomer = "SELECT * FROM Orders WHERE storeId =" + id;

        //create query statement
        try {
            stmt = StatementTemplate.connStatement(stmt);
        }catch(Exception e){
            System.out.println("Error Creating Fetch Statement for Order");
        }

        //execute and get results of query
        try {
            rs = dbController.ExecuteSelectQuery(stmt, selectCustomer);
        }catch(Exception e){
            System.out.println("Error Executing Query for Order");
            e.printStackTrace();
        }

        //return parseResultSet(rs); //used for when returning an arraylist
        return rs;
    }

    //Utils//

    //create cust object based off query
    private static ArrayList<Order> parseResultSet(ResultSet rs) {

        ArrayList<Order> orderHistory = new ArrayList<Order>();

        if (rs != null) {

            try {

                while (rs.next()) {
                    int orderNum = Integer.parseInt( rs.getString("orderId") );
                    int custId = Integer.parseInt( rs.getString("userId") );
                    int storeId = Integer.parseInt( rs.getString("shopId") );

                    Order order = new Order(orderNum,storeId,custId, null);
                    orderHistory.add(order);

                }
            } catch (Exception e) {
                System.out.print("Error Building Order" + '\n');
                e.printStackTrace();
            }
        }

        return orderHistory;
    }



    public String viewOrderDetails(){
        String orderDetails = "Order Number:" + this.orderNum +'\n';

        for(ProductQuantity product:orderedProducts){
            //orderDetails += product.getProduct().getName()+'x'+product.getQuantity();
        }

        return orderDetails;
    }
}

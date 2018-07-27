package Models;

import Controllers.DatabaseController;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Michael on 7/6/2018.
 */
public class Cart
{
    public Integer CustomerId;
    private int StoreId;
    private LinkedHashMap<String, CartEntry> Contents;
    private LinkedHashMap<String, CartEntry> StoreContents;
    private int itemcount;
    private float totalCost;

    public Cart(int StoreId)
    {

        this.StoreId = StoreId;
        CustomerId = null;
        itemcount = 0;
        totalCost = 0.0f;
        Contents = new LinkedHashMap<>();
        StoreContents = CartEntry.RStoContents(DatabaseController.SelectQuery
                (("Select Product.UPC, Product.Name, Product.Brand, Product.Price, Inventory.Quantity from product "
                  + "join inventory on Product.UPC = inventory.productUPC where inventory.storeId="
                  + String.valueOf(StoreId)), false));

    }

    public ArrayList<CartEntry> getCartItemDetails()
    {

        ArrayList<CartEntry> conts = new ArrayList<>();
        conts.addAll(Contents.values());
        return conts;
    }

    public boolean addItem(String UPC, int quantity)
    {

        if (StoreContents.containsKey(UPC))
        {
            CartEntry item = StoreContents.get(UPC);
            int desiredQuantity = quantity + (Contents.containsKey(UPC) ? Contents.get(UPC).Quantity : 0);
            if (item.Quantity >= desiredQuantity)
            {
                CartEntry entry = new CartEntry();
                entry.UPC = UPC;
                entry.Name = item.Name;
                entry.Brand = item.Brand;
                entry.Price = item.Price;
                entry.Quantity = desiredQuantity;
                Contents.put(UPC, entry);
                itemcount += quantity;
                totalCost += quantity * entry.Price;
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(String UPC, int quantity)
    {

        if (Contents.containsKey(UPC))
        {
            CartEntry item = Contents.get(UPC);
            int desiredQuantity = item.Quantity - quantity;
            if (desiredQuantity > 0)
            {
                Contents.get(UPC).Quantity = desiredQuantity;
                totalCost -= item.Price * quantity;
                itemcount -= quantity;
                return true;
            }
            else if (desiredQuantity == 0)
            {
                totalCost -= item.Price * quantity;
                itemcount -= quantity;
                Contents.remove(UPC);
                return true;
            }
        }
        return false;
    }

    public boolean CheckOut()
    {

        return DatabaseController.createOrder(StoreId, CustomerId, getCartItemDetails());
    }

    public float getTotalCost()
    {

        return totalCost;
    }

    public int getItemcount()
    {

        return itemcount;
    }
}

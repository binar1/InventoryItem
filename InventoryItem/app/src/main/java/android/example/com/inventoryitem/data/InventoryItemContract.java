package android.example.com.inventoryitem.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by binar on 20/11/2017.
 */

public class InventoryItemContract {
    public  InventoryItemContract()
    {}

    public final static class ItemEntry
    {
        public static final String TableName = "Inventory";
        public static final String ID = BaseColumns._ID;
        public static final String ItemName = "ItemName";
        public static final String ItemKind = "ItemKind";
        public static final String ItemQuantity = "ItemQuantity";
        public static final String ItemPrice = "ItemPrice";
        public static final String imge = "imge";
    }

}

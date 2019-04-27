package android.example.com.inventoryitem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.ID;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.ItemKind;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.ItemName;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.ItemPrice;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.ItemQuantity;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.TableName;
import static android.example.com.inventoryitem.data.InventoryItemContract.ItemEntry.imge;

/**
 * Created by binar on 20/11/2017.
 */

public class InventoryItemDbHelper extends SQLiteOpenHelper {

    public final static String Database_Name = "Items";
    public final static int Database_Vertion = 1;
    SQLiteDatabase database;

    public InventoryItemDbHelper(Context context) {
        super(context, Database_Name, null, Database_Vertion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String Table = "create table " + TableName + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ItemName + " TEXT," + ItemKind + " TEXT," + ItemQuantity + " INTEGER," + ItemPrice + " INTEGER," + imge + " BLO);";
        sqLiteDatabase.execSQL(Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insert(String ItemName, String kind, int quantity, int price, byte[] image) {
        database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryItemContract.ItemEntry.ItemName, ItemName);
        contentValues.put(InventoryItemContract.ItemEntry.ItemKind, kind);
        contentValues.put(InventoryItemContract.ItemEntry.ItemQuantity, quantity);
        contentValues.put(InventoryItemContract.ItemEntry.ItemPrice, price);
        contentValues.put(InventoryItemContract.ItemEntry.imge, image);


        long id = database.insert(InventoryItemContract.ItemEntry.TableName, null, contentValues);
        return id != -1;
    }

    public Cursor getData(long id) {
        database = this.getReadableDatabase();

        String[] projection = {
                InventoryItemContract.ItemEntry.ID,
                InventoryItemContract.ItemEntry.ItemName,
                InventoryItemContract.ItemEntry.ItemKind,
                InventoryItemContract.ItemEntry.ItemQuantity,
                InventoryItemContract.ItemEntry.ItemPrice,
                InventoryItemContract.ItemEntry.imge
        };

        String selection = InventoryItemContract.ItemEntry.ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = database.query(
                InventoryItemContract.ItemEntry.TableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    public Cursor GetAllData() {
        database = this.getReadableDatabase();
        return database.query(InventoryItemContract.ItemEntry.TableName, null, null, null, null, null, null);
    }


    public boolean update(long id, String ItemName, String kind, int quantity, int price, byte[] image) {
        database = this.getWritableDatabase();

        String selection = InventoryItemContract.ItemEntry.ID + " = ?";
        String[] selectionArgs = {id + ""};

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryItemContract.ItemEntry.ItemName, ItemName);
        contentValues.put(InventoryItemContract.ItemEntry.ItemKind, kind);
        contentValues.put(InventoryItemContract.ItemEntry.ItemQuantity, quantity);
        contentValues.put(InventoryItemContract.ItemEntry.ItemPrice, price);
        contentValues.put(InventoryItemContract.ItemEntry.imge, image);

        long count = database.update(InventoryItemContract.ItemEntry.TableName, contentValues, selection, selectionArgs);

        return count != -1;
    }

    public void delete(long id) {
        database = this.getWritableDatabase();
        String selection = InventoryItemContract.ItemEntry.ID + " = ?";
        String[] selectionArgs = {id + ""};
        database.delete(InventoryItemContract.ItemEntry.TableName, selection, selectionArgs);
    }

    public boolean decreaseQuantity(long id) {
        database = this.getWritableDatabase();

        String selection = InventoryItemContract.ItemEntry.ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor c = this.getData(id);
        ContentValues contentValues = new ContentValues();

        while (c.moveToNext()) {
            int quantity = c.getInt(c.getColumnIndex(InventoryItemContract.ItemEntry.ItemQuantity));
            if (quantity > 0) {
                contentValues.put(InventoryItemContract.ItemEntry.ItemQuantity, (quantity - 1));
            }
        }
        long count = database.update(InventoryItemContract.ItemEntry.TableName, contentValues, selection, selectionArgs);

        return count != -1;
    }
}

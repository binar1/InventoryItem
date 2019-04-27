package android.example.com.inventoryitem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.inventoryitem.data.InventoryItemContract;
import android.example.com.inventoryitem.data.InventoryItemDbHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by binar on 20/11/2017.
 */

public class InventoryItemCursorAdapter extends CursorAdapter {
    TextView name, quantity, price;
    ImageView Image;
    Button sell;
    SQLiteDatabase database;
    InventoryItemDbHelper db;
    Context mContext;

    public InventoryItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        name = view.findViewById(R.id.name);
        quantity = view.findViewById(R.id.Price);
        price = view.findViewById(R.id.Count);
        Image = view.findViewById(R.id.show_imge);
        sell = view.findViewById(R.id.sell);

        final long itemId = cursor.getLong(cursor.getColumnIndex(InventoryItemContract.ItemEntry.ID));
        String ItemName = cursor.getString(cursor.getColumnIndex(InventoryItemContract.ItemEntry.ItemName));
        int Quantity = cursor.getInt(cursor.getColumnIndex(InventoryItemContract.ItemEntry.ItemQuantity));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryItemContract.ItemEntry.imge));
        int Price = cursor.getInt(cursor.getColumnIndex(InventoryItemContract.ItemEntry.ItemPrice));

        name.setText("Name: " + ItemName);
        price.setText("Price:" + "$" + Price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        Image.setImageBitmap(bitmap);
        quantity.setText("Count: " + Quantity);

        if (Quantity <= 0) {
            sell.setEnabled(false);
        }


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity) {
                    InventoryItemDbHelper db = new InventoryItemDbHelper(context);
                    db.decreaseQuantity(itemId);
                    ((MainActivity) mContext).AllOfList();
                }
            }
        });

    }


}



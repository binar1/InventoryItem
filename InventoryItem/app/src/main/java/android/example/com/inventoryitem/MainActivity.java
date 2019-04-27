package android.example.com.inventoryitem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.inventoryitem.data.InventoryItemContract;
import android.example.com.inventoryitem.data.InventoryItemDbHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    InventoryItemCursorAdapter adapter;
    FloatingActionButton fab;
    SQLiteDatabase database;
    InventoryItemDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_item);
        View view = findViewById(R.id.empty_view);
        listView.setEmptyView(view);
        fab = findViewById(R.id.insert);
        db = new InventoryItemDbHelper(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditItem.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("choni");
                Intent intent = new Intent(MainActivity.this, EditItem.class);
                Cursor c = (Cursor) adapterView.getItemAtPosition(i);
                long id = c.getInt(c.getColumnIndex(InventoryItemContract.ItemEntry.ID));
                intent.putExtra("itemID", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AllOfList();
    }

    public void AllOfList() {
        Cursor c = db.GetAllData();
        InventoryItemCursorAdapter adapter = new InventoryItemCursorAdapter(MainActivity.this, c);
        listView.setAdapter(adapter);
    }

}

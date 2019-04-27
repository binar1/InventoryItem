package android.example.com.inventoryitem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.example.com.inventoryitem.data.InventoryItemContract;
import android.example.com.inventoryitem.data.InventoryItemDbHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class EditItem extends AppCompatActivity {
    EditText name, kind, price;
    Button save, uploadImage, delete, decreaseButton, increaseButton, orderButton;
    ImageView editImage, fixedImage;
    Boolean Mode;
    TextView quantity;
    long itemId;
    ImageView AddRes, editRes;
    InventoryItemDbHelper db = new InventoryItemDbHelper(this);
    public static final String KEY_ITEM_ID = "itemID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mode = false;
        setContentView(R.layout.activity_edit__item);
        name = findViewById(R.id.change_name);
        kind = findViewById(R.id.change_category);
        quantity = findViewById(R.id.change_quantity);
        price = findViewById(R.id.change_price);
        save = findViewById(R.id.edit_button);
        delete = findViewById(R.id.delete_button);
        editImage = findViewById(R.id.editImage);
        uploadImage = findViewById(R.id.uploadImage);
        orderButton = (Button) findViewById(R.id.order);
        increaseButton = (Button) findViewById(R.id.increase_button);
        decreaseButton = (Button) findViewById(R.id.decrease_button);
        save.setText("save");
        delete.setVisibility(View.INVISIBLE);
        orderButton.setVisibility(View.INVISIBLE);
        editImage.setVisibility(View.GONE);
        fixedImage = findViewById(R.id.fixedImage);
        fixedImage.setVisibility(View.GONE);


        if (getIntent().hasExtra(KEY_ITEM_ID)) {
            Mode = true;
            orderButton.setVisibility(View.VISIBLE);
            itemId = getIntent().getLongExtra(KEY_ITEM_ID, 0);
            delete.setVisibility(View.VISIBLE);
            fixedImage.setVisibility(View.VISIBLE);
            save.setText("edit");
            editImage.setVisibility(View.GONE);
            Cursor c = db.getData(itemId);
            while (c.moveToNext()) {
                byte[] imageBytes = c.getBlob(c.getColumnIndex(InventoryItemContract.ItemEntry.imge));
                String Name = c.getString(c.getColumnIndex(InventoryItemContract.ItemEntry.ItemName));
                String Kind = c.getString(c.getColumnIndex(InventoryItemContract.ItemEntry.ItemKind));
                int Price = c.getInt(c.getColumnIndex(InventoryItemContract.ItemEntry.ItemPrice));
                int Quantity = c.getInt(c.getColumnIndex(InventoryItemContract.ItemEntry.ItemQuantity));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                fixedImage.setImageBitmap(bitmap);
                name.setText(Name);
                price.setText(Price + "");
                quantity.setText(Quantity + "");
                kind.setText(Kind);


            }

        }
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                ActionEmail(Name);
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                editImage.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Mode) {
                    saveData();
                } else {
                    Updatee();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = AskOption();
                dialog.show();
            }
        });
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(quantity);
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(quantity);
            }
        });
    }

    public boolean checkEntries() {
        if (name.getText().toString().equals("") || price.getText().toString().equals("") || quantity.getText().toString().equals("") || editImage.getDrawable() == null) {
            Toast.makeText(this, "Please fill All of them", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkBeforeUpdate() {
        if (name.getText().toString().equals("") || price.getText().toString().equals("") || quantity.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill All of them", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }


    public void saveData() {
        if (checkEntries()) {
            try {
                BitmapDrawable drawable = (BitmapDrawable) editImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                String title = name.getText().toString();
                int pric = Integer.valueOf(price.getText().toString());
                int quantite = Integer.valueOf(quantity.getText().toString());
                String kind2 = kind.getText().toString();
                Boolean state;
                state = db.insert(title, kind2, quantite, pric, imageBytes);
                if (state) {
                    Toast.makeText(this, "insert is success full", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditItem.this, MainActivity.class));
                } else {
                    Toast.makeText(this, "You have error", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public void Updatee() {

        if (checkBeforeUpdate()) {
            try {
                System.out.println("chakiii");
                Bitmap bitmap;
                byte[] imageBytes;
                if (editImage.getDrawable() == null) {
                    BitmapDrawable drawable = (BitmapDrawable) fixedImage.getDrawable();
                    bitmap = drawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageBytes = stream.toByteArray();
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) editImage.getDrawable();
                    bitmap = drawable.getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageBytes = stream.toByteArray();
                }
                String title = name.getText().toString();
                int pric = Integer.valueOf(price.getText().toString());
                int quantite = Integer.valueOf(quantity.getText().toString());
                String kind2 = kind.getText().toString();
                Boolean state;
                System.out.println("update");
                state = db.update(itemId, title, kind2, quantite, pric, imageBytes);

                if (state) {
                    Toast.makeText(this, "You are succesully update", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditItem.this, MainActivity.class));
                } else {
                    Toast.makeText(this, "You are failed in update", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    public void increaseQuantity(TextView editText) {
        if (Number(editText)) {
            int number = Integer.parseInt(editText.getText().toString());
            editText.setText((number + 1) + "");
        } else {
            editText.setText(0 + "");
        }
    }

    public void decreaseQuantity(TextView editText) {
        if (Number(editText)) {
            int number = Integer.parseInt(editText.getText().toString());
            if (number > 0) {
                editText.setText((number - 1) + "");
            }
        } else {
            editText.setText(0 + "");
        }
    }

    public void ActionEmail(String Item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order");
        intent.putExtra(Intent.EXTRA_TEXT, "I want order some more of" + Item);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send Email"));
            ;
        }
    }

    public boolean Number(TextView editText) {
        String regexStr = "^[0-9]*$";
        String text = editText.getText().toString();
        if (!text.equals("") && text.trim().matches(regexStr)) {
            return true;
        }

        return false;
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Delete))
                .setMessage(getString(R.string.Ask))

                .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.delete(itemId);
                        Toast.makeText(EditItem.this, getString(R.string.AfterDeleteMesssage), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditItem.this, MainActivity.class));
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            editImage.setImageBitmap(imageBitmap);
        }
    }

}

package me.henrylai.inventory.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import me.henrylai.inventory.R;
import me.henrylai.inventory.data.Item;
import me.henrylai.inventory.data.SQLiteDBHandler;

public class AddItemActivity extends AppCompatActivity {

    private Button mAddItemButton;
    private EditText mNameField;
    private EditText mValueField;
    private EditText mConditionField;
    private EditText mDescriptionField;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final SQLiteDBHandler DBhandler = new SQLiteDBHandler(this);
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");

        mAddItemButton = (Button) findViewById(R.id.item_add_button);
        mNameField = (EditText) findViewById(R.id.item_name_field);
        mValueField = (EditText) findViewById(R.id.item_value_field);
        mConditionField = (EditText) findViewById(R.id.item_condition_field);
        mDescriptionField = (EditText) findViewById(R.id.item_description_field);

        mAddItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    // Create an item to add
                    Item newItem = new Item(mNameField.getText().toString(),
                            Double.parseDouble(mValueField.getText().toString()),
                            mConditionField.getText().toString(), mDescriptionField.getText().toString());

                    // Add item to local SQLite DB
                    DBhandler.addItem(newItem);
                    DBhandler.close();

                    // Move back to viewing inventory after adding an item
                    Intent backToViewInventory = new Intent(AddItemActivity.this, ViewItemsActivity.class);
                    backToViewInventory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(AddItemActivity.this, "Item (probably) successfully added! ", Toast.LENGTH_SHORT).show();
                    startActivity(backToViewInventory);
                }
            }
        });
    }

    private boolean validateFields() {
        boolean result = true;
        for (Item item : itemList) {
            if (item.getmName().equals(mNameField.getText().toString())) {
                result = false;
                Toast.makeText(AddItemActivity.this, "There already exists an item with this name!", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }
}

package me.henrylai.inventory.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.henrylai.inventory.R;
import me.henrylai.inventory.data.Item;
import me.henrylai.inventory.data.SQLiteDBHandler;

public class ItemDetailsActivity extends AppCompatActivity {

    private int mItemID;
    private TextView mNameDetail;
    private TextView mValueDetail;
    private TextView mConditionDetail;
    private TextView mDescriptionDetail;
    private Button mEditButton;
    private SQLiteDBHandler mDBhandler;
    private Item selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mDBhandler = new SQLiteDBHandler(this);
        mNameDetail = (TextView)findViewById(R.id.details_item_name);
        mValueDetail = (TextView)findViewById(R.id.details_item_value);
        mConditionDetail = (TextView)findViewById(R.id.details_item_condition);
        mDescriptionDetail = (TextView)findViewById(R.id.details_item_description);
        mEditButton = (Button) findViewById(R.id.details_edit_button);

        // GETS THE LIST OF ALL ITEMS
        ArrayList<Item> itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");
        String selectedItemName = getIntent().getStringExtra("selected");
        selectedItem = new Item();
        // Find the selected item from the list of items!
        for (Item item : itemList) {
            if (item.getmName().equals(selectedItemName)) {
                // found, assign the item as selectedItem.
                selectedItem = item;
                mItemID = item.getmId();
            }
        }

        // populate UI with details about selectedItem.
        mNameDetail.setText(        "Name: " + selectedItem.getmName());
        mValueDetail.setText(       "Value: " + selectedItem.getmValue() + "");
        mConditionDetail.setText(   "Condition: " + selectedItem.getmCondition());
        mDescriptionDetail.setText( "Description: " + selectedItem.getmDescription());

        mEditButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsActivity.this, EditItemActivity.class);
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
        });
    }

    public void deleteItem(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDBhandler.deleteItem(selectedItem);
                        Toast.makeText(ItemDetailsActivity.this, "Item deleted! ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ItemDetailsActivity.this, ViewItemsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

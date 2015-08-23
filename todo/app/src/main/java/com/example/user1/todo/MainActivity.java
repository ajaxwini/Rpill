package com.example.user1.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity{

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsadapter;
    private ListView lvItems;

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));

        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems=(ListView) findViewById(R.id.lvItems);
        items= new ArrayList<String>();
        readItems();
        itemsadapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsadapter);
        setupListViewListener();
    }

    public void onADDItem(View v)
    {
        EditText etNewItem=(EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
         Boolean a =itemText.isEmpty();
        if(a==true){
            Toast.makeText(getBaseContext(),"Please Enter the item in the textbox",Toast.LENGTH_SHORT).show();

        }
        else {
            String datetimestamp= DateFormat.getDateTimeInstance().format(new Date());
            itemsadapter.add(itemText + "\n" + datetimestamp);
            etNewItem.setText("");
            writeItems();
        }

       }

    private void setupListViewListener()
    {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                                           {
                                               @Override
                                               public boolean onItemLongClick(AdapterView<?> adapter,
                                                                              View item, final int pos, long id) {
                                                  AlertDialog.Builder alertbox= new AlertDialog.Builder(item.getContext());
                                                           alertbox.setTitle("Delete entry")
                                                           .setMessage("Are you sure you want to delete this entry?")
                                                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   items.remove(pos);
                                                                   itemsadapter.notifyDataSetChanged();
                                                                   writeItems();
                                                                   // continue with delete
                                                               }
                                                           })
                                                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   // do nothing
                                                               }
                                                           })
                                                           .setIcon(android.R.drawable.ic_dialog_alert)
                                                           .show();
                                                   return true;
                                               }
                                           }
        );
}

}

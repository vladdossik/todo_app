package com.example.todo_app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import static com.example.todo_app.Databasehelper.DB_name;

public class todohelper extends AppCompatActivity {
     ForNewDb mDatabaseHelper;
    private SwipeMenuListView mListView;
    private ListView list;
    EditText edittodo;
     ArrayList<String> listData;
     ArrayList<String> donelist;
     public static int value;
     public static  int theme;
    final Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeColors(this);
        if(theme==1){
            setTheme(R.style.darktheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.todolayout);
        mListView = (SwipeMenuListView) findViewById(R.id.todolist);
        list = (ListView) findViewById(R.id.tododone);
        mDatabaseHelper = new ForNewDb(this);
        edittodo = (EditText) findViewById(R.id.newtodo);
        final ArrayAdapter<String> adapter;
        edittodo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        mDatabaseHelper.Add(edittodo.getText().toString());
                        edittodo.setText("");
                        populateListView();
                        return true;
                    }
                return false;
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                editItem.setWidth(170);
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        LayoutInflater li = LayoutInflater.from(context);
                        View vview = li.inflate(R.layout.add_goal, null);
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                        mDialogBuilder.setView(vview);
                        final EditText userInput = (EditText) vview.findViewById(R.id.input_text);
                        userInput.setTextColor(Color.WHITE);
                        mDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                final char dm = (char) 34;
                                                String name = dm + listData.get(position) + dm;
                                                mDatabaseHelper.delete(name);
                                                String name_db = userInput.getText().toString();
                                                name_db.replace(" ", "_");
                                                mDatabaseHelper.Add(name_db);
                                                populateListView();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = mDialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.grey)));
                        alertDialog.show();
                        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white));
                        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white));
                        break;
                    case 1:
                        final char dm = (char) 34;
                        String name = dm + listData.get(position) + dm;
                        mDatabaseHelper.delete(name);
                        populateListView();
                        break;
                }
                return false;
            }
        });

        populateListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char dm=(char)34;
                mDatabaseHelper.replace(dm+listData.get(position)+dm,"1");
populateListView();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char dm=(char)34;
                mDatabaseHelper.replace(dm+donelist.get(position)+dm,"0");
                populateListView();
            }
        });
    }
    public void populateListView() {
        value=0;
        Cursor data = mDatabaseHelper.getData();
        listData = new ArrayList<>();
        donelist = new ArrayList<>();
        while (data.moveToNext()) {
            if (!data.getString(2).contains("1")) {

                listData.add(data.getString(1));
            } else {
                donelist.add(data.getString(1));
            }
        }

        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.multiple_choice, listData);
        mListView.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, R.layout.done_list, donelist);
        list.setAdapter(adapter);
        if(listData.size()==0&&donelist.size()>listData.size()){
           String name=ForNewDb.DB_name;
         MainActivity.rep(name);


        }
    }
    private void toastMessage(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent intent=new Intent(todohelper.this,MainActivity.class);
       startActivity(intent);
    }
}

package com.example.todo_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
public class todohelper extends AppCompatActivity {
    ForNewDb mDatabaseHelper;
    private SwipeMenuListView listview_todo;
    EditText edittodo;
    ArrayAdapter adapter;
    ArrayList<String> listData=new ArrayList<>();
    final Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);
        mDatabaseHelper = new ForNewDb(this);
        listview_todo = (SwipeMenuListView) findViewById(R.id.todolist);
        populateListView();
                edittodo=(EditText) findViewById(R.id.newtodo);
                edittodo.setOnKeyListener(  new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                mDatabaseHelper.Add( edittodo.getText().toString());
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
                SwipeMenuItem doneItem = new SwipeMenuItem(
                        getApplicationContext());
                doneItem.setBackground(new ColorDrawable(getColor(R.color.green)));
                doneItem.setWidth(170);
                doneItem.setIcon(R.drawable.ic_done);
                menu.addMenuItem(doneItem);
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
        listview_todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               char dm = (char) 34;
                String name= dm+listData.get(position)+dm;
                mDatabaseHelper.replace(name);
                listview_todo.setItemChecked(position,true);
                populateListView();
            }
        });
        listview_todo.setMenuCreator(creator);
        listview_todo.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                char dm = (char) 34;
                switch (index) {
                    case 0:
                          dm = (char) 34;
                        String name= dm+listData.get(position)+dm;
                        mDatabaseHelper.delete(name);
                        populateListView();
                        if(listData.size()==0)
                        {
                            toastMessage("Цель достигнута");
                            String del= ForNewDb.DB_name;
                            context.deleteDatabase(del);
                            del=dm+ForNewDb.DB_name+dm;
                            try{
                                MainActivity.delete(del);
                            }
                            catch (Exception e) {
                            }
                            Intent intent=new Intent(todohelper.this,MainActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 1:
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
                                            public void onClick(DialogInterface dialog,int id) {
                                                final char dm = (char) 34;
                                                String name= dm+listData.get(position)+dm;
                                                mDatabaseHelper.delete(name);
                                                mDatabaseHelper.Add(userInput.getText().toString());
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
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
                        alertDialog.show();
                        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white));
                        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white));
                        break;
                    case 2:
                          dm = (char) 34;
                         name= dm+listData.get(position)+dm;
                        mDatabaseHelper.delete(name);
                        populateListView();
                        if(listData.size()==0)
                        {
                            toastMessage("Цель достигнута");
                            String del= ForNewDb.DB_name;
                            context.deleteDatabase(del);
                            del=dm+ForNewDb.DB_name+dm;
                            try{
                            MainActivity.delete(del);
                            }
                            catch (Exception e) {
                            }
                            Intent intent=new Intent(todohelper.this,MainActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            }
        });
    }
    public void populateListView() {
        Cursor data = mDatabaseHelper.getData();
        listData.clear();
        while(data.moveToNext()) {
                listData.add(data.getString(1));
            }
      adapter = new ArrayAdapter<>(this,R.layout.multiple_choice, listData);
        listview_todo.setAdapter(adapter);
    }
    private void toastMessage(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}


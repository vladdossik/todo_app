package com.example.todo_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class todohelper extends AppCompatActivity {
    ForNewDb mDatabaseHelper;
    private ListView listview_todo;
    EditText edittodo;
    ArrayAdapter adapter;
    ArrayList<String> listData=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);
        mDatabaseHelper = new ForNewDb(this);
        listview_todo = (ListView) findViewById(R.id.todolist);
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
        listview_todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     mDatabaseHelper.replace(listData.get(position));
                    populateListView();
            }
        });
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        listData.clear();

        while(data.moveToNext()) {
           // if(data.getString(2).equals("1"))
           //     mListView.setItemChecked(data.getPosition(),true);
            listData.add( data.getString(1)+" | "+data.getString(2) );
        }
         adapter = new ArrayAdapter<>(this, R.layout.multiple_choice, listData);
        listview_todo.setAdapter(adapter);
    }


}

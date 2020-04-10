package com.example.todo_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class todohelper extends AppCompatActivity {
    ForNewDb mDatabaseHelper;
    private ListView mListView;
    EditText edittodo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);

        mListView = (ListView) findViewById(R.id.todolist);
        mDatabaseHelper = new ForNewDb(this);
        final ArrayList<String> todo = new ArrayList<>();
edittodo=(EditText) findViewById(R.id.newtodo);
        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, todo);
        mListView.setAdapter(adapter);
        populateListView();
        edittodo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER|| keyCode==KeyEvent.ACTION_DOWN) {
                        mDatabaseHelper.Add( edittodo.getText().toString());
                        edittodo.setText("");
                        return true;
                    }
                return false;
            }
        });
        populateListView();
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()) {
            listData.add( data.getString(1) );
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listData);
        mListView.setAdapter(adapter);
    }


}

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
    private ListView mListView;
    EditText edittodo;
    ArrayList<String> listData ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);
         mListView = (ListView) findViewById(R.id.todolist);
        final ArrayList<String> listData = new ArrayList<>();
        mDatabaseHelper = new ForNewDb(this);
            edittodo=(EditText) findViewById(R.id.newtodo);
                final ArrayAdapter<String> adapter;
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray sp=mListView.getCheckedItemPositions();
                for(int i=0;i < listData.size();i++)
                {
                    if(sp.get(i))
                        mDatabaseHelper.replace(listData.get(position));
                }

            }
        });
        populateListView();
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        listData = new ArrayList<>();
        listData.clear();
        SparseBooleanArray sp=mListView.getCheckedItemPositions();
        while(data.moveToNext()) {
            if(data.getString(2).equals("1"))
            {
               mListView.setItemChecked(data.getPosition(),true);
            }
            listData.add( data.getString(1)+" | "+data.getString(2) );
        }
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.multiple_choice, listData);


        mListView.setAdapter(adapter);
    }


}

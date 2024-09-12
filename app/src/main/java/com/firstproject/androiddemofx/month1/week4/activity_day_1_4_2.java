package com.firstproject.androiddemofx.month1.week4;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class activity_day_1_4_2 extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private RadioGroup radioGroup;
    private Button buttonSave;
    private Button buttonLoad;
    private MyDatabaseHelper databaseHelper;
    private String FILENAME = "test.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day142);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);
        databaseHelper = new MyDatabaseHelper(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    private void saveData() {
        String inputText = editText.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (R.id.radioSharePreference == selectedId) {
            saveToSharedPreferences(inputText);
        } else if (R.id.radioSQLite == selectedId) {
            saveToSQLite(inputText);
        } else if (R.id.radioFileIO == selectedId) {
            saveToFileIO(inputText);
        }
    }

    private void saveToSharedPreferences(String text) {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedText", text);
        boolean success = editor.commit();
        if (success) {
            Toast.makeText(this, "Saved to SharedPreferences successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save to SharedPreferences", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToSQLite(String text) {
        if (databaseHelper.isTableExists("person_tb")) {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("text_data", text);
            database.insert("person_tb", null, values);
            Toast.makeText(this, "Saved to SQLite successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save to SQLite", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToFileIO(String text) {
        try {
            // 实例化文件输入流，指定写数据路径
            FileOutputStream write = openFileOutput(FILENAME,
                    MODE_PRIVATE);
            // 声明字节数组，用于把字符串转化成字节数据
            byte[] b = text.getBytes();
            // 写数据
            write.write(b);
            // 关闭流
            write.close();
            // 提示保存数据成功
            Toast.makeText(this, "Saved to FileIO successfully", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "保存数据成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to save to FileIO", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadData() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String loadText="";
        String tar="";
        if (selectedId == R.id.radioSharePreference) {
            loadText = loadFromSharedPreferences();
            tar="SharePreference";
        } else if (selectedId == R.id.radioSQLite) {
            loadText = loadFromSQLite();
            tar="SQLite";
        } else if (selectedId == R.id.radioFileIO) {
            loadText = loadFromFileIO();
            tar="FileIO";
        }
        if (loadText != null && loadText!="") {
            textView.setText(loadText);
            Toast.makeText(this, "Loaded from "+tar+" successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to load from "+tar, Toast.LENGTH_SHORT).show();
        }
    }

    private String loadFromSQLite() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor=database.query("person_tb",null,null,null,null,null,null);
        String textData="";
        if (cursor!= null && cursor.moveToFirst()) {
            StringBuilder data = new StringBuilder();
            do {
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                textData = cursor.getString(cursor.getColumnIndexOrThrow("text_data"));
//                data.append("ID: ").append(id).append(", Text: ").append(textData).append("\n");
                return textData;
            } while (cursor.moveToNext());
            // 将查询到的数据显示在TextView上
//            textView.setText(data.toString());
        }
        // 关闭游标
        cursor.close();
        // 关闭数据库
        database.close();
        return textData;
    }

    private String loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("savedText", null);
    }

    private String loadFromFileIO(){
        // 用于把byte数组转化成字符串
        String str = null;
        try {
            // 实例化文件输入流，指定读数据路径
            FileInputStream read = openFileInput(FILENAME);
            // 声明字节型数组，用于缓冲读取的数据
            byte[] b = new byte[1024];
            // 定义长度，如果文件中还有数据则继续读
            int len = 0;
            while ((len = read.read(b)) > 0) {
                str = new String(b);// 把字节数组转化成字符串
            }
            // 关闭流
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把转化后的字符串赋值到TextView中
        return str;
    }
}
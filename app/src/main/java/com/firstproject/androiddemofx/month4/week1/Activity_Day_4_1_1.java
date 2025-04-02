package com.firstproject.androiddemofx.month4.week1;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;
import com.firstproject.androiddemofx.provider.trafficstats.TrafficStatsContract;

public class Activity_Day_4_1_1 extends AppCompatActivity {
    String TAG = "trafficstatsTest";
    private EditText insertCellId;
    private EditText insertRsrp;
    private EditText insertTechType;
    private EditText insertTimestamp;
    private Button insertButton;

    private Button queryAllButton;
    private EditText queryId;
    private Button queryByIdButton;

    private EditText deleteId;
    private Button deleteButton;

    private EditText updateId;
    private EditText updateRsrp;
    private Button updateButton;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day411);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        insertCellId = findViewById(R.id.insert_cell_id);
        insertRsrp = findViewById(R.id.insert_rsrp);
        insertTechType = findViewById(R.id.insert_tech_type);
        insertTimestamp = findViewById(R.id.insert_times_tamp);
        insertButton = findViewById(R.id.insert_button);

        queryAllButton = findViewById(R.id.query_all_button);
        queryId = findViewById(R.id.query_id);
        queryByIdButton = findViewById(R.id.query_by_id_button);

        deleteId = findViewById(R.id.delete_id);
        deleteButton = findViewById(R.id.delete_button);

        updateId = findViewById(R.id.update_id);
        updateRsrp = findViewById(R.id.update_rsrp);
        updateButton = findViewById(R.id.update_button);

        resultTextView = findViewById(R.id.result_text_view);
    }

    private void setupClickListeners() {
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        queryAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllData();
            }
        });

        queryByIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDataById();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void insertData() {
        String cellId = insertCellId.getText().toString();
        String rsrp = insertRsrp.getText().toString();
        String techType = insertTechType.getText().toString();
        String timestamp = insertTimestamp.getText().toString();

        ContentValues values = new ContentValues();
        values.put(TrafficStatsContract.TrafficStatsColumns.cellId, cellId);
        values.put(TrafficStatsContract.TrafficStatsColumns.rsrp, rsrp);
        values.put(TrafficStatsContract.TrafficStatsColumns.techType, techType);
        values.put(TrafficStatsContract.TrafficStatsColumns.timestamp, timestamp);

        Uri uri = getContentResolver().insert(TrafficStatsContract.Companion.getCONTENT_URI(), values);
        Log.d(TAG,"Insert new uri:"+uri);
        resultTextView.setText(uri != null ? "插入数据成功，URI: " + uri : "插入数据失败");
    }

    private void queryAllData() {
        Cursor cursor = getContentResolver().query(
                TrafficStatsContract.Companion.getCONTENT_URI(),
                null,
                null,
                null,
                null
        );

        displayCursorData(cursor);
    }

    private void queryDataById() {
        String id = queryId.getText().toString();
        if (!id.isEmpty()) {
            Cursor cursor = getContentResolver().query(
                    TrafficStatsContract.Companion.getCONTENT_URI(),
                    null,
                    TrafficStatsContract.TrafficStatsColumns._id + " = ?",
                    new String[]{id},
                    null
            );
            displayCursorData(cursor);
        } else {
            resultTextView.setText("请输入要查询的 ID");
        }
    }

    private void deleteData() {
        String id = deleteId.getText().toString();
        if (!id.isEmpty()) {
            int rowsDeleted = getContentResolver().delete(
                    TrafficStatsContract.Companion.getCONTENT_URI(),
                    TrafficStatsContract.TrafficStatsColumns._id + " = ?",
                    new String[]{id}
            );
            resultTextView.setText(rowsDeleted > 0 ? "删除 " + rowsDeleted + " 条数据成功" : "删除数据失败");
        } else {
            resultTextView.setText("请输入要删除的 ID");
        }
    }

    private void updateData() {
        String id = updateId.getText().toString();
        String newRsrp = updateRsrp.getText().toString();

        if (!id.isEmpty() && !newRsrp.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(TrafficStatsContract.TrafficStatsColumns.rsrp, newRsrp);

            int rowsUpdated = getContentResolver().update(
                    TrafficStatsContract.Companion.getCONTENT_URI(),
                    values,
                    TrafficStatsContract.TrafficStatsColumns._id + " = ?",
                    new String[]{id}
            );
            resultTextView.setText(rowsUpdated > 0 ? "更新 " + rowsUpdated + " 条数据成功" : "更新数据失败");
        } else {
            resultTextView.setText("请输入要修改的 ID 和新的 rsrp");
        }
    }

    private void displayCursorData(Cursor cursor) {
        if (cursor != null) {
            StringBuilder result = new StringBuilder();
            while (cursor.moveToNext()) {
                String id = getStringFromCursor(cursor,TrafficStatsContract.TrafficStatsColumns._id);
                String cellId = getStringFromCursor(cursor,TrafficStatsContract.TrafficStatsColumns.cellId);
                String rsrp = getStringFromCursor(cursor,TrafficStatsContract.TrafficStatsColumns.rsrp);
                String techType = getStringFromCursor(cursor,TrafficStatsContract.TrafficStatsColumns.techType);

                result.append("ID: ").append(id).append(", CellId: ").append(cellId).append(", RSRP: ").append(rsrp).append(", TechType: ").append(techType).append("\n");
            }
            cursor.close();
            resultTextView.setText(result.length() == 0 ? "暂无数据" : result.toString());
        } else {
            resultTextView.setText("查询失败");
        }
    }

    private String getStringFromCursor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        } else {
            return null;
        }
    }
}

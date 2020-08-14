package com.example.roadfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roadfinder.DrawPath.NewVersionDraw;

public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnFinish;
    private NewVersionDraw newVersionDraw;
    private static final int areaRowsSize = 20;
    private static final int areaColsSize = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newVersionDraw =findViewById(R.id.path_view);
        newVersionDraw.setBoardParams(areaRowsSize,areaColsSize);
        btnStart = findViewById(R.id.btn_start);
        btnFinish =findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newVersionDraw.setSelectedFinish(true);
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newVersionDraw.setSelectedStart(true);
            }
        });
    }
}

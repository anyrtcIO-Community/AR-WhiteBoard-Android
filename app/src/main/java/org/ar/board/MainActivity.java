package org.ar.board;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etRoom;
    Button btnJoin,btnCreat;
    TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersion=findViewById(R.id.tv_version);
        tvVersion.setText("v "+BuildConfig.VERSION_NAME);
        etRoom=findViewById(R.id.et_roomId);
        btnJoin=findViewById(R.id.btn_join);
        btnCreat=findViewById(R.id.btn_creat);
        btnCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRoom.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"请输入6位画板ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etRoom.getText().toString().trim().length()<6){
                    Toast.makeText(MainActivity.this,"请输入6位画板ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i=new Intent(MainActivity.this,BoardActivity.class);
                i.putExtra("roomId",etRoom.getText().toString());
                i.putExtra("isHost",true);
                startActivity(i);
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRoom.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"请输入6位画板ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etRoom.getText().toString().trim().length()<6){
                    Toast.makeText(MainActivity.this,"请输入6位画板ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i=new Intent(MainActivity.this,BoardActivity.class);
                i.putExtra("roomId",etRoom.getText().toString());
                i.putExtra("isHost",false);
                startActivity(i);
            }
        });
    }

}

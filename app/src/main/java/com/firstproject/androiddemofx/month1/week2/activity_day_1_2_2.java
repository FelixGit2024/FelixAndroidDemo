package com.firstproject.androiddemofx.month1.week2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_1_2_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day122);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //普通toast
        Button btnToast1221=findViewById(R.id.btnToast1221);
        btnToast1221.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Day_1_2_2.this, "this is Toast", Toast.LENGTH_SHORT).show();
            }
        });
        //带图片的toast
        Button btnToast1222=findViewById(R.id.btnToast1222);
        btnToast1222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=new Toast(Activity_Day_1_2_2.this);
                toast.setDuration(Toast.LENGTH_SHORT);
                ImageView img=new ImageView(Activity_Day_1_2_2.this);
                img.setImageResource(R.drawable.bingwallpaper2);
                toast.setView(img);
                toast.show();
            }
        });
        //自定义布局的toast
        Button btnToast1223=findViewById(R.id.btnToast1223);
        btnToast1223.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=new Toast(Activity_Day_1_2_2.this);
                toast.setDuration(Toast.LENGTH_SHORT);

                LinearLayout linearLayout=new LinearLayout(Activity_Day_1_2_2.this);

                TextView textView = new TextView(Activity_Day_1_2_2.this);
                textView.setText("自定义布局");
                ImageView img = new ImageView(Activity_Day_1_2_2.this);
                img.setImageResource(R.drawable.bingwallpaper3);
                linearLayout.addView(img);
                linearLayout.addView(textView);

                toast.setView(linearLayout);
                toast.show();
            }
        });
        //普通Dialog
        Button btnDialog1221=findViewById(R.id.btnDialog1221);
        btnDialog1221.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Day_1_2_2.this)
                        .setIcon(R.drawable.icon1_bluestar_foreground)
                        .setTitle("普通Dialog")
                        .setMessage("点击下面按钮")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("不确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了不确定", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //列表Dialog
        Button btnDialog1222=findViewById(R.id.btnDialog1222);
        btnDialog1222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] itemArray={"第一个","第二个","第三个","第四个","第五个"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Day_1_2_2.this)
                        .setIcon(R.drawable.icon1_bluestar_foreground)
                        .setTitle("列表Dialog")
//                .setMessage("点击下面列表元素")
                        .setItems(itemArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了"+itemArray[which], Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //单选对话框
        Button btnDialog1223=findViewById(R.id.btnDialog1223);
        btnDialog1223.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] itemArray = {"元素1", "元素2", "元素3", "元素4", "元素5"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Day_1_2_2.this)
                        .setIcon(R.drawable.icon1_bluestar_foreground)
                        .setTitle("单选对话框")
                        .setSingleChoiceItems(itemArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "单选了-"+itemArray[which], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //多选对话框
        Button btnDialog1224=findViewById(R.id.btnDialog1224);
        btnDialog1224.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] itemArray = {"元素1", "元素2", "元素3", "元素4", "元素5"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Day_1_2_2.this)
                        .setIcon(R.drawable.icon1_bluestar_foreground)
                        .setTitle("多选对话框")
                        .setMultiChoiceItems(itemArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Toast.makeText(Activity_Day_1_2_2.this, itemArray[which]+"——"+isChecked, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Day_1_2_2.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //日期对话框
        Button btnDialog1225=findViewById(R.id.btnDialog1225);
        btnDialog1225.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog.THEME_HOLO_LIGHT
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Day_1_2_2.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Toast.makeText(Activity_Day_1_2_2.this, year + "-" + month + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                            }
                        }, 2024, 5, 4);
                datePickerDialog.show();
            }
        });
        //时间选择对话框
        Button btnDialog1226=findViewById(R.id.btnDialog1226);
        btnDialog1226.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog.THEME_HOLO_LIGHT设置对话框格式，可去掉
                TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_Day_1_2_2.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(Activity_Day_1_2_2.this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                    }
                }, 9, 30, true);
                timePickerDialog.show();
            }
        });
        //notification
        Button btnNotification1221=findViewById(R.id.btnNotification1221);
        btnNotification1221.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Android 8.0之后，需要手动添加NotifacationChannel实现，否则log会有如下提示：
                // D/skia: --- Failed to create image decoder with message 'unimplemented'
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("channel_001", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Notification notification = new NotificationCompat.Builder(Activity_Day_1_2_2.this,"channel_001")
                        .setContentTitle("notification通知")
                        .setContentText("通知消息：notification显示完成")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.wireless_tower)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.walk))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT) //权限设置成默认
                        .build();

                notificationManager.notify(1,notification);
//                Log.d("TAG", "btnDialogN: notification");
            }
        });

        Button btnNotification1222=findViewById(R.id.btnNotification1222);
        btnNotification1222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Android 8.0之后，需要手动添加NotifacationChannel实现，否则log会有如下提示：
                // D/skia: --- Failed to create image decoder with message 'unimplemented'
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("channel_002", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Notification notification = new NotificationCompat.Builder(Activity_Day_1_2_2.this,"channel_002")
                        .setContentTitle("自定义notification通知")
                        .setContentText("通知消息：自定义notification显示完成")
                        .setTicker("顶部显示文字SetTicker")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.wireless_tower)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.walk))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT) //权限设置成默认
                        .setVibrate(new long[]{0,300,500,700}) //振动方式
                        .setLights(153,500,200)
                        .setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"5"))
                        .setProgress(100,70,false)
                        .build();

                notification.flags=Notification.FLAG_SHOW_LIGHTS; //显示灯光

                notificationManager.notify(1,notification);
                Log.d("TAG", "btnDialogN: notification");
            }
        });
    }
}
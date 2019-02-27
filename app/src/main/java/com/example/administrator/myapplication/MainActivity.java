package com.example.administrator.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private Fragment[] fg = new Fragment[3];
    private FragmentManager fManager;
    private TextView title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        title_text = findViewById(R.id.txt_topbar);
        fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fg[0] = new BeatFragment();
        fg[1] = new HistoryFragment();
        fg[2] = new SettingsFragment();
        for (int i = 0; i < 3; i++) {
            fTransaction.add(R.id.ly_content, fg[i]);
        }
        fTransaction.commit();
        //UI Object
        RadioGroup rg_tab_bar = findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        RadioButton rb_channel = findViewById(R.id.rb_record);
        rb_channel.setChecked(true);

    }


    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < 3; i++) {
            fragmentTransaction.hide(fg[i]);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId) {
            case R.id.rb_record:
                title_text.setText(R.string.tab_menu_record);
                fTransaction.show(fg[0]);
                break;
            case R.id.rb_history:
                title_text.setText(R.string.tab_menu_history);

                fTransaction.show(fg[1]);
                break;
            case R.id.rb_settings:
                title_text.setText(R.string.tab_menu_setting);
                fTransaction.show(fg[2]);
                break;
        }
        fTransaction.commit();
    }

}

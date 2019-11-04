package cn.zhouchaoyuan.excelpaneldemo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by spissay on 10,October,2019
 */
public class DetailActivity extends AppCompatActivity {

    TextView wbsCode;
    TextView wbsItem;
    TextView wbsBudget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        wbsCode = findViewById(R.id.wbs_code_tv);
        wbsItem = findViewById(R.id.wbs_item_tv);
        wbsBudget = findViewById(R.id.budget_tv);
        wbsCode.setText(getIntent().getStringExtra("code"));
        wbsItem.setText(getIntent().getStringExtra("item"));
        wbsBudget.setText(getIntent().getStringExtra("budget"));
    }
}

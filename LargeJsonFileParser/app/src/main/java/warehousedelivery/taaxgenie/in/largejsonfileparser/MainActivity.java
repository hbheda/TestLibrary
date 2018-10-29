package warehousedelivery.taaxgenie.in.largejsonfileparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.helper.harshitlibrary.RssFeedProvider;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.num1)    EditText num1;
    @BindView(R.id.num2)    EditText num2;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.total)
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Library");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double first = Double.parseDouble(num1.getText().toString());
                Double second = Double.parseDouble(num2.getText().toString());

                Double sum = RssFeedProvider.getSum(first,second);
                total.setText(String.valueOf(sum));
            }
        });
    }
}

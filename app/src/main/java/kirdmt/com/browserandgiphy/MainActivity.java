package kirdmt.com.browserandgiphy;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button browser, giphy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser = (Button) findViewById(R.id.browser);
        giphy = (Button) findViewById(R.id.giphy);


        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getApplicationContext(), BrowserActivity.class);
                startActivity(browserIntent);
            }
        });

        giphy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giphyIntent = new Intent(getApplicationContext(), GiphyActivity.class);
                startActivity(giphyIntent);
            }
        });


    }
}

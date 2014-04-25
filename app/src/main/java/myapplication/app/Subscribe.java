package myapplication.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

public class Subscribe extends Activity {

    private ViewFlipper vs;
    TextView tv_vousetes;
    TextView tv_ouais;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        vs = (ViewFlipper) findViewById(R.id.viewSwitcher);

        tv_vousetes = (TextView) findViewById(R.id.tv_vousetes);
        tv_ouais = (TextView) findViewById(R.id.tv_ouais);


        tv_vousetes.setOnClickListener(ok);
        tv_ouais.setOnClickListener(ok);




    }

    public View.OnClickListener ok = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            vs.showNext();

        }
    };

}

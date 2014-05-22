package myapplication.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

public class  Questions extends Activity {

    ViewFlipper viewFlipper;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);


        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        radioGroup = (RadioGroup) findViewById(R.id.radiog1);


        SharedPreferences preferences = getSharedPreferences("SXBN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

      //   editor.putString("SXBN", "passage" ) ou putInt ou autre : rajoute une donnée dans les préférences SXBN
        // editor.commit(); -> Sauvegarde les préférences

        //  viewFlipper.showNext(); passe au second RelativeLayout de question.xml

       // editor.putString("SXBN_exist", "yes"); -> A mettre tout à la fin quand les test sont finis pour ne plus avoir à refaire les questions

    }
}
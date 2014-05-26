package myapplication.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.HashMap;
import java.util.Map;

public class Questions extends Activity {

    ViewFlipper viewQuestions;
    ViewFlipper viewReponses;
    //ViewFlipper viewAffichage;
    RadioGroup radioGroup;
    String rt1 = "";
    String rt2 = "";
    String rt3 = "";
    String rt4 = "";
    String rt5 = "";
    boolean offtour = false;
    boolean parking = true;
    boolean autotrement = false;
    boolean velhop = true;
    boolean pratique= false;
    boolean spectacle = false;
    boolean equipsportif = false;
    boolean culte = false;
    boolean eau = true;
    boolean toilette = true;
    boolean promenade = false;
    boolean vert = false;
    boolean airejeux = false;
    boolean fontaine = false;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    SharedPreferences chaine_choix;
    SharedPreferences.Editor editor_choix;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        viewQuestions = (ViewFlipper) findViewById(R.id.viewQuestions);

        // Initialisation des valeurs pour la question 1
        RadioGroup rg1 = (RadioGroup) findViewById(R.id.id_radiog1);
        Button btn_next1 = (Button) findViewById(R.id.id_btn_next1);

        // Initialisation des valeurs pour la question 2
        RadioGroup rg2 = (RadioGroup) findViewById(R.id.id_radiog2);
        Button btn_next2 = (Button) findViewById(R.id.id_btn_next2);

        // Initialisation des valeurs pour la question 3
        RadioGroup rg3 = (RadioGroup) findViewById(R.id.id_radiog3);
        Button btn_next3 = (Button) findViewById(R.id.id_btn_next3);

        // Initialisation des valeurs pour la question 4
        RadioGroup rg4 = (RadioGroup) findViewById(R.id.id_radiog4);
        Button btn_next4 = (Button) findViewById(R.id.id_btn_next4);

        // Initialisation des valeurs pour la question 5
        RadioGroup rg5 = (RadioGroup) findViewById(R.id.id_radiog5);
        Button btn_valid = (Button) findViewById(R.id.id_btn_valid);
        Button btn_confirm = (Button) findViewById(R.id.id_btn_confirm);

        // Initialisation des preferences
        preferences = getSharedPreferences("SXBN", MODE_PRIVATE);
        editor = preferences.edit();

        chaine_choix = getSharedPreferences("SXBN_data", MODE_PRIVATE);
        editor_choix = chaine_choix.edit();

        // Actions
        btn_next1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewQuestions.showNext();
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewQuestions.showNext();
            }
        });
        btn_next3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewQuestions.showNext();
            }
        });
        btn_next4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewQuestions.showNext();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //viewQuestions.showNext();

                CheckBox cb_offtour = (CheckBox) findViewById(R.id.id_offtour);
                CheckBox cb_spectacle = (CheckBox) findViewById(R.id.id_spectacle);
                CheckBox cb_promenade = (CheckBox) findViewById(R.id.id_promenade);
                CheckBox cb_vert = (CheckBox) findViewById(R.id.id_vert);
                CheckBox cb_equipsportif = (CheckBox) findViewById(R.id.id_equipsportif);
                CheckBox cb_fontaine = (CheckBox) findViewById(R.id.id_fontaine);
                CheckBox cb_airejeux = (CheckBox) findViewById(R.id.id_airejeux);
                CheckBox cb_culte = (CheckBox) findViewById(R.id.id_culte);
                CheckBox cb_eau = (CheckBox) findViewById(R.id.id_eau);
                CheckBox cb_toilette = (CheckBox) findViewById(R.id.id_toilette);
                CheckBox cb_velhop = (CheckBox) findViewById(R.id.id_velhop);
                CheckBox cb_autotrement = (CheckBox) findViewById(R.id.id_autotrement);
                CheckBox cb_parking = (CheckBox) findViewById(R.id.id_parking);
                CheckBox cb_pratique = (CheckBox) findViewById(R.id.id_pratique);

                //Affichage de la chaîne de choix
                String choix2 = "data";

                if (cb_offtour.isChecked()) {
                    choix2 = choix2.concat("_offtour");
                };
                if (cb_pratique.isChecked()) {
                    choix2 = choix2.concat("_pratique");
                };
                if (cb_promenade.isChecked()) {
                    choix2 = choix2.concat("_promenade");
                };
                if (cb_vert.isChecked()) {
                    choix2 = choix2.concat("_vert");
                };
                if (cb_airejeux.isChecked()) {
                    choix2 = choix2.concat("_airejeux");
                };
                if (cb_equipsportif.isChecked()) {
                    choix2 = choix2.concat("_equipsportif");
                };
                if (cb_autotrement.isChecked()) {
                    choix2 = choix2.concat("_autotrement");
                };
                if (cb_velhop.isChecked()) {
                    choix2 = choix2.concat("_velhop");
                };
                if (cb_parking.isChecked()) {
                    choix2 = choix2.concat("_parking");
                };
                if (cb_culte.isChecked()) {
                    choix2 = choix2.concat("_culte");
                };
                if (cb_fontaine.isChecked()) {
                    choix2 = choix2.concat("_fontaine");
                };
                if (cb_spectacle.isChecked()) {
                    choix2 = choix2.concat("_spectacle");
                };
                if (cb_toilette.isChecked()) {
                    choix2 = choix2.concat("_eau");
                };
                if (cb_eau.isChecked()) {
                    choix2 = choix2.concat("_toilette");
                };

                //Enregistrement de la chaîne dans un SharedPreference
                editor_choix.putString("tag", choix2);
                editor.putString("SXBN_exist", "yes");
                editor_choix.commit();
                editor.commit();

//                TextView tv_choix2 = (TextView) findViewById(R.id.id_choix2);
//                String res_choix2 = chaine_choix.getString("tag", "");
//                tv_choix2.setText(res_choix2);
                Intent intent = new Intent(Questions.this, Data.class);
                startActivity(intent);
            }
        });

        //Validation des questions et enregistrement du profil
        btn_valid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //setContentView(R.layout.reponse);
                //viewReponses = (ViewFlipper) findViewById(R.id.viewReponses);
                viewQuestions.showNext();
                viewQuestions.setBackgroundColor(0xf0f0f0);

                CheckBox cb_offtour = (CheckBox) findViewById(R.id.id_offtour);
                CheckBox cb_spectacle = (CheckBox) findViewById(R.id.id_spectacle);
                CheckBox cb_promenade = (CheckBox) findViewById(R.id.id_promenade);
                CheckBox cb_vert = (CheckBox) findViewById(R.id.id_vert);
                CheckBox cb_equipsportif = (CheckBox) findViewById(R.id.id_equipsportif);
                CheckBox cb_fontaine = (CheckBox) findViewById(R.id.id_fontaine);
                CheckBox cb_airejeux = (CheckBox) findViewById(R.id.id_airejeux);
                CheckBox cb_culte = (CheckBox) findViewById(R.id.id_culte);
                CheckBox cb_eau = (CheckBox) findViewById(R.id.id_eau);
                CheckBox cb_toilette = (CheckBox) findViewById(R.id.id_toilette);
                CheckBox cb_velhop = (CheckBox) findViewById(R.id.id_velhop);
                CheckBox cb_autotrement = (CheckBox) findViewById(R.id.id_autotrement);
                CheckBox cb_parking = (CheckBox) findViewById(R.id.id_parking);
                CheckBox cb_pratique = (CheckBox) findViewById(R.id.id_pratique);

                //Affichage de la chaîne de choix
                String choix = "toilette_eau";

                if (offtour) {
                    choix = choix.concat("_offtour");
                    cb_offtour.setChecked(true);
                };
                if (pratique) {
                    choix = choix.concat("_pratique");
                    cb_pratique.setChecked(true);
                };
                if (promenade) {
                    choix = choix.concat("_promenade");
                    cb_promenade.setChecked(true);
                };
                if (vert) {
                    choix = choix.concat("_vert");
                    cb_vert.setChecked(true);
                };
                if (airejeux) {
                    choix = choix.concat("_airejeux");
                    cb_airejeux.setChecked(true);
                };
                if (equipsportif) {
                    choix = choix.concat("_equipsportif");
                    cb_equipsportif.setChecked(true);
                };
                if (autotrement) {
                    choix = choix.concat("_autotrement");
                    cb_autotrement.setChecked(true);
                };
                if (velhop) {
                    choix = choix.concat("_velhop");
                    cb_velhop.setChecked(true);
                };
                if (parking) {
                    choix = choix.concat("_parking");
                    cb_parking.setChecked(true);
                };
                if (culte) {
                    choix = choix.concat("_culte");
                    cb_culte.setChecked(true);
                };
                if (fontaine) {
                    choix = choix.concat("_fontaine");
                    cb_fontaine.setChecked(true);
                };
                if (spectacle) {
                    choix = choix.concat("_spectacle");
                    cb_spectacle.setChecked(true);
                };

                cb_toilette.setChecked(true);
                cb_eau.setChecked(true);

                //Enregistrement de la chaîne dans un SharedPreference
                editor_choix.putString("tag", choix);
                editor_choix.commit();

                TextView tv_choix = (TextView) findViewById(R.id.id_choix);
                String res_choix = chaine_choix.getString("tag", "");
                tv_choix.setText(res_choix);
            }
        });

        // Récupération des radio button checked (question 1)
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.cb_visite:
                        rt1 = "visite";
                        offtour = true;
                        parking = true;
                        autotrement = true;
                        velhop = true;
                        pratique= true;
                        break;
                    case R.id.cb_habite:
                        rt1 = "habite";
                        break;
                    case R.id.cb_passage:
                        rt1 = "passage";
                        pratique= true;
                        break;
                    default:
                        rt1 = "default";
                        break;
                }

                // Ajoute une donnée dans les préférences SXBN + enregistrement
                editor.putString("habite", rt1);
                editor.putBoolean("offtour", offtour);
                editor.putBoolean("parking", parking);
                editor.putBoolean("autotrement", autotrement);
                editor.putBoolean("velhop", velhop);
                editor.putBoolean("pratique", pratique);
                editor.commit();
            }
        });

        // Récupération des radio button checked (question 2)
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.cb_mineur:
                        rt2 = "mineur";
                        parking = false;
                        break;
                    case R.id.cb_jeune:
                        rt2 = "jeune";
                        break;
                    case R.id.cb_adulte:
                        rt2 = "adulte";
                        break;
                    case R.id.cb_adulteplus:
                        rt2 = "adulteplus";
                        break;
                    case R.id.cb_senior:
                        rt2 = "senior";
                        break;
                    default:
                        rt2 = "default";
                        break;
                }

                // Ajoute une donnée dans les préférences SXBN + enregistrement
                editor.putString("age", rt2);
                editor.putBoolean("parking", parking);
                editor.commit();
            }
        });

        // Récupération des radio button checked (question 3)
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.cb_promene:
                        rt3 = "promene";
                        promenade = true;
                        vert = true;
                        fontaine = true;
                        break;
                    case R.id.cb_detente:
                        rt3 = "detente";
                        fontaine = true;
                        vert = true;
                        break;
                    case R.id.cb_divert:
                        rt3 = "divert";
                        spectacle = true;
                        break;
                    case R.id.cb_culture:
                        rt3 = "culture";
                        culte = true;
                        offtour = true;
                        break;
                    default:
                        rt3 = "default";
                        break;
                }

                // Ajoute une donnée dans les préférences SXBN + enregistrement
                editor.putString("pref", rt3);
                editor.putBoolean("promenade", promenade);
                editor.putBoolean("vert", vert);
                editor.putBoolean("fontaine", fontaine);
                editor.putBoolean("spectacle", spectacle);
                editor.putBoolean("culte", culte);
                editor.putBoolean("offtour", offtour);
                editor.commit();
            }
        });

        // Récupération des radio button checked (question 4)
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.cb_sportoui:
                        rt4 = "sportoui";
                        equipsportif = true;
                        break;
                    case R.id.cb_sportnon:
                        rt4 = "sportnon";
                        break;
                    default:
                        rt4 = "default";
                        break;
                }

                // Ajoute une donnée dans les préférences SXBN + enregistrement
                editor.putString("sport", rt4);
                editor.putBoolean("equipsportif", equipsportif);
                editor.commit();
            }
        });

        // Récupération des radio button checked (question 5)
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.cb_enfoui:
                        rt5 = "enfoui";
                        airejeux = true;
                        break;
                    case R.id.cb_enfnon:
                        rt5 = "enfnon";
                        break;
                    default:
                        rt5 = "default";
                        break;
                }

                // Ajoute une donnée dans les préférences SXBN + enregistrement
                editor.putString("enfant", rt5);
                editor.putBoolean("airejeux", airejeux);
                editor.commit();
            }
        });
        // editor.putString("SXBN_exist", "yes"); -> A mettre tout à la fin quand les test sont finis pour ne plus avoir à refaire les questions
    }

};
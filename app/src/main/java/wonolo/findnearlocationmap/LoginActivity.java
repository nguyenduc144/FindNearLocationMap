package wonolo.findnearlocationmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    private SharedPreferences mSharedPreferences;
    private EditText mPassword;
    private EditText mUsername;
    private CheckBox mCheckHide;
    private String mName,mPass;
    private DataBaseAdapter mDataBase;
    private boolean mIsCorrect ;
    public static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = (EditText) findViewById(R.id.login);
        mPassword = (EditText) findViewById(R.id.password);
        mCheckHide = (CheckBox) findViewById(R.id.check_hide);

        mSharedPreferences = getSharedPreferences(PREFS_NAME,0);

        mDataBase = new DataBaseAdapter(this);
        MultiDex.install(this);
        if(mSharedPreferences.getString("logged","").toString().equals("logged")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
    public void onClickLogin(View view){
        mName = mUsername.getText().toString();
        mPass = mPassword.getText().toString();
        checkLogin(mName,mPass);
    }
    private void checkLogin(String name,String pass){
        mDataBase.open();
        Cursor cursor = mDataBase.getAllContacts();
        if(cursor.moveToFirst()){
            do{
                if (name.equals(cursor.getString(1))&& pass.equals(cursor.getString(2))){
                    mIsCorrect = true;
                    mSharedPreferences = getSharedPreferences(PREFS_NAME,0);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("logged","logged");
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
            } while (cursor.moveToNext());
            if(!mIsCorrect){
                Toast.makeText(getApplicationContext(), "Username or Password not correct", Toast.LENGTH_SHORT).show();
            }
        }
        mDataBase.close();
    }
    public void onClickNotHide(View view){
        if(!mCheckHide.isChecked()){
            mPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            mPassword.setTransformationMethod(null);
        }
        mPassword.setSelection(mPassword.getText().length());

    }
    public void onClickRegister(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void onClickLoginWithQR(View view){
        Intent intent = new Intent(this,BarcodeCaptureActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                try {
                    JSONObject jsonObject = new JSONObject(barcode.displayValue.toString());
                    mName = jsonObject.getString("username");
                    mPass = jsonObject.getString("password");
                    checkLogin(mName,mPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

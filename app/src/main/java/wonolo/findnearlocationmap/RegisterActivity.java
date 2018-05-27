package wonolo.findnearlocationmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText mRegisterUsername,mRegisterPassword,mRegisterPasswordAgain;
    public static DataBaseAdapter sDataBase;
    private String mName,mPass,mPassAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterUsername = (EditText)findViewById(R.id.registerUsername);
        mRegisterPassword = (EditText)findViewById(R.id.registerPassword);
        mRegisterPasswordAgain = (EditText)findViewById(R.id.registerPasswordAgain);
    }
    public void onClickRegisterOK(View view){
        mName = mRegisterUsername.getText().toString();
        mPass = mRegisterPassword.getText().toString();
        mPassAgain = mRegisterPasswordAgain.getText().toString();
        if(!mPass.equals(mPassAgain)){
            Toast.makeText(this,"Password and Password do not match",Toast.LENGTH_LONG).show();
            mRegisterPassword.setText("");
            mRegisterPasswordAgain.setText("");
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            sDataBase = new DataBaseAdapter(this);
            sDataBase.open();
            long id = sDataBase.insertContact(mName,mPass);
            sDataBase.close();
            startActivity(intent);
        }

    }
}

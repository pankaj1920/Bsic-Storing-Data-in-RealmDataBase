package com.app.realmdatabase;

import android.service.autofill.FieldClassification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    EditText editText_name, editText_email, editText_password;
    Button count, rigster;

    private Pattern pattern;
    private Matcher matcher;

    //this will set that password should contain 1CapitalLetter,1Symbol,1Numeric And alphaber
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$";

    private RealmResults<Customer> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        pattern = Pattern.compile(PASSWORD_PATTERN);

        editText_name = (EditText) findViewById(R.id.name);
        editText_email = (EditText) findViewById(R.id.email);
        editText_password = (EditText) findViewById(R.id.password);
        rigster = (Button) findViewById(R.id.rigster);
        count = (Button) findViewById(R.id.count);

        rigster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editText_name.getText().toString();
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();

                //it will check wheather the password is contaning all requriment
                matcher = pattern.matcher(password);

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the Valid Number", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the Valid email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the Valid Password", Toast.LENGTH_SHORT).show();
                }else if (!matcher.matches()){
                    Toast.makeText(MainActivity.this, "Password Should Contain Unique Code", Toast.LENGTH_SHORT).show();
                }else{
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setEmail(email);
                    customer.setPassword(password);

                    //calling method to save data
                    saveInDataBase(customer);

                }
            }
        });

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getuser();
            }
        });

    }

    //This method to save data in data base
    private void saveInDataBase(final Customer customer) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNum = realm.where(Customer.class).max("id");

                long nextId;
                if (currentIdNum == null){
                    nextId = 1;
                }else {
                    nextId = currentIdNum.longValue()+1;
                }

                customer.setId(nextId);
                realm.insertOrUpdate(customer);
//                Toast.makeText(MainActivity.this, "Inserted Sucessfully", Toast.LENGTH_SHORT).show();
                editText_name.setText("");
                editText_email.setText("");
                editText_password.setText("");
            }
        });
    }

    //This method will count how many people entered data
    private void getuser() {

        realmResults = realm.where(Customer.class).findAll();
        for (Customer use : realmResults) {
            String messg = use.getName() + " " + use.getPassword();
            Log.e("Mesafe", messg);
        }
        Toast.makeText(this, "Size is " + realmResults.size(), Toast.LENGTH_SHORT).show();


    }

}

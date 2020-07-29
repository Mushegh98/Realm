package com.test.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.myapplication.model.Student;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText name,marks;
    TextView display;
    Button saveButton;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        marks = findViewById(R.id.marks);
        display = findViewById(R.id.display);
        saveButton = findViewById(R.id.saveButton);

        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        //Insert item or list
        realm.beginTransaction();
        Student student = new Student("Mushegh",21);
        List<Student> students = Arrays.asList(new Student("Edik",19),new Student("Mushegh",22));
        realm.copyToRealm(student);
        realm.insert(students);
        realm.commitTransaction();

        //Insert2
        realm.beginTransaction();
        Student _student = realm.createObject(Student.class);
        _student.setName("Mushegh");
        _student.setAge(21);
        realm.commitTransaction();

        //query looking at all users
        RealmQuery<Student> studentRealmQuery = realm.where(Student.class);

        //query conditions
        //SELECT * FROM TABLE WHERE age=22 nerqevin@ da a anum
        studentRealmQuery.equalTo("age",22);
//        studentRealmQuery.or().equalTo("name","Edik");

        RealmResults<Student> studentResult = studentRealmQuery.findAll();
        Log.d("TAG",studentResult.get(0).getName());




        //query looking at all users
        RealmResults<Student> stud = realm.where(Student.class).greaterThan("age",21).limit(10).findAll();

        //find first
        Student first = realm.where(Student.class).greaterThan("age",21).limit(10).findFirst();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveData();
                realm.beginTransaction();
                Student _student = realm.createObject(Student.class);
                _student.setName("Mushegh");
                _student.setAge(25);
                realm.commitTransaction();
                readData();
            }
        });
    }

    private void saveData() {
       realm.executeTransactionAsync(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
                Student student = realm.createObject(Student.class);
                student.setName(name.getText().toString().trim());
                student.setAge(Integer.parseInt(marks.getText().toString().trim()));
           }
       }, new Realm.Transaction.OnSuccess(){

           @Override
           public void onSuccess() {
               readData();
           }
       }, new Realm.Transaction.OnError(){

           @Override
           public void onError(Throwable error) {

           }
       });
    }

    private void readData(){
        RealmResults<Student> students = realm.where(Student.class).findAll();
        display.setText("");
        String data = "";
        for(Student student : students){
            try {
                data = data + "\n" + student.toString();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        display.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
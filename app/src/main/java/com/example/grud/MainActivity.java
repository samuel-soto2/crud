package com.example.grud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Abrir la base de datos (o crearla si no existe)
        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        // Asignar los elementos de la interfaz
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Asignar el listener para el botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (login(username, password)) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    // Aquí puedes iniciar una nueva actividad, por ejemplo, la actividad del panel principal
                } else {
                    Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Asignar el listener para el botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (register(username, password)) {
                        Toast.makeText(MainActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Método para verificar el inicio de sesión
    private boolean login(String username, String password) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    // Método para registrar un nuevo usuario
    private boolean register(String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NOMBRE_USUARIO, username);
        contentValues.put(DBHelper.COLUMN_CONTRASEÑA, password);

        long result = mDatabase.insert(DBHelper.TABLE_USUARIOS, null, contentValues);
        return result != -1;
    }
}
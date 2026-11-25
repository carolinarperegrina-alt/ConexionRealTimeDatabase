package com.example.conexionrealtimedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NuevoPacienteActivity extends AppCompatActivity {

    //Atributos de la clase
    EditText etNuhsa, etNombre, etApellidos;
    Spinner spGrupo;
    Button btnFecha;
    String fechaNacimiento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_paciente);

        //Busco las vistas de la interfaz
        etNuhsa = findViewById(R.id.etNuhsa);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        spGrupo = findViewById(R.id.spGrupo);
        btnFecha = findViewById(R.id.btnFecha);

        //Relleno el desplegable con los grupos sanguíneos de Constantes
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Constantes.grupo);
        spGrupo.setAdapter(adaptador);
    }

    //OnClick asignado desde el Layout: abre el selector de fecha
    public void elegirFecha(View view) {
        Calendar hoy = Calendar.getInstance();
        DatePickerDialog dialogo = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //month empieza en 0, por eso sumo 1
                        fechaNacimiento = String.format("%02d/%02d/%d", day, month + 1, year);
                        btnFecha.setText(fechaNacimiento);
                    }
                },
                hoy.get(Calendar.YEAR) - 30, hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));
        //No se puede elegir una fecha futura
        dialogo.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialogo.show();
    }

    //OnClick asignado desde el Layout: guarda el paciente en Firebase
    public void guardaPaciente(View view) {
        String nuhsa = etNuhsa.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();

        if (nuhsa.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        //Firebase no permite estos caracteres en el nombre de un nodo
        if (nuhsa.matches(".*[.$#\\[\\]/].*")) {
            Toast.makeText(this, "El NUHSA no puede llevar . $ # [ ] /", Toast.LENGTH_SHORT).show();
            return;
        }

        String grupo = spGrupo.getSelectedItem().toString();
        Paciente paciente = new Paciente(nombre, apellidos, grupo, nuhsa, fechaNacimiento);

        //Igual que en la inserción aleatoria: en Pacientes y colgando de su grupo sanguíneo
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();
        raiz.child(Constantes.pacientes).child(nuhsa).setValue(paciente);
        raiz.child(grupo).child(nuhsa).setValue(paciente);

        Toast.makeText(this, "Paciente guardado", Toast.LENGTH_SHORT).show();
        finish();
    }
}
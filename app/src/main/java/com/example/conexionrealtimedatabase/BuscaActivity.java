package com.example.conexionrealtimedatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuscaActivity extends AppCompatActivity {

    //Atributos de la clase
    String nuhsa;
    TextView nombre, apellidos, grupoSanguineo, fecha;
    FirebaseDatabase database;
    DatabaseReference puntoAcceso;
    //Grupo sanguíneo del paciente mostrado (lo necesito para borrarlo también de su grupo)
    String grupoActual;
    //Para distinguir "paciente borrado" de "paciente no encontrado"
    boolean borrando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        //Recupero el String de nuhsa que me pasó la anterior activity
        nuhsa = getIntent().getStringExtra(Constantes.nuhsa);

        //Busco las vistas que tengo que modificar en mi interfaz
        nombre = findViewById(R.id.tvNombre);
        apellidos = findViewById(R.id.tvApellidos);
        grupoSanguineo = findViewById(R.id.tvGrupoSanguineo);
        fecha = findViewById(R.id.tvFecha);

        //Instancia de mi base de datos
        database = FirebaseDatabase.getInstance();

        //Punto de acceso al nuhsa que se nos ha pasado
        puntoAcceso = database.getReference(Constantes.pacientes + "/" + nuhsa);

        //Listener que escucha si hay algún elemento colgando de ese punto de acceso
        puntoAcceso.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!nuhsa.equals("") && dataSnapshot.getValue() != null) {//Nuhsa encontrado
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    nombre.setText(paciente.getNombre());
                    apellidos.setText(paciente.getApellidos());
                    grupoSanguineo.setText(paciente.getGrupoSanguineo());
                    //Los pacientes aleatorios no tienen fecha de nacimiento
                    if (paciente.getFechaNacimiento() == null) {
                        fecha.setText("No indicada");
                    } else {
                        fecha.setText(paciente.getFechaNacimiento());
                    }
                    grupoActual = paciente.getGrupoSanguineo();
                } else {
                    if (borrando) {
                        Toast.makeText(BuscaActivity.this, "Paciente borrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BuscaActivity.this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    //OnClick asignado desde el Layout: pide confirmación y borra el paciente
    public void borraPaciente(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Borrar paciente")
                .setMessage("¿Seguro que quieres borrar a " + nombre.getText() + " " + apellidos.getText() + "?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    borrando = true;
                    //Borro la copia que cuelga de su grupo sanguíneo
                    if (grupoActual != null) {
                        database.getReference().child(grupoActual).child(nuhsa).removeValue();
                    }
                    //y el paciente de Pacientes (esto dispara el listener, que cierra la pantalla)
                    puntoAcceso.removeValue();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
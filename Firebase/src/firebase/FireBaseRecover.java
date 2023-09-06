package firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

public class FireBaseRecover {

    public static void main(String[] args) throws FileNotFoundException {

        new FireBaseRecover().recover();
    }

    private FirebaseDatabase firebaseDatabase;

    private void initFirebase() {

        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://final1-61460-default-rtdb.firebaseio.com/")
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\sofia\\OneDrive\\Escritorio\\final1Json.json")))
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexion exitosa....");
        } catch (RuntimeException ex) {
            System.out.println("Problema ejecucion ");
        } catch (FileNotFoundException ex) {
            System.out.println("Problema archivo");
        }
    }

    private void recover() {
        initFirebase();
        DatabaseReference databaseReference = firebaseDatabase.getReference("item");
        //System.out.println(firebaseDatabase.getReference("item"));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                System.out.println("ID: " + item.getId());
                System.out.println("Nombre: " + item.getName());
                System.out.println("Precio: " + item.getPrice());
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

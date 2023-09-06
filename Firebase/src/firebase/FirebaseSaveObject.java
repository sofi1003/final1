package firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;


public class FirebaseSaveObject {

    public static void main(String[] args) throws FileNotFoundException {
        Item item = new Item();
        item.setId(100L);
        item.setName("PruebaNetbeans");
        item.setPrice(100.0);

        // save item objec to firebase.
        new FirebaseSaveObject().save(item);
        //new FirebaseSaveObject().recover();
    }

    private FirebaseDatabase firebaseDatabase;

    /**
         * inicialización de firebase.
*/
    private void initFirebase() {
        
        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()   
                    .setDatabaseUrl("https://final1-61460-default-rtdb.firebaseio.com/")
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\sofia\\OneDrive\\Escritorio\\final1Json.json")))
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexion exitosa....");
        }catch (RuntimeException ex) {
            System.out.println("Problema ejecucion ");
        }catch (FileNotFoundException ex) {
            System.out.println("Problema archivo");
        }

         
    }

    /**
     * Save item object in Firebase.
     * @param item 
     */
    private void save(Item item) throws FileNotFoundException {
        if (item != null) {
            initFirebase();
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
            DatabaseReference childReference = databaseReference.child("item");
            CountDownLatch countDownLatch = new CountDownLatch(1);
            childReference.setValue(item, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("Registro guardado!");
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
            }
        }
    }
}

    

package firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.concurrent.CountDownLatch;


public class FirebasePushObject {
    
    public static void main(String[] args) {
        Item item = new Item();
        item.setId(2L);
        item.setName("motorola");
        item.setPrice(600.156);
        //test de prueba
        // You can use List<Item> also.
        new FirebasePushObject().saveUsingPush(item);
    }
    
    private FirebaseDatabase firebaseDatabase;

    /**
     * inicialización de firebase.
     */
    private void initFirebase() {
        
        try {
            
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    //.setDatabaseUrl("https://prueba-esp-a7c2a.firebaseio.com") // 
                    .setDatabaseUrl("https://final1-61460-default-rtdb.firebaseio.com/")
                    //.setServiceAccount(new FileInputStream(new File("C:\\Users\\Lenovo\\Documents\\pc\\NetBeansProjects\\firebase\\prueba-esp-a7c2a-firebase-adminsdk-yd7qe-1bdb100458.json")))
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\sofia\\OneDrive\\Escritorio\\final1Json.json")))

                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("La conexión se realizo exitosamente...");
            
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (RuntimeException ex) {   
            ex.printStackTrace();
        }
        
        
    }

   
    private void saveUsingPush(Item item) {
        if (item != null) {
            initFirebase();
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
            DatabaseReference childReference = databaseReference.child("item");
            CountDownLatch countDownLatch = new CountDownLatch(1);
            childReference.push().setValue(item, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("Registro subido exitosamente");
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
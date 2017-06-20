package com.woowstudio.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * This little snippet highlight a bug in Firebase related to persistence.
 *
 * 1. Clear database (using clear button)
 * 2. uninstall and install app
 * 3. Display all DB (using GET ALL button) => empty (OK)
 * 4. Display "last score" (using GET LAST SCORES button)
 * 5. Add a score (user1, 1)
 * 6. Display 'last score' => (user1, 1) => OK
 *
 * 8. uninstall and install app
 * 9.  Display "last score" => (user1, 1) => OK
 * 10. Add 3 new scores
 * 11. Display all DB (using GET ALL button)  => (user1, 1), (user2, 2), (user3, 3) => OK
 * 12.  Display "last score" => (user1, 1) => NOK
 *
 */
public class MainActivity extends AppCompatActivity {

    private int indexGenerator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseReference scoresReference = FirebaseDatabase.getInstance().getReference("scores").child("users");

        /**
         * Generate a new user along with a new score, starting at indexGenerator(1)
         */
        findViewById(R.id.add_score_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexGenerator++;
                System.out.println("User added : user" + indexGenerator + ", score:" + indexGenerator);
                scoresReference.child("user" + indexGenerator).child("score").setValue(indexGenerator);
            }
        });

        /**
         * Get the 3 highest scores.
         */
        findViewById(R.id.get_last_scores_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoresReference.orderByChild("score").limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("Last scores : " + dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        /**
         * Get all the database
         */
        findViewById(R.id.all_db_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoresReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("All database : " + dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoresReference.setValue(null);
            }
        });
    }
}

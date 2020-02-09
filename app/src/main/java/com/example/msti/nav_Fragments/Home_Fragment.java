package com.example.msti.nav_Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msti.MainActivity;
import com.example.msti.R;
import com.example.msti.adapters.SwipeCard_Adapter;
import com.example.msti.model.userData;
import com.example.msti.staticStrings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import static com.example.msti.staticStrings.TAG;
import static com.example.msti.staticStrings.USERS;

public class Home_Fragment extends Fragment {

    private List<userData> dataList = new ArrayList<>();
    SwipeFlingAdapterView flingContainer;

    SwipeCard_Adapter adapter;

    ImageView show_profilePic;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String REQUIRED_USER_TO_SHOW;
    private  String REQUIRED_USER_GENDER;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_home, container, false);
        adapter = new SwipeCard_Adapter(getActivity(), R.layout.swipe_card_item, dataList);
        flingContainer = view.findViewById(R.id.swipeCard);
        show_profilePic = view.findViewById(R.id.show_profilePic);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadDataFromFirebase();

        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                dataList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(getActivity(), "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getActivity(), "Clicked!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadDataFromFirebase() {

        dataList.clear();
        if (MainActivity.USER_GENDER.equals("MALE_USER")) {
            REQUIRED_USER_TO_SHOW = "FEMALE_USER";
            REQUIRED_USER_GENDER = "Female";
        } else {
            REQUIRED_USER_TO_SHOW = "MALE_USER";
            REQUIRED_USER_GENDER = "Male";
        }

        db.collection(USERS).document(REQUIRED_USER_GENDER).collection(REQUIRED_USER_TO_SHOW)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.isEmpty());
                for (DocumentSnapshot d : queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: in");
                    dataList.add(d.toObject(userData.class));
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


//2. reduce image size while uploading image to firebase
//3. how to show data properly on Home Fragment
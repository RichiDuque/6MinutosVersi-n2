package com.venta.a6minutosapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTwo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTwo newInstance(String param1, String param2) {
        FragmentTwo fragment = new FragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_two,container,false);
        final TextView enrightF=root.findViewById(R.id.text802);
        final TextView troosterF=root.findViewById(R.id.text102);
        final TextView gibbonsF=root.findViewById(R.id.text1002);
        SharedPreferences preferences= getContext().getSharedPreferences("usur",MODE_PRIVATE);

        enrightF.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Ha escogido Enright, género Femenino", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Walk.class);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("formula","Enright");
            editor.commit();
            startActivity(intent);
        });
        troosterF.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Ha escogido Trooster, género Femenino", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("formula","Trooster");
            editor.commit();
            Intent intent = new Intent(getContext(), Walk.class);
            startActivity(intent);
        });
        gibbonsF.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Ha escogido Gibbons, género Femenino", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("formula","Gibbons");
            editor.commit();
            Intent intent = new Intent(getContext(), Walk.class);
            startActivity(intent);
        });
        // Inflate the layout for this fragment
        return root;
    }
}
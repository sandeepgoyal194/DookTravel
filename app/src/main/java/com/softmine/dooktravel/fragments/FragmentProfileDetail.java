package com.softmine.dooktravel.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileDetail extends Fragment {


    EditText etFirstName,etMiddleName,etLastName,etAddress,etContact,etSkype,etDescription;

    public FragmentProfileDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFirstName=(EditText)view.findViewById(R.id.edFirstname);
        etMiddleName=(EditText)view.findViewById(R.id.edMiddleName);

        etLastName=(EditText)view.findViewById(R.id.edlastName);

        etAddress=(EditText)view.findViewById(R.id.edAddress);


        etContact=(EditText)view.findViewById(R.id.edContact);

        etSkype=(EditText)view.findViewById(R.id.edSkype);

        etDescription=(EditText)view.findViewById(R.id.edDescription);

        etFirstName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etMiddleName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etLastName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etContact.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etSkype.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDescription.setTypeface(Utils.getRegularTypeFace(getActivity()));



    }
}

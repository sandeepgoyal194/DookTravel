package com.softmine.dooktravel.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.adapter.AdapterSearchResult;
import com.softmine.dooktravel.pojos.Model;
import com.softmine.dooktravel.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearchResult extends Fragment {

    private RecyclerView recyclerView;
    private AdapterSearchResult adapterSearchResult;
    private List<Model> modelList;
    Spinner spnCountry, spnState, spnCategory;
    Button btnGo;
    TextView tvSearchResult;
    LinearLayoutManager mLayoutManager;

    public FragmentSearchResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        btnGo = (Button) view.findViewById(R.id.search_go_btn);
        btnGo.setOnClickListener(mGoClickListner);
        spnCountry = (Spinner) view.findViewById(R.id.spinner_country);
        spnState = (Spinner) view.findViewById(R.id.spinner_state);
        spnCategory = (Spinner) view.findViewById(R.id.spinner_filter_category);
        tvSearchResult=(TextView)view.findViewById(R.id.tvSearchResult) ;
        ArrayAdapter countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.country, R.layout.spinner_item);

        countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnCountry.setAdapter(countryAdapter);
        ArrayAdapter stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.state, R.layout.spinner_item);
        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnState.setAdapter(stateAdapter);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.category, R.layout.spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnCategory.setAdapter(categoryAdapter);

        btnGo.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        tvSearchResult.setTypeface(Utils.getRegularTypeFace(getActivity()));

    }

    View.OnClickListener mGoClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adapterSearchResult = new AdapterSearchResult(getDataSet(),getActivity());
            recyclerView.setAdapter(adapterSearchResult);
        }
    };


    private ArrayList<Model> getDataSet() {
        ArrayList results = new ArrayList<Model>();
        for (int index = 0; index < 6; index++) {
            Model obj = new Model("Sandeep Gupta", "Dook Travels pvt Ltd",
                    "New Delhi, India");
            results.add(index, obj);
        }
        return results;
    }
}

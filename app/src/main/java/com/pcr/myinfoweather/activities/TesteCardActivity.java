package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.adapters.SearchResultListAdapter;
import com.pcr.myinfoweather.models.user.UserAdress;
import com.pcr.myinfoweather.request.UserLocation;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Paula on 24/05/2015.
 */
public class TesteCardActivity extends BaseActivity{

    private RecyclerView.LayoutManager layoutManager;
    private SearchResultListAdapter adapterSearch;

    @InjectView(R.id.addressResultList) RecyclerView searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<UserAdress> addressList = UserLocation.getInstance(this).getAddress("sao car");

        layoutManager = new LinearLayoutManager(this);
        searchResultList.setLayoutManager(layoutManager);

        adapterSearch = new SearchResultListAdapter(addressList);
        searchResultList.setAdapter(adapterSearch);
        //searchResultList.setVisibility(View.VISIBLE);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.teste_recyclerview_layout;
    }

    @Override
    protected boolean checkSessionOnStart() {
        return false;
    }
}

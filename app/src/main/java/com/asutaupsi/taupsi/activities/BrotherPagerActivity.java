package com.asutaupsi.taupsi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.asutaupsi.taupsi.R;
import com.asutaupsi.taupsi.entities.Brother;
import com.asutaupsi.taupsi.fragments.BrotherDetailsFragment;
import com.asutaupsi.taupsi.infrastructure.TauPsiApplication;
import com.asutaupsi.taupsi.services.ServiceCalls;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class BrotherPagerActivity extends BaseActivity {
    private ArrayList<Brother> brothers;
    private ViewPager viewPager;
    private  static final String BROTHER_EXTRA_INFO = "BROTHER_EXTRA_INFO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brother_pager);

        brothers = new ArrayList<>();
        bus.post(new ServiceCalls.SearchBrothersRequest(TauPsiApplication.BROTHER_REFERENCE));

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.activity_brother_viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Brother brother = brothers.get(position);
                return BrotherDetailsFragment.newInstance(brother);
            }

            @Override
            public int getCount() {
                return brothers.size();
            }


        });
    }





    @Subscribe
    public void onBrosLoad(final ServiceCalls.SearchBrothersResponse response){
       int oldSize = brothers.size();
        if (oldSize ==0) {
            brothers.clear();
            brothers.addAll(response.Brothers);
            viewPager.getAdapter().notifyDataSetChanged();
        }


        Brother brother = getIntent().getParcelableExtra(BROTHER_EXTRA_INFO);
        int brotherId = brother.getBrotherId();

        for(int i=0;i<brothers.size();i++){
            if(brothers.get(i).getBrotherId() == brotherId){
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }


    public static Intent newIntent(Context context, Brother brother){
        Intent intent = new Intent(context,BrotherPagerActivity.class);
        intent.putExtra(BROTHER_EXTRA_INFO,brother);
        return intent;
    }

}

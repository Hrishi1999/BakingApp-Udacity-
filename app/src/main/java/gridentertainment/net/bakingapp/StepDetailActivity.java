package gridentertainment.net.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Bundle bundle = getIntent().getBundleExtra("bundle2");
        stepDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null)
        {
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

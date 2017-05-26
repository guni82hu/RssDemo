package rssdemo.andras.hu.rssdemo.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import rssdemo.andras.hu.rssdemo.R;
import rssdemo.andras.hu.rssdemo.data.Subscription;
import rssdemo.andras.hu.rssdemo.databinding.ActivityFeedBinding;
import rssdemo.andras.hu.rssdemo.di.Injector;
import rssdemo.andras.hu.rssdemo.repository.FeedRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//7 pomodoros
public class FeedActivity extends AppCompatActivity implements FeedView {

    private static final long DRAWER_CLOSE_DELAY_MS = 200;

    public FeedViewModel viewModel;
    private ActivityFeedBinding binding;
    private ActionBarDrawerToggle drawerToggle;
    private FeedAdapter adapter;
    private final Handler drawerActionHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed);
        setSupportActionBar(binding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initDrawer();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedAdapter();
        binding.recyclerView.setAdapter(adapter);
        viewModel.setView(this);
    }

    @Override
    public FeedAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void populateDrawerMenu(List<Subscription> subscriptions) {
        final Menu menu = binding.navigation.getMenu();
        for (Subscription subscription : subscriptions) {
            menu.add(subscription.getName());
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.errror_load_feed, Toast.LENGTH_SHORT).show();
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        binding.navigation.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(final MenuItem menuItem) {
        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening. This may seem a bit hacky but in practice
        // it works noticeably faster than listening to the onDrawerClosed callback
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        drawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewModel.onDrawerMenuSelection(menuItem.getTitle());
            }
        }, DRAWER_CLOSE_DELAY_MS);

        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

package com.classyinc.classytreasurer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;

//import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import com.classyinc.classytreasurer.fragments.DashBoardFragment;
import com.classyinc.classytreasurer.fragments.ExpenseFragment;
import com.classyinc.classytreasurer.fragments.IncomeFragment;
import com.classyinc.classytreasurer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.classyinc.classytreasurer.R.id.mainframe;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    DatabaseReference usermail;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Classy Treasurer");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usermail = db.getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usermail.keepSynced(true);

        String email = null;
        if (firebaseUser != null) {

            email = firebaseUser.getEmail();

        }

        bottomNavigationView = findViewById(R.id.bottomnavbar_id);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView navmail = header.findViewById(R.id.txt_nav_header_email_id);
        navmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);


        Fragment frag = new DashBoardFragment();
        getSupportFragmentManager().beginTransaction()

                .replace(R.id.mainframe, frag)
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.dashboard:

                        Fragment frag = new DashBoardFragment();
                        getSupportFragmentManager().beginTransaction()

                                .add(new ExpenseFragment(), "ExpenseFragment")
                                .replace(R.id.mainframe, frag)
                                .addToBackStack("ExpenseFragment")
                                .commit();

                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;

                    case R.id.income:

                        Fragment frag1 = new IncomeFragment();

                            getSupportFragmentManager().beginTransaction()

                                    .add(new DashBoardFragment(), "DashBoardFragment")
                                    .replace(R.id.mainframe, frag1)
                                    .addToBackStack("DashBoardFragment")
                                    .commit() ;

                        bottomNavigationView.setItemBackgroundResource(R.color.incomefragment);
                        return true;

                    case R.id.expense:

                        Fragment frag2 = new ExpenseFragment();
                        getSupportFragmentManager().beginTransaction()

                                .replace(R.id.mainframe, frag2)
                                .add(new IncomeFragment(), "IncomeFragment")
                                .addToBackStack("IncomeFragment")
                                .commit();

                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

       /* Intent a  = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a); */

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

     /*  if (backPressedTime + 2000 > System.currentTimeMillis()){
            backtoast.cancel();
            super.onBackPressed();
            return;
        }else {
           backtoast= Toast.makeText(getBaseContext(), "Press back again to exit!", Toast.LENGTH_SHORT);
           backtoast.show();
        }
        backPressedTime = System.currentTimeMillis(); */
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void  displaySelectedListener(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.dashboard:

                fragment = new DashBoardFragment();
                break;

            case R.id.income:
                fragment = new IncomeFragment();
                break;

            case R.id.expense:
                fragment = new ExpenseFragment();
                break;

            case R.id.FinancialTip:
                startActivity(new Intent(getApplicationContext(),RewardActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                break;

            case R.id.developer_info:

                startActivity(new Intent(getApplicationContext(), Developer.class));
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                break;

            case R.id.request:

                startActivity(new Intent(getApplicationContext(),RequestFeature.class));
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                break;

            case R.id.logout_id:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                break;
        }

        if(fragment!=null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(mainframe,fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }

}

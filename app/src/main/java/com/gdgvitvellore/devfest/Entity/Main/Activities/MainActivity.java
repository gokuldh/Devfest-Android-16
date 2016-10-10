package com.gdgvitvellore.devfest.Entity.Main.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdgvitvellore.devfest.Control.Animations.Main.DrawerCircularReveal;
import com.gdgvitvellore.devfest.Control.Animations.Main.ObjectAnimations;
import com.gdgvitvellore.devfest.Entity.About.Fragments.AboutFragment;
import com.gdgvitvellore.devfest.Entity.Actors.DrawerItem;
import com.gdgvitvellore.devfest.Entity.Timeline.Fragments.TimelineFragment;
import com.gdgvitvellore.devfest.gdgdevfest.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TextView titleView;
    private RecyclerView drawerRecycler;
    private LinearLayout drawer, drawerTrigger;
    private LinearLayout fragmentHolder;
    private ImageView arrowIcon;

    private DrawerAdapter drawerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isDrawerOpened = false;

    private List<DrawerItem> drawerItems;
    private int lastFragmentSelected = -1;
    private String[] toolbarTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setInit();
        setData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.title);
        drawer = (LinearLayout) findViewById(R.id.drawer);
        drawerTrigger = (LinearLayout) findViewById(R.id.drawer_trigger);
        fragmentHolder = (LinearLayout) findViewById(R.id.fragment_holder);
        drawerRecycler = (RecyclerView) findViewById(R.id.toolbar_recycler);
        arrowIcon = (ImageView) findViewById(R.id.arrow);

        linearLayoutManager = new LinearLayoutManager(this);

        drawerItems = new ArrayList<>();
        toolbarTitles=getResources().getStringArray(R.array.drawer_titles);
    }

    private void setInit() {
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        drawerRecycler.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        drawerTrigger.setOnClickListener(this);

        TypedArray drawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);
        for (int i = 0; i < toolbarTitles.length; i++) {
            //TODO To change the default icon later
            drawerItems.add(new DrawerItem(toolbarTitles[i], drawerIcons.getResourceId(i, R.drawable.ic_default)));
        }
    }

    private void setData() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerRecycler.setAdapter(drawerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragment(0);
    }

    private void setFragment(int i) {

        FragmentManager manager = getSupportFragmentManager();
        if (i == lastFragmentSelected) {
            toggle();
            return;
        } else {
            switch (i) {
                case 0:
                    Fragment timelineFragment = new TimelineFragment();
                    manager.beginTransaction().replace(R.id.fragment_holder, timelineFragment, TimelineFragment.class.getSimpleName()).commit();
                    break;
                case 4:
                    Fragment aboutFragment = new AboutFragment();
                    manager.beginTransaction().replace(R.id.fragment_holder, aboutFragment, AboutFragment.class.getSimpleName()).commit();
            }
        }
    }


    private void setToolbarTitle(int position) {
        titleView.setText(toolbarTitles[position]);
    }

    private void toggle() {

        if (isDrawerOpened) {
            if (Build.VERSION.SDK_INT >= 21) {
                Animator animator = DrawerCircularReveal.destroyCircularRevealDrawer(drawer, 0, 0);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        drawer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                ObjectAnimations.drawerArrowAnimator(arrowIcon, ObjectAnimations.Position.DOWN).start();
                ObjectAnimations.fragmentHolderAnimator(fragmentHolder, drawer.getHeight(),0,300).start();

            } else {
                drawer.setVisibility(View.GONE);
            }
            isDrawerOpened = false;
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                drawer.setVisibility(View.VISIBLE);
                DrawerCircularReveal.circularRevealDrawer(drawer, 0, 0).start();
                ObjectAnimations.drawerArrowAnimator(arrowIcon, ObjectAnimations.Position.UP).start();
                ObjectAnimations.fragmentHolderAnimator(fragmentHolder,0,drawer.getHeight(),0).start();
            } else {
                drawer.setVisibility(View.VISIBLE);
            }
            isDrawerOpened = true;
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer_trigger:
                toggle();
        }
    }


    private class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
        private final List<DrawerItem> data;
        private LayoutInflater inflater;
        private Context c;

        public DrawerAdapter(Context context, List<DrawerItem> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public DrawerAdapter.DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.activity_main_drawer_item, parent, false);
            DrawerViewHolder drawerViewHolder = new DrawerViewHolder(view);
            return drawerViewHolder;
        }


        @Override
        public void onBindViewHolder(DrawerViewHolder holder, int position) {
            DrawerItem item = data.get(position);
            holder.title.setText(item.getTitle());
            holder.icon.setImageResource(item.getIconId());
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        public void remove(int position) {
            data.remove(position);
        }

        class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView title;
            private ImageView icon;

            public DrawerViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                toggle();
                setFragment(getAdapterPosition());
                setToolbarTitle(getAdapterPosition());
            }
        }
    }

}
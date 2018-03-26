package aksamedia.wikiloka.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aksamedia.wikiloka.Activity.activity_daftar;
import aksamedia.wikiloka.Activity.activity_login;
import aksamedia.wikiloka.R;

/**
 * Created by Jinesh on 12-12-2016.
 */

public class custom_navview_member extends ScrimInsetsFrameLayout {
    private ScrollView scrollView;
    private ListAdapter adapter;
    private Drawable background;
    private LinearLayout linearLayout;
    private int backGroundColor = 0xffe2e2e2;
    View[] childView;
    private AppCompatActivity context;
    private static NavigationItemSelectedListner navigationItemSelectedListner;
    private static ChildNavigationItemSelectedListner childnavigationItemSelectedListner;
    private LinearLayout lay_child;
    private ImageView icon_collapse;
    private RelativeLayout child_product, layout_after_login, layout_before_login;
    public static boolean isLogin = false;

    public static void setIsLogin(boolean isLogin) {
        custom_navview_member.isLogin = isLogin;
    }


    public custom_navview_member(Context context) {
        super(context);
        init(context);

    }


    public custom_navview_member(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        LayoutParams listParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView = new ScrollView(context);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(listParams);
        listParams.gravity = Gravity.START;
        scrollView.setLayoutParams(listParams);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        if (background != null)
            setBackground(background);
        else
            setBackgroundColor(0xffffffff);
        scrollView.addView(linearLayout);
        addView(scrollView);
        setFitsSystemWindows(true);

    }


    public void setAdapter(final ListAdapter adapter, final AppCompatActivity context, final String[] sub_kategori) {
        this.adapter = adapter;
        final int defColor = 0xffffffff;
        childView = new View[adapter.getCount()];

        for (int index = 0; index < adapter.getCount(); index++) {
            childView[index] = adapter.getView(index, null, this);
            childView[index].setTag(index);
            if (index>1&index<7)
                childView[index].setVisibility(GONE);
            if (index == 7) {
                final int finalIndex = index;
                lay_child = (LinearLayout) childView[index].findViewById(R.id.lay_child);
                icon_collapse = (ImageView) childView[index].findViewById(R.id.exp_col);
                for (int i = 0; i < sub_kategori.length; i++) {
                    final int j = i;
                    View child = context.getLayoutInflater().inflate(R.layout.list_childmenumember, null);
                    child_product = (RelativeLayout) child.findViewById(R.id.child_product);
                    TextView text = (TextView) child.findViewById(R.id.teks);
                    text.setText(sub_kategori[i]);
                    child_product.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (childnavigationItemSelectedListner != null) {
                                childnavigationItemSelectedListner.onChildItemSelected(child_product, j);
                            }
                        }
                    });
                    lay_child.addView(child);
                }

                final Boolean[] status_collapse = {true};

                childView[index].findViewById(R.id.gambar).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status_collapse[0]) {
                            status_collapse[0] = false;
                            icon_collapse.setImageResource(R.drawable.ic_down_collaps);
                            icon_collapse.setRotationX(-90);
                            lay_child.setVisibility(VISIBLE);
                        } else {
                            status_collapse[0] = true;
                            icon_collapse.setImageResource(R.drawable.ic_up_collaps);
                            icon_collapse.setRotationX(90);
                            lay_child.setVisibility(GONE);
                        }
                        for (int innerIndex = 0; innerIndex < adapter.getCount(); innerIndex++) {
                            if ((int) childView[innerIndex].getTag() != finalIndex) {
                                childView[innerIndex].setBackgroundColor(defColor);
                            }
                        }

                        if (navigationItemSelectedListner != null)
                            navigationItemSelectedListner.onItemSelected(childView[finalIndex], finalIndex);

                    }
                });
                childView[index].findViewById(R.id.teks).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status_collapse[0]) {
                            status_collapse[0] = false;
                            icon_collapse.setImageResource(R.drawable.ic_down_collaps);
                            lay_child.setVisibility(VISIBLE);
                        } else {
                            status_collapse[0] = true;
                            icon_collapse.setImageResource(R.drawable.ic_up_collaps);
                            lay_child.setVisibility(GONE);
                        }
                        for (int innerIndex = 0; innerIndex < adapter.getCount(); innerIndex++) {
                            if ((int) childView[innerIndex].getTag() != finalIndex) {
                                childView[innerIndex].setBackgroundColor(defColor);
                            }
                        }

                        if (navigationItemSelectedListner != null)
                            navigationItemSelectedListner.onItemSelected(childView[finalIndex], finalIndex);

                    }
                });
                childView[index].findViewById(R.id.exp_col).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status_collapse[0]) {
                            status_collapse[0] = false;
                            icon_collapse.setImageResource(R.drawable.ic_down_collaps);
                            lay_child.setVisibility(VISIBLE);
                        } else {
                            status_collapse[0] = true;
                            icon_collapse.setImageResource(R.drawable.ic_up_collaps);
                            lay_child.setVisibility(GONE);
                        }
                        for (int innerIndex = 0; innerIndex < adapter.getCount(); innerIndex++) {
                            if ((int) childView[innerIndex].getTag() != finalIndex) {
                                childView[innerIndex].setBackgroundColor(defColor);
                            }
                        }

                        if (navigationItemSelectedListner != null)
                            navigationItemSelectedListner.onItemSelected(childView[finalIndex], finalIndex);

                    }
                });
            } else {
                childView[index] = adapter.getView(index, null, this);
                childView[index].setTag(index);
                final int finalIndex = index;
                childView[index].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int innerIndex = 0; innerIndex < adapter.getCount(); innerIndex++) {
                            if ((int) childView[innerIndex].getTag() != finalIndex) {
                                childView[innerIndex].setBackgroundColor(defColor);
                            } else {
                                childView[innerIndex].setBackgroundColor(backGroundColor);
                            }
                        }
                        if (navigationItemSelectedListner != null)
                            navigationItemSelectedListner.onItemSelected(childView[finalIndex], finalIndex);

                    }
                });
            }
            linearLayout.addView(childView[index]);
        }

    }


    public void setMenuLogin() {

    }

    public void setMenuLogOut() {
    }


    public void setHeaderView(View view, int marginBottom) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, marginBottom);
        view.setLayoutParams(layoutParams);
        linearLayout.addView(view, 0);

        layout_after_login = (RelativeLayout) linearLayout.findViewById(R.id.after_login);
        layout_before_login = (RelativeLayout) linearLayout.findViewById(R.id.before_login);

        if (isLogin) {
            layout_before_login.setVisibility(GONE);
            layout_after_login.setVisibility(VISIBLE);
        } else {
            layout_before_login.setVisibility(VISIBLE);
            layout_after_login.setVisibility(GONE);
        }


        RelativeLayout btn_login = (RelativeLayout) linearLayout.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.getContext().startActivity(new Intent(linearLayout.getContext(), activity_login.class));
            }
        });

        RelativeLayout btn_register = (RelativeLayout) linearLayout.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.getContext().startActivity(new Intent(linearLayout.getContext(), activity_daftar.class));
            }
        });
    }

    public void setBackGround(Drawable backGround) {
        background = backGround;
        setBackground(backGround);
    }


    public void setBackGround(int backGround) {
        setBackgroundColor(backGround);
    }


    public void setSelectionBackGround(int color) {
        backGroundColor = color;
    }


    public ListAdapter getAdapter() {
        return adapter;
    }


    public interface NavigationItemSelectedListner {
        void onItemSelected(View view, int position);
    }

    public interface ChildNavigationItemSelectedListner {
        void onChildItemSelected(View view, int position);
    }


    public void setOnNavigationItemSelectedListner(NavigationItemSelectedListner navigationItemSelectedListner) {
        custom_navview_member.navigationItemSelectedListner = navigationItemSelectedListner;
    }

    public void setOnChildNavigationItemSelectedListner(ChildNavigationItemSelectedListner childnavigationItemSelectedListner) {
        custom_navview_member.childnavigationItemSelectedListner = childnavigationItemSelectedListner;
    }

}
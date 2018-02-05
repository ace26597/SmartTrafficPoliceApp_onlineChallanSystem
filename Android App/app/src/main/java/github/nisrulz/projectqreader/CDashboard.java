package github.nisrulz.projectqreader;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

public class CDashboard extends AppCompatActivity {
    String name = "";
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(Html.fromHtml("<font color='#ffffff'>Customer DashBoard</font>"));
        initCollapsingToolbar();

        Intent i = getIntent();
        String pname = i.getStringExtra("name");

        String amt = i.getStringExtra("amt");
        Alerter.create(this)
                .setTitle("Welcome - " + pname)
                .setText("Your available balence is " + amt)
                .setBackgroundColor(R.color.accent)
                .setDuration(10000)
                .show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        name = sharedpref.getString("username", "");

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover1).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(CDashboard.this, Preface.class));
                                break;
                            case 1:
                                startActivity(new Intent(CDashboard.this, PunePoliceBranch.class));
                                break;
                            case 2:
                                startActivity(new Intent(CDashboard.this, TrafficBranchPune.class));
                                break;
                            case 3:
                                startActivity(new Intent(CDashboard.this, QRCodegeneration.class));
                                break;
                            case 4:
                                startActivity(new Intent(CDashboard.this, Upload_img.class));
                                break;
                            case 5:
                                startActivity(new Intent(CDashboard.this, Expand.class));
                                break;
                            case 6:
                                startActivity(new Intent(CDashboard.this, UserDocDetails.class));
                                break;
                            case 7:
                                startActivity(new Intent(CDashboard.this, DoesActivity.class));
                                break;
                            case 8:
                                startActivity(new Intent(CDashboard.this, Complaint.class));
                                break;
                            case 9:
                                Intent i = new Intent(CDashboard.this, FeedBackActivity.class);
                                i.putExtra("value", name);
                                startActivity(i);

                            default:
                                break;
                        }
                    }
                })
        );

    }

    public void ShowAlertDialogWithListview() {
        List<String> mlist = new ArrayList<String>();
        mlist.add("Police");
        mlist.add("Hospital");

        //Create sequence of items
        final CharSequence[] list = mlist.toArray(new String[mlist.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Emergency Call");
        dialogBuilder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String tel = "9503531792";//Hospital contact number

                if (item == 0) {
                    tel = "7040237207";   //Police contact number

                }


                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + tel)); // you
                    // put
                    // desired
                    // phone
                    // number
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("dialing example", "Call failed", e);

                }
            }

        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //appBarLayout.setExpanded(true);
        appBarLayout.animate();
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.english1,
                R.drawable.english2,
                R.drawable.english3,
                R.drawable.english4,
                R.drawable.english8,
                R.drawable.english5,
                R.drawable.english10,
                R.drawable.english16,
                R.drawable.english11,
                R.drawable.english19,
                R.drawable.album11};


        Album a = new Album("PREFECE", "App Information", covers[0]);
        albumList.add(a);

        a = new Album("PUNE CITY POLICE", "Police Dashboard", covers[1]);
        albumList.add(a);

        a = new Album("TRAFFIC BRANCH", "Traffic Officers Info", covers[2]);
        albumList.add(a);

        a = new Album("Generate QR Code", "Get Secure", covers[3]);
        albumList.add(a);

        a = new Album("Upload Document", "General Info/Notice", covers[4]);
        albumList.add(a);

        a = new Album("TRAFFIC VIOLATION", "Rules And Regulation", covers[5]);
        albumList.add(a);

        a = new Album("Basic Queries", "General Enquiries", covers[6]);
        albumList.add(a);

        a = new Album("DO'S/DON'TS", "Traffic Rules", covers[7]);
        albumList.add(a);

        a = new Album("Emergency Contacts", "Get Closure", covers[8]);
        albumList.add(a);

        a = new Album("FEEDBACK", "Show Suggestion", covers[9]);
        albumList.add(a);


        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

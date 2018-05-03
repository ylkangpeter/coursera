package course.labs.fragmentslab;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements
        FriendsFragment.SelectionListener {

    private static final String TAG = "Lab-Fragments";

    private FriendsFragment mFriendsFragment;
    private FeedFragment mFeedFragment;

    private FrameLayout layout;
    private LinearLayout linLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // If the layout is single-pane, create the FriendsFragment
        // and add it to the Activity

        layout = (FrameLayout) findViewById(R.id.fragment_container);
        linLayout = (LinearLayout) findViewById(R.id.frags);

        if (!isInTwoPaneMode()) {

            mFriendsFragment = new FriendsFragment();

            //TODO 1 - add the FriendsFragment to the fragment_container

            // Begin a new FragmentTransaction
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();

            // Add the FriendsFragment
            fragmentTransaction.addToBackStack(null).add(R.id.fragment_container, mFriendsFragment);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

//            getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                @Override
//                public void onBackStackChanged() {
//                    setLayout();
//                }
//            });

        } else {

            // Otherwise, save a reference to the FeedFragment for later use

            mFeedFragment = (FeedFragment) getFragmentManager()
                    .findFragmentById(R.id.feed_frag);
        }

    }

//    private void setLayout() {
//        layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
//
//        if (isInTwoPaneMode()) {
//            // Make the TitleFragment occupy the entire layout
//            layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
//        } else {
//            linLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
//        }
//
//    }
    // If there is no fragment_container ID, then the application is in
    // two-pane mode

    private boolean isInTwoPaneMode() {

        return findViewById(R.id.fragment_container) == null;

    }

    // Display selected Twitter feed

    public void onItemSelected(int position) {

        Log.i(TAG, "Entered onItemSelected(" + position + ")");

        // If there is no FeedFragment instance, then create one

        if (mFeedFragment == null)
            mFeedFragment = new FeedFragment();

        // If in single-pane mode, replace single visible Fragment

        if (!isInTwoPaneMode()) {

            //TODO 2 - replace the fragment_container with the FeedFragment
            // Begin a new FragmentTransaction
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();

            // Add the FriendsFragment
            fragmentTransaction.addToBackStack(null).replace(R.id.fragment_container, mFeedFragment);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // execute transaction now
            getFragmentManager().executePendingTransactions();

        }

        // Update Twitter feed display on FriendFragment
        mFeedFragment.updateFeedDisplay(position);

    }

}

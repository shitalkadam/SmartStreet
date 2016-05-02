package com.example.android.smartstreet;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewListActivity extends AppCompatActivity {

    public static final String MQ = "MQ";
    private ReviewArrayAdapter mAdapter;
    private ListView listView;
    private String myUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        myUsername = bundle.getString("username");

        setContentView(R.layout.review_list_view);
        listView = (ListView)findViewById(R.id.list_view);
        final ImageButton button = (ImageButton) findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });
        mAdapter = new ReviewArrayAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener());
    }

    //to add to the git
    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final UserReview currentQuote = (UserReview) listView.getAdapter().getItem(position);
                DialogFragment df = new DialogFragment() {
                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.dialog_edit, container);
                        getDialog().setTitle(getString(R.string.edit_dialog_title));
                        final Button cancelButton = (Button) view.findViewById(R.id.add_dialog_cancel);
                        final TextView usernameEditText = (TextView) view.findViewById(R.id.add_dialog_usename);
                        final TextView reviewEditText = (TextView) view.findViewById(R.id.add_dialog_review);
                        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.add_ratingBar);
                        // pre-populate
                        usernameEditText.setText("UserName/EmailID: "+currentQuote.getUsername());
                        reviewEditText.setText("Review:"+"\n\n"+currentQuote.getReview());
                        ratingBar.setRating(currentQuote.getRating().floatValue());

                        TextWatcher textWatcher = new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String usernameTitleText = usernameEditText.getText().toString();
                                String reviewQuoteText = reviewEditText.getText().toString();
                                float rating_value = ratingBar.getRating();
                                mAdapter.updateItem(currentQuote, usernameTitleText, reviewQuoteText, rating_value);
                            }
                        };

                        usernameEditText.addTextChangedListener(textWatcher);
                        reviewEditText.addTextChangedListener(textWatcher);

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });
                        return view;
                    }
                };
                df.show(getFragmentManager(), "");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addItem() {
        DialogFragment df = new DialogFragment() {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.dialog_add, container);
             //   getDialog().setTitle("Add a Review and Rating");
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                final Button confirmButton = (Button) view.findViewById(R.id.add_dialog_ok);
                final Button cancelButton = (Button) view.findViewById(R.id.add_dialog_cancel);
                final TextView userTextView = (TextView) view.findViewById(R.id.add_dialog_usename);
                //UserProfile profile = getIntent().getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
                userTextView.setText(myUsername);
                final EditText reviewQuoteEditText = (EditText) view.findViewById(R.id.add_dialog_review);
                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.add_ratingBar);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reviewQuoteText = reviewQuoteEditText.getText().toString();
                        float rating = ratingBar.getRating();
                        Toast.makeText(ReviewListActivity.this,
                                "Got the review and rating and quote " + myUsername + ":" + reviewQuoteText, Toast.LENGTH_LONG)
                                .show();

                        UserReview currentQuote = new UserReview(null, myUsername, reviewQuoteText, rating);
                        currentQuote.setUsername(myUsername);
                        mAdapter.addItem(currentQuote);
                        dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                return view;
            }
        };
        df.show(getFragmentManager(), "");
    }
}



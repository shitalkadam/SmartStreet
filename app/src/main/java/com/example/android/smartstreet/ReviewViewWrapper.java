package com.example.android.smartstreet;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by sesharika on 4/24/16.
 */
class ReviewViewWrapper {
    View base;
    RatingBar rate=null;
    TextView commnet=null;
    TextView label=null;

    ReviewViewWrapper(View base) {
        this.base=base;
    }

    RatingBar getRatingBar() {
        if (rate==null) {
            rate=(RatingBar)base.findViewById(R.id.rate);
        }
        return(rate);
    }

    TextView getCommnet() {
        if (commnet==null) {
            commnet=(TextView)base.findViewById(R.id.comment);
        }
        return(commnet);
    }
    TextView getLabel() {
        if (label==null) {
            label=(TextView)base.findViewById(R.id.label);
        }
        return(label);
    }
}

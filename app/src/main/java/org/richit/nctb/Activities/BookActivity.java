package org.richit.nctb.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.richit.nctb.AdUtils.MyAdsListener;
import org.richit.nctb.AdUtils.MyAllAdsUtil;
import org.richit.nctb.Adapters.BookAdapter;
import org.richit.nctb.Models.Book;
import org.richit.nctb.R;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    //Views
    Toolbar toolbar;
    RecyclerView recyclerViewBook;
    LinearLayout linearLayoutAds;

    //Adapter Class
    BookAdapter bookAdapter;

    //Data variables
    ArrayList<Book> bookArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //Initializing Views
        recyclerViewBook = findViewById(R.id.recyclerView_book);
        toolbar = findViewById(R.id.toolbar);
        linearLayoutAds = findViewById(R.id.adsLL);

        //Retrieving data from Book adapter and set it into array list
        bookArrayList = getIntent().getParcelableArrayListExtra("BookList");

        //Getting the position of the student class
        int position = getIntent().getIntExtra("position", 0);
        setSupportActionBar(toolbar);

        //Setting toolbar title
        toolbar.setTitle(R.string.app_name);
        position++;

        //Books of class 9 and 10 are same so that if student class is 9 then
        //we set the subtitle of the toolbar for both class 9 and 10
        toolbar.setSubtitle("Class " + (position == 9 ? "9 and 10" : "" + position));

        //Initializing adapter
        bookAdapter = new BookAdapter(bookArrayList, this);

        //Setting view
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBook.setAdapter(bookAdapter);

        //Showing Home button at toolbar and setting its functionality
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Aetting ads
        adsAdder();

        //populateList();


        //String className = getIntent().getStringExtra("ClassName");


//        String data = "";
//        for (int i = 0; i < bookArrayList.size(); i++) {
//            data = data + bookArrayList.get(i).getBookName() + "\n";
//        }
//        textView_title.setText(data);
    }
//    public void populateList(){
//        bookArrayList = getIntent().getParcelableArrayListExtra("BookList");
//        bookAdapter.replaceArrayList(bookArrayList);
//    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Checking if item is clicked for home button at the toolbar
        if (item.getItemId() == android.R.id.home) {

            //if home button selected then finish the activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void adsAdder() {

        //Showing banner ads
        MyAllAdsUtil.addAnyBannerAd(true, this, true, linearLayoutAds, new MyAdsListener());
    }
}

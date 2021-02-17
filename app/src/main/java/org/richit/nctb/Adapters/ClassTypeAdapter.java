package org.richit.nctb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.richit.nctb.Activities.BookActivity;
import org.richit.nctb.AdUtils.MyAdsListener;
import org.richit.nctb.AdUtils.MyAllAdsUtil;
import org.richit.nctb.Models.ClassType;
import org.richit.nctb.R;

import java.util.ArrayList;

public class ClassTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LIST_AD_DELTA = 5;
    private static final int CONTENT = 0;
    private static final int AD = 1;

    private Context context;
    private ArrayList<ClassType> classeModelArrayList = new ArrayList<>();

    private int classImgs[] = {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six,
            R.drawable.seven,
            R.drawable.eight,
            R.drawable.nine
    };

    public ClassTypeAdapter(Context context, ArrayList<ClassType> classeModels) {
        this.context = context;
        this.classeModelArrayList = classeModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT) {
            return new ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.item_layout2, parent, false));
        } else {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.banner_ad_container, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == CONTENT) {
            ViewHolderClass viewHolderClass = (ViewHolderClass) holder;

            final ClassType classeModel = classeModelArrayList.get(getRealPosition(position));

            viewHolderClass.textView_title.setText("Class " + classeModel.getClassName());
            viewHolderClass.textView_bookCount.setText("Total Books: " + classeModel.getBooks().size());
            Picasso.get().load(classImgs[getRealPosition(position)]).into(viewHolderClass.imageView_bookCover);

            viewHolderClass.linearLayout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAllAdsUtil.showInterIfOk(context, new MyAdsListener());

                    Intent intent = new Intent(context, BookActivity.class);
                    intent.putExtra("ClassName", classeModel.getClassName());
                    intent.putExtra("position", getRealPosition(position));
                    intent.putParcelableArrayListExtra("BookList", classeModel.getBooks());
                    context.startActivity(intent);
                }
            });
        } else {
            AdViewHolder viewHolder = (AdViewHolder) holder;
            MyAllAdsUtil.setBannerAdsTried(1);
            MyAllAdsUtil.addAnyBannerAd(false, context, true, viewHolder.linearLayoutAds, new MyAdsListener());
        }
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        ImageView imageView_bookCover;
        TextView textView_title, textView_bookCount;
        RelativeLayout linearLayout_item;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            imageView_bookCover = itemView.findViewById(R.id.imageView_bookCover);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_bookCount = itemView.findViewById(R.id.textView_bookCount);
            linearLayout_item = itemView.findViewById(R.id.linearLayout_item);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutAds;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayoutAds = itemView.findViewById(R.id.adsLL);
        }
    }

    public void replaceArrayList(ArrayList<ClassType> classeModels) {
        this.classeModelArrayList = classeModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 0 && position % LIST_AD_DELTA == 0) {
            return AD;
        }
        return CONTENT;
    }

    @Override
    public int getItemCount() {
        int additionalContent = 0;
        if (classeModelArrayList.size() > 0 && LIST_AD_DELTA > 0 && classeModelArrayList.size() > LIST_AD_DELTA) {
            additionalContent = classeModelArrayList.size() / LIST_AD_DELTA;
        }
        return classeModelArrayList.size() + additionalContent;
    }

    private int getRealPosition(int position) {
        if (LIST_AD_DELTA == 0) {
            return position;
        } else {
            return position - position / LIST_AD_DELTA;
        }
    }
}

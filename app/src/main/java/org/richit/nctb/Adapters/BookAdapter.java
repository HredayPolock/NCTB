package org.richit.nctb.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.squareup.picasso.Picasso;

import org.richit.nctb.Activities.BookWebViewActivity;
import org.richit.nctb.AdUtils.MyAdsListener;
import org.richit.nctb.AdUtils.MyAllAdsUtil;
import org.richit.nctb.Config;
import org.richit.nctb.Models.Book;
import org.richit.nctb.R;

import java.io.File;
import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LIST_AD_DELTA = 8;
    private static final int CONTENT = 0;
    private static final int AD = 1;

    Context context;
    ArrayList<Book> bookArrayList = new ArrayList<>();

    String MIME_TYPE_PDF = "application/pdf";
    String TAG = "Download";
    ProgressDialog progressDialog;

    public BookAdapter(ArrayList<Book> books, Context context) {
        this.bookArrayList = books;
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Downloading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT) {
            return new ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.book_layout2, parent, false));
        } else {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.banner_ad_container, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CONTENT) {
            final ViewHolderClass viewHolderClass = (ViewHolderClass) holder;

            final Book book = bookArrayList.get(getRealPosition(position));
            viewHolderClass.textView_bookName.setText(book.getBookName());
            final String fileName = book.getPdf();

            try {
                Picasso.get().load(book.getBookCover()).placeholder(R.drawable.ic_book_open_page_variant_grey600_48dp).into(viewHolderClass.imageView_bookCover);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isFileExists(fileName)) {
                Picasso.get().load(R.drawable.ic_book_open_page_variant_grey600_24dp).into(viewHolderClass.download_icon);
            } else {
                Picasso.get().load(R.drawable.ic_download_grey600_24dp).into(viewHolderClass.download_icon);
            }

            viewHolderClass.linearLayout_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlView = book.getBanglaView();
                    String urlDownload = Config.bookDriveLink;
                    String[] str = urlView.split("/");
                    final String urlDownloadFinal = urlDownload.replace("FILE-ID", str[5]);
                    //final String fileName = str[5] + ".pdf";

                    final String choices[];

                    if (isFileExists(fileName)) {
                        choices = new String[]{
                                "Read online", "Read Offline"
                        };
                    } else {
                        choices = new String[]{
                                "Read online", "Download"
                        };
                    }

                    String bookNameArr[] = book.getPdf().split("-");


                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("" + bookNameArr[bookNameArr.length - 1].replace(".pdf", "").replace("OPT", ""))
                            .setItems(choices, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (choices[i].equals("Download")) {
                                        MyAllAdsUtil.showAnyInter(true, context, new MyAdsListener());
                                        downloadFile(urlDownloadFinal, fileName, viewHolderClass);
                                    } else if (choices[i].equals("Read Offline")) {
                                        readFile(fileName);
                                    } else {
                                        MyAllAdsUtil.showInterIfOk(context, new MyAdsListener());
                                        Intent intent = new Intent(context, BookWebViewActivity.class);
                                        intent.putExtra("bookFromAdapter", book);
                                        context.startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    ;

                    MyAllAdsUtil.addBannerAdsInDialog(context, builder);
                    builder.show();
                }
            });
        } else {
            AdViewHolder viewHolder = (AdViewHolder) holder;
            MyAllAdsUtil.setBannerAdsTried(1);
            MyAllAdsUtil.addAnyBannerAd(false, context, true, viewHolder.linearLayoutAds, new MyAdsListener());
        }
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
        if (bookArrayList.size() > 0 && LIST_AD_DELTA > 0 && bookArrayList.size() > LIST_AD_DELTA) {
            additionalContent = bookArrayList.size() / LIST_AD_DELTA;
        }
        return bookArrayList.size() + additionalContent;
    }

    private int getRealPosition(int position) {
        if (LIST_AD_DELTA == 0) {
            return position;
        } else {
            return position - position / LIST_AD_DELTA;
        }
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        ImageView imageView_bookCover, download_icon;
        TextView textView_bookName;
        RelativeLayout linearLayout_book;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            linearLayout_book = itemView.findViewById(R.id.linearLayout_book);
            imageView_bookCover = itemView.findViewById(R.id.imageView_bookCover);
            textView_bookName = itemView.findViewById(R.id.textView_bookName);
            download_icon = itemView.findViewById(R.id.downloadIcon);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutAds;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayoutAds = itemView.findViewById(R.id.adsLL);
        }
    }

    public void replaceArrayList(ArrayList<Book> books) {
        this.bookArrayList = books;
        notifyDataSetChanged();
    }

    //download
    public String getRootDirPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    private boolean isFileExists(String fileName) {
        File myfile = new File(getRootDirPath() + "/" + fileName);
        return myfile.exists();
    }

    boolean canDisplayPdf(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType(MIME_TYPE_PDF);
        if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void downloadFile(String urlDownload, final String fileName, final ViewHolderClass holder) {
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        // GorsAds.showInterAd(context);
        AndroidNetworking.download(urlDownload, getRootDirPath(), fileName)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        Log.d(TAG, "onProgress: " + bytesDownloaded);
                        progressDialog.setMessage((bytesDownloaded / 1048576) + "MB Downloaded ");
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d(TAG, "onDownloadComplete: ");
                        progressDialog.dismiss();

                        if (isFileExists(fileName)) {
                            Picasso.get().load(R.drawable.ic_book_open_page_variant_grey600_24dp).into(holder.download_icon);
                        }
                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                        showReadNowDialog(fileName);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
                    }
                });
//
    }

    private void showReadNowDialog(final String fileName) {
        new AlertDialog.Builder(context)
                .setTitle("Open Downloaded Book")
                .setMessage("Would you like to read the book now?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        readFile(fileName);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void readFile(String fileName) {
        if (canDisplayPdf(context)) {
            File myfile = new File(getRootDirPath() + "/" + fileName);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(myfile), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                // Instruct the user to install a PDF reader here, or something
                Toast.makeText(context, "Install a pdf reader", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Install a pdf reader", Toast.LENGTH_SHORT).show();
            //Toasty.error(context, "Install a pdf reader!!.", Toast.LENGTH_SHORT, true).show();
        }
    }

}

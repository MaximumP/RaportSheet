package de.schumann.max.raportsheet.util;

import android.content.Context;
import android.database.Cursor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.schumann.max.raportsheet.R;

/**
 * Created by max on 09.06.16.
 */
public class Printer {

    private WebView webView;
    private Context context;

    public Printer(Context context) {
        this.context = context;
    }

    public void printHtml(Cursor cursor){
        webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                webView = null;
            }
        });
        String html = createHtml(cursor);
        Log.i("Print content", html);
        webView.loadData(html, "text/HTML; charset=utf-8", "utf-8");
    }

    private void createWebPrintJob(WebView view) {
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter documentAdapter = view.createPrintDocumentAdapter();
        printManager.print("test", documentAdapter, new PrintAttributes.Builder().build());
    }

    private String createHtml(Cursor cursor) {
        StringBuilder htmlBuilder = new StringBuilder();
        Double sumHours = 0.0d;
        htmlBuilder.append("<!DOCTYPE HTML><html><head><style type='text/css'>" +
                ".date,.customer {padding-right: 4em;}" +
                "</style><meta charset='UTF-8'></head><body>");

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            htmlBuilder.append("<div><div>");
            // date
            htmlBuilder.append("<span class='date'><b>");
            htmlBuilder.append(context.getString(R.string.raport_date));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(getDateFromTicksString(cursor.getString(1)));
            htmlBuilder.append("</span>");
            // customer
            htmlBuilder.append("<span class='customer'><b>");
            htmlBuilder.append(context.getString(R.string.customer_name));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(cursor.getString(2));
            htmlBuilder.append("</span>");
            // city
            htmlBuilder.append("<span><b>");
            htmlBuilder.append(context.getString(R.string.raport_construction_site));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(cursor.getString(3));
            htmlBuilder.append("</span>");
            htmlBuilder.append("</div><br />");
            // work description
            htmlBuilder.append("<div><span><b>");
            htmlBuilder.append(context.getString(R.string.raport_work_description));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(cursor.getString(4));
            htmlBuilder.append("</span>");
            // work time
            htmlBuilder.append("<p style='text-align:right'><b>");
            htmlBuilder.append(context.getString(R.string.raport_work_hours));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(cursor.getDouble(5));
            htmlBuilder.append("</p><hr /><br /></div></div>");
            sumHours += cursor.getDouble(5);
            cursor.moveToNext();
        }

        htmlBuilder.append("<span><p style='text-align:right'><b>");
        htmlBuilder.append(context.getString(R.string.raport_total_hours));
        htmlBuilder.append(": </b>");
        htmlBuilder.append(sumHours);
        htmlBuilder.append("</p></span>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private String getDateFromTicksString(String sTicks) {
        long ticks;
        Date date;

        ticks = Long.parseLong(sTicks);
        date = new Date(ticks);

        return new SimpleDateFormat("dd.MM.yyyy", context.getResources().getConfiguration().locale).format(date);
    }
}

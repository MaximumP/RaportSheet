package de.schumann.max.raportsheet.util;

import android.content.Context;
import android.database.Cursor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.schumann.max.raportsheet.R;

/**
 * Created by max on 09.06.16.
 *
 * Prints a raport set from a given Cursor.
 *
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
        Date curDate;
        Date prevDate = null;
        while(!cursor.isAfterLast()) {

            curDate = new Date(cursor.getLong(1));
            if (prevDate != null) {
                if (isSameDay(curDate, prevDate)) {
                    sumHours += cursor.getDouble(5);
                } else {
                    htmlBuilder.append("<span><p style='text-align:right'><b>");
                    htmlBuilder.append(context.getString(R.string.raport_total_hours));
                    htmlBuilder.append(" ");
                    htmlBuilder.append(getDateFromTicksString(cursor.getLong(1)));
                    htmlBuilder.append(": </b>");
                    htmlBuilder.append(sumHours);
                    htmlBuilder.append("</p></span>");
                    sumHours = 0.0d;
                }
            } else {
                sumHours += cursor.getDouble(5);
            }

            htmlBuilder.append("<div><div>");
            // date
            htmlBuilder.append("<span class='date'><b>");
            htmlBuilder.append(context.getString(R.string.raport_date));
            htmlBuilder.append(": </b>");
            htmlBuilder.append(getDateFromTicksString(cursor.getLong(1)));
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

            prevDate = curDate;
            cursor.moveToNext();
        }

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance(context.getResources().getConfiguration().locale);
        Calendar c2 = Calendar.getInstance(context.getResources().getConfiguration().locale);
        c1.setTime(date1);
        c2.setTime(date2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private String getDateFromTicksString(long ticks) {
        Date date;

        date = new Date(ticks);

        return new SimpleDateFormat("dd.MM.yyyy", context.getResources().getConfiguration().locale).format(date);
    }
}

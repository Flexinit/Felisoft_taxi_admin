package com.example.drupp_driver.Utils;

import android.content.Context;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PDFReader {

    public void loadPdf(Context context, PDFView pdfView, String fileName){
        pdfView.fromAsset(fileName)
                .password(null)
                .defaultPage(0)
                .enableDoubletap(true)
                .enableSwipe(true)
                .onPageError((page, t) -> {
                    Toast.makeText(context,"Error loading page",Toast.LENGTH_SHORT).show();
                })
                .scrollHandle(new DefaultScrollHandle(context))
                .onError(t -> {
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                })
                .load();
    }
}

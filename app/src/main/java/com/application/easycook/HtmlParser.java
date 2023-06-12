package com.application.easycook;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class HtmlParser {
    private static final String TAG = "HtmlParser";
    public HtmlParsingTask parsingTask;

    public void parseHtmlFile(Context context, String fileName) {
        parsingTask=new HtmlParsingTask(context);
        parsingTask.doInBackground();

    }

    public HtmlParser(Context context) {
        parseHtmlFile(context,"veg-fresh.html");

    }
    public void showData(){
        System.out.println(parsingTask.imag);
        System.out.println(parsingTask.titels);
        System.out.println(parsingTask.titels.size());

    }

    private static class HtmlParsingTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        public ArrayList<String> titels=new ArrayList<>();
        public ArrayList<String> imag=new ArrayList<>();
        private FirebaseStorage storage;


        public HtmlParsingTask(Context context) {
            this.context = context;
            storage = FirebaseStorage.getInstance();
        }


        public void downloadHTMLFile() {
            // Get a reference to the HTML file in Firebase Storage
            StorageReference storageRef = storage.getReference().child("html_shufersal/veg-fresh.html");

            // Download the HTML file as a stream
            StreamDownloadTask task = storageRef.getStream();
            task.addOnSuccessListener(taskSnapshot -> {
                try {
                    InputStream stream = taskSnapshot.getStream();
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = stream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    String htmlContent = result.toString(StandardCharsets.UTF_8.name());
                    Document document = Jsoup.parse(htmlContent);
                    Elements data = document.select("main#main");
                    for (Element li : data.select("section.tileSection3 li")) {
                        String imgsrc = li.select("img").attr("src");
                        String title = li.attr("data-product-name");
                        if (imgsrc == "" || imgsrc==null || title==null)
                            continue;
//                        titels.add(title);
//                        imag.add(imgsrc);
                        System.out.println(imgsrc);
                        System.out.println(title);
                    }
                    // Handle the Document as needed
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).addOnFailureListener(exception -> {
                // Handle any errors that occur during the download
                // For example, you can display an error message to the user
            });

        }











        @Override
        protected Void doInBackground(Void... voids) {
            downloadHTMLFile();
//            try {
//
//                AssetManager assetManager = context.getAssets();
//                InputStream inputStream = assetManager.open("veg-fresh.html");
//                Document doc = Jsoup.parse(inputStream, "UTF-8", "");
////                Document document = Jsoup.parse(doc.outerHtml());
//                Elements data = doc.select("main#main");
//                for (Element li : data.select("section.tileSection3 li")) {
//                    String imgsrc = li.select("img").attr("src");
//                    String title = li.attr("data-product-name");
//                    if (imgsrc == "" || imgsrc==null || title==null)
//                        continue;
//                    titels.add(title);
//                    imag.add(imgsrc);
//                    System.out.println(imgsrc);
//                    System.out.println(title);
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }

    }
}

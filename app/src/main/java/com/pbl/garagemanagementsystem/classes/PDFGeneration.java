package com.pbl.garagemanagementsystem.classes;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PDFGeneration implements OnCompleteListener<QuerySnapshot>, Runnable {
    private final String name;
    private final String email;
    private final String contactNo;
    private final String carRegNo;
    private final String date;
    private final ArrayList<String> spares;
    private final ArrayList<String> complaint;
    int num, totalEstimate;
    int[] cost;
    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PDFGeneration(String name, String email, String contactNo, String carRegNo, String date, ArrayList<String> spares, ArrayList<String> complaint) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.carRegNo = carRegNo;
        this.spares = spares;
        this.complaint = complaint;
        this.date = date;
        num = spares.size();
        cost = new int[num];
        //Fetch Estimate of all Required spares.
        db = FirebaseFirestore.getInstance();
        db.collection("Inventory")
                .get()
                .addOnCompleteListener(this);
        new Handler().postDelayed(this
                , 2000);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GeneratePDF() throws IOException, DocumentException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), carRegNo+"_"+date+".pdf");
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph p0 = new Paragraph("Customer Details:");
        Paragraph p1 = new Paragraph(name);
        Paragraph p2 = new Paragraph(carRegNo);
        Paragraph p3 = new Paragraph(email);
        Paragraph p4 = new Paragraph(contactNo+"\n\n\n");
        Paragraph p6 = new Paragraph("Complaints:-");
        Paragraph p5 = new Paragraph();
        for (String str1: complaint){
            Chunk chunk = new Chunk(str1+", ");
            p5.add(chunk);
        }

        document.add(p1);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        document.add(p6);
        document.add(p5);

        PdfPTable table = new PdfPTable(3);

        table.setSpacingBefore(20f);
        table.addCell("Sr. No");
        table.addCell("Spare");
        table.addCell("Estimate");
        int i = 0;

        for (String str1: spares) {
            table.addCell("" + (i + 1));
            table.addCell(str1);
            table.addCell(""+cost[i]);
            i++;
        }
        table.addCell("Total:");
        table.addCell(""+spares.size());
        table.addCell(""+totalEstimate);

        document.add(table);
        document.close();
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            int cnt = 0;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Spares spare = document.toObject(Spares.class);
                if(cnt == num){
                    break;
                }
                else if (spares.contains(spare.spare)){
                    int position = spares.indexOf(spare.spare);
                    cost[position] = Integer.parseInt(spare.Estimate);
                    cnt++;
                }
            }
        } else {
            Log.e("PDFGen", "DB Error");
        }
    }

    @Override
    public void run() {
        for (int i : cost){
            totalEstimate += i;
        }
    }
}

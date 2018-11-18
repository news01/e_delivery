package com.thinkgem.jeesite.util;

import com.KGitextpdf.text.*;
import com.KGitextpdf.text.pdf.*;
//import com.KGitextpdf.text.pdf.parser.PdfReaderContentParser;
//import com.KGitextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
//import com.KGitextpdf.text.pdf.parser.TextExtractionStrategy;
//import com.kinggrid.pdf.KGPdfHummerUtils;
//import com.kinggrid.pdfview.KGPdf2Image;
import com.KGitextpdf.text.pdf.parser.PdfReaderContentParser;
import com.KGitextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.KGitextpdf.text.pdf.parser.TextExtractionStrategy;
import com.kinggrid.pdf.KGPdfHummerUtils;
import com.kinggrid.pdfview.KGPdf2Image;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viper on 2017/7/10.
 */
public class PdfUtils {
    public static void main(String[] args) throws Exception {

//        FileInputStream fileInputStream = new FileInputStream("d:/tmp/11.pdf");
//
//        byte[] temp = formatPDFA4(fileInputStream);
//
//        FileOutputStream fileOutputStream = new FileOutputStream("d:/1111.pdf");
//
//        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//
//        bufferedOutputStream.write(temp);
//
//
//        fileOutputStream.flush();
//        fileInputStream.close();
//        fileOutputStream.close();

        formatPDFA4("d:/tmp/11.pdf");


    }

    public static byte[] formatPdf(InputStream in) throws Exception{

        ByteArrayOutputStream outputStream = null;

        try {

            outputStream = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(in);
            int page = reader.getNumberOfPages();
            Rectangle rectangle;
            boolean needRotate = false;
            for(int i=1;i<=page;i++){
                rectangle = reader.getPageSize(i);
                float w = rectangle.getWidth();
                float h = rectangle.getHeight();
                if(w>h){
                    needRotate = true;
                    break;
                }
            }
            if(needRotate){
                Document document = new Document();
                PdfCopy p = new PdfSmartCopy(document,outputStream);
                document.open();
                PdfDictionary pd;
                for(int i=1;i<=page;i++){
                    rectangle = reader.getPageSize(i);
                    float w = rectangle.getWidth();
                    float h = rectangle.getHeight();
                    pd = reader.getPageN(i);
                    if(w>h){
                        pd.put(PdfName.ROTATE, new PdfNumber(270));
                    }
                }
                for (int i = 0; i < page; ) {
                    p.addPage(p.getImportedPage(reader, ++i));
                }

                document.close();
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            if (outputStream!=null){
                outputStream.close();
            }
        }
        return null;
    }

    private static final String blankResourcePath="/templates/blank.pdf";

    private static  byte[]blankData = null;

    public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream,boolean needAddBlank){
        try {
            if(needAddBlank){
                if(blankData==null||blankData.length==0){
                    Resource blankResource = new ClassPathResource(blankResourcePath);
                    blankData = new byte[(int) blankResource.getFile().length()];
                    blankResource.getInputStream().read(blankData);
                }
                for(int i=0;i<streamOfPDFFiles.size();i++){
                    InputStream in = streamOfPDFFiles.get(i);
                    if(getPdfPage(in)%2==1){
                        streamOfPDFFiles.add(i+1,new ByteArrayInputStream(blankData));
                        i++;
                    }
                }

            }
            KGPdfHummerUtils.mergeMultiPdf(streamOfPDFFiles, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (InputStream inputStream : streamOfPDFFiles) {
                close(inputStream);
            }
            close(outputStream);
        }
    }
    public static void concatPDFsA3(List<InputStream> streamOfPDFFiles, OutputStream outputStream,boolean needAddBlank){
        try {
            if(needAddBlank){
                if(blankData==null||blankData.length==0){
                    Resource blankResource = new ClassPathResource(blankResourcePath);
                    blankData = new byte[(int) blankResource.getFile().length()];
                    blankResource.getInputStream().read(blankData);
                }

                for(int i=0;i<streamOfPDFFiles.size();i++){


                    InputStream in = streamOfPDFFiles.get(i);
                    int k = getPdfPage(in)% 4;
                    byte[] temp = formatPdf(in);

                    if (temp != null){

                        in  = new ByteArrayInputStream(temp);
                        streamOfPDFFiles.clear();
                        streamOfPDFFiles.add(in);
                    }else{
                        in.reset();
                    }


                    if (k != 0) {

                        k = 4-k;

                        for (int j = 0;j < k;j++){

                            streamOfPDFFiles.add(i+1,new ByteArrayInputStream(blankData));
                            i++;
                        }

                    }

                }




            }

//            FileOutputStream fileOutputStream = new FileOutputStream("d:/333.pdf");
//
//            KGPdfHummerUtils.mergeMultiPdf(streamOfPDFFiles, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
            KGPdfHummerUtils.mergeMultiPdf(streamOfPDFFiles, outputStream);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            for (InputStream inputStream : streamOfPDFFiles) {
                close(inputStream);
            }
            close(outputStream);
        }
    }

    private static void close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void concatPDFsOld(List<InputStream> streamOfPDFFiles,
                                  OutputStream outputStream) {

        Document document = new Document();
        try {
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;

            // Create Readers for the pdfs.
            for (InputStream pdf : streamOfPDFFiles) {
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;

            // Loop through the PDF files and add to the output.
            for (PdfReader pdfReader : readers) {
                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }

            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            for (InputStream inputStream : streamOfPDFFiles) {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static byte[] convertImgToPdf(byte[] imgSrc) {

        Document document = new Document();
        ByteArrayOutputStream fos = null;
        try {
            fos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, fos);
            document.setPageSize(PageSize.A4);
            document.open();
            Image image = Image.getInstance(imgSrc);
            float imageHeight = image.getScaledHeight();
            float imageWidth = image.getScaledWidth();
            int i = 0;
            while (imageHeight > 500 || imageWidth > 500) {
                image.scalePercent(100 - i);
                i++;
                imageHeight = image.getScaledHeight();
                imageWidth = image.getScaledWidth();
                System.out.println("imageHeight->" + imageHeight);
                System.out.println("imageWidth->" + imageWidth);
            }
            image.setAlignment(Image.ALIGN_CENTER);
            document.add(image);
        } catch (DocumentException | IOException de) {
            System.out.println(de.getMessage());
        } finally {
            document.close();
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fos.toByteArray();
    }

    public static List<byte[]> convertPdfToImg(String pdfName)throws Exception{

        List<byte[]> imageBytes = null;
        KGPdf2Image pdf2Image = null;
        try {
            pdf2Image = new KGPdf2Image(pdfName);

            pdf2Image.scalePercent(0.38f);

            imageBytes = new ArrayList<>();

            for(int i=1;i<=pdf2Image.getNumberOfPages();i++){
                ByteArrayOutputStream byteArrayOutputStream = null;

                try {

                    byteArrayOutputStream = new ByteArrayOutputStream();
                    pdf2Image.save2Image(i, byteArrayOutputStream, "PNG");

                    imageBytes.add(byteArrayOutputStream.toByteArray());

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                } finally {
                    if (byteArrayOutputStream !=null){
                        byteArrayOutputStream.close();
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (pdf2Image != null){
                pdf2Image.close();
            }

        }

        return imageBytes;

    }

    public static byte[] formatPDFA4(String pdfname)throws Exception{

        List<InputStream> inputStreams = new ArrayList<>();

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            List<byte[]> imageBytes = convertPdfToImg(pdfname);

            for (byte[] temp : imageBytes){

                byte[] pdf = convertImgToPdf(temp);

                inputStreams.add(new ByteArrayInputStream(pdf));

            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            KGPdfHummerUtils.mergeMultiPdf(inputStreams, byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (byteArrayOutputStream != null){
                byteArrayOutputStream.close();
            }
            for (InputStream in :inputStreams){
                if (in != null){
                    in.close();
                }
            }

        }

        return byteArrayOutputStream.toByteArray();

    }


    public static int getPdfPage(byte[] pdfData) throws Exception{
        PdfReader reader = null;
        try{
            reader=new PdfReader(pdfData);
            return reader.getNumberOfPages();
        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
    }



    public static int getPdfPage(InputStream in) throws Exception{
        PdfReader reader = null;
        try{
            reader=new PdfReader(in);
            return reader.getNumberOfPages();
        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }finally {
            if(reader!=null){
                reader.close();
            }
            in.reset();
        }
    }


    public static String getTextFromPdf(InputStream in) throws Exception{
        PdfReader reader = null;
        try{
            reader=new PdfReader(in);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            StringBuilder sb = new StringBuilder();
            int num = reader.getNumberOfPages();// 获得页数
            TextExtractionStrategy strategy;
            for (int i = 1; i <= num; i++) {
                strategy = parser.processContent(i,new SimpleTextExtractionStrategy());
                sb.append(strategy.getResultantText());
            }
            return sb.toString();
        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }finally {
            if(reader!=null){
                reader.close();
            }

        }
    }

    public static String[] getParagraphsWithoutSpace(byte[] data) throws Exception{
        String text = getTextFromPdf(new ByteArrayInputStream(data));
        return getParagraphsWithoutSpace(text);
    }

    public static String[] getParagraphsWithoutSpace(InputStream data) throws Exception{
        String text = getTextFromPdf(data);
        return getParagraphsWithoutSpace(text);
    }












    public static String[] getParagraphsWithoutSpace(String text){
        if(text==null){
            return new String[0];
        }
        text = text.replace(" ","");
        String[]paragraphs = text.split("\n");
        return paragraphs;
    }




    public static String[] getParagraphs(byte[] data) throws Exception{
        String text = getTextFromPdf(new ByteArrayInputStream(data));
        return getParagraphs(text);
    }

    public static String[] getParagraphs(InputStream data) throws Exception{
        String text = getTextFromPdf(data);
        return getParagraphs(text);
    }


    public static String[] getParagraphs(String text){
        if(text==null){
            return new String[0];
        }
        String[]paragraphs = text.split("\n");
        return paragraphs;
    }







    public static String getDate(String text){
        return null;
    }






}

// A class that contains built-in functions that convert files from one type to another
import java.io.*;
import java.time.LocalDateTime; // get the current time and date. We use it to record the conversion time in the conversion history
import java.time.format.DateTimeFormatter; // control how the date and time are displayed

//From an external library called PDFBox
import org.apache.pdfbox.pdmodel.PDDocument; // open PDF file through which we read or edit its content
import org.apache.pdfbox.text.PDFTextStripper; // tool that extracts text from PDF pages

//From an external library called iText -> to create a new PDF files
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

// Apache POI library -> allow to create MicrosoftWord files
import org.apache.poi.xwpf.usermodel.XWPFDocument; // new Word document
import org.apache.poi.xwpf.usermodel.XWPFParagraph; // paragragh within the document


public class FileConverter {

    //    convertPdfToTxt

    public static boolean convertPdfToTxt (File pdfFile) {
        try {
            // upload PDF file
            PDDocument document = PDDocument.load(pdfFile);

            // extract text from PDF
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // path of the new file (same name as the original file but with .txt extension)
                                    // folder
            String txtPath = pdfFile.getParent() + File.separator + pdfFile.getName().replaceAll("\\.pdf$", "") + "_converted.txt";

            // create the text file and write the content into it
            BufferedWriter writer = new BufferedWriter(new FileWriter(txtPath));
            writer.write(text);
            writer.close();

            // close the original file
            document.close(); // -> closes the original PDF file (important to avoid memory issues)

            // We register the new file in the conversion log
            logConversion(pdfFile, new File(txtPath), "PDF to TXT");
            return true;

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }

    //    convertTxtToPdf

    public static boolean convertTxtToPdf (File txtFile) {
        if (txtFile == null || !txtFile.getName().toLowerCase().endsWith(".txt")) {
            return false;
        }

        try {
            Document document = new Document();
            String outputPath = txtFile.getAbsolutePath().replace(".txt", "_converted.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(outputPath)); // PdfWriter: tool that writes content into a PDF document
            document.open();

            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            String line;
            while ((line = reader.readLine()) != null) {
                document.add(new Paragraph(line));
            }

            document.close();
            reader.close();

            // We register the new file in the conversion log
            logConversion(txtFile, new File(outputPath), "TXT to PDF");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //    convertJpgToPdf

    public static boolean convertJpgToPdf (File imageFile) {
        if (imageFile == null || !(imageFile.getName().toLowerCase().endsWith(".jpg") || imageFile.getName().toLowerCase().endsWith(".jpeg") || imageFile.getName().toLowerCase().endsWith(".png"))) {
            return false;
        }

        try {
            Document document = new Document();
            String outputPath = imageFile.getAbsolutePath().replaceFirst("\\.(jpg|jpeg|png)$", "_converted.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            Image img = Image.getInstance(imageFile.getAbsolutePath());
            img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            img.setAlignment(Image.ALIGN_CENTER);
            document.add(img);

            document.close();

            // We register the new file in the conversion log
            logConversion(imageFile, new File(outputPath), "JPG to PDF");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //    convertTxtToDocx

    public static boolean convertTxtToDocx (File txtFile) {
        if (txtFile == null || !txtFile.getName().toLowerCase().endsWith(".txt")) {
            return false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            XWPFDocument document = new XWPFDocument();

            String line;
            while ((line = reader.readLine()) != null) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.createRun().setText(line);
            }

            String outputPath = txtFile.getAbsolutePath().replace(".txt", "_converted.docx");
            FileOutputStream out = new FileOutputStream(outputPath);
            document.write(out);
            out.close();
            reader.close();

            // We register the new file in the conversion log
            logConversion(txtFile, new File(outputPath), "TXT to DOCX");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // History - The new file name is recorded in the transfer log file
    // Each conversion operation is recorded in a text file containing the operation information
    private static void logConversion (File original, File converted, String type) {
        try (FileWriter fw = new FileWriter("converted_history.txt", true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String logEntry = "✅ " + type + ": " + original.getName() + " → " + converted.getName() + "(" + timestamp + ")";
            fw.write(logEntry + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

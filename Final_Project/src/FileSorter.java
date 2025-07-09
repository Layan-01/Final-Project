import java.io.File;
import java.util.ArrayList; //To store multiple files within a category
import java.util.List;
import java.util.HashMap; //To store the categories themselves, each category has a list of files
import java.util.Map;

public class FileSorter {
    public static Map<String, List<File>> classifyFiles(File[] files) {
        Map<String, List<File>> classified = new HashMap<>();

        //== Map Preparation : created an empty ArrayList for each category so we could add files to it later
        classified.put("Documents", new ArrayList<>());
        classified.put("Images", new ArrayList<>());
        classified.put("Videos", new ArrayList<>());
        classified.put("Audio", new ArrayList<>());
        classified.put("Others", new ArrayList<>());
        // If there are no files (or the folder is empty or there is an error), we return the map empty
        // This is a security condition (null check)
        if (files == null)
            return classified;
        //loop that goes through each file in the folder
        for (File file : files) {
            //Make sure the item is an actual file (not a folder or shortcut)
            if (file.isFile()) {
                // We take the file name and make lowercase.
                //==== WHY?===
                //So that .PDF and .pdf work the same.
                String name = file.getName().toLowerCase();
                //If the file extension is pdf, docx or txt, add it to the "Documents" list
                if (name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".txt")) {
                    classified.get("Documents").add(file);
                } else if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
                    classified.get("Images").add(file);
                } else if (name.endsWith(".mp4") || name.endsWith(".avi")) {
                    classified.get("Videos").add(file);
                } else if (name.endsWith(".mp3") || name.endsWith(".wav")) {
                    classified.get("Audio").add(file);
                } else {
                    classified.get("Others").add(file); //If it does not apply to any type, we put it under “Others”
                }
            }
        }
        return classified;

    }
}




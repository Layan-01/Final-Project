import java.io.File;
import java.util.ArrayList; // specific type of list to store a collection of files
import java.util.List; // general list

public class FileSearcher {
    public static List<File> searchFilesByName (File[] files, String keyword){ // List<File> : type of the result that the function will return
        List<File> result = new ArrayList<>();

        if (files == null || keyword == null || keyword.isEmpty()) // protect program from errors
            return result;
        for (File file : files){ // for each loop (for each file in the folder)
            if (file.isFile() && file.getName().toLowerCase().contains(keyword.toLowerCase())){
                result.add(file);
            }
        }
        return result;
    }

}

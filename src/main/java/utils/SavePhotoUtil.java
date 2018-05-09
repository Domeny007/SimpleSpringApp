package utils;

import database.entity.Author;
import database.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import exceptions.AddPhotoException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class SavePhotoUtil {

    private static String MAIN_PART_PATH = "/WEB-INF/resources";

    public String savePhoto(HttpServletRequest req,MultipartFile photoFile,Class clazz) throws AddPhotoException {
        String photo = "";
        String defFolder = "";
        try {
            if(Author.class.equals(clazz)) {
                defFolder = Const.AUTHOR_IMAGES;
            } else if(Book.class.equals(clazz)) {
                defFolder = Const.BOOK_IMAGES;
            }

            System.out.println("reqPath = " + req.getServletContext().getRealPath(MAIN_PART_PATH));

            String path = req.getServletContext().getRealPath(MAIN_PART_PATH) + defFolder;

            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileType = photoFile.getContentType().replace("image/",".");
            File file = File.createTempFile(photoFile.getName() + "-", fileType,dir);

            InputStream input = photoFile.getInputStream();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            photo = defFolder + "/" +  file.getName();
        }
        catch (Exception e) {
            System.out.println("mess = " + e.getMessage());
            throw new AddPhotoException(clazz.getTypeName().toLowerCase());
        }
        return photo;
    }
}

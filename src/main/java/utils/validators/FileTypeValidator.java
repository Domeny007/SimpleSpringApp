package utils.validators;

import org.springframework.web.multipart.MultipartFile;
import utils.Const;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements
        ConstraintValidator<FileTypeConstraint, MultipartFile> {

    @Override
    public void initialize(FileTypeConstraint fileTypeConstraint) {
    }

    @Override
    public boolean isValid(MultipartFile file,
                           ConstraintValidatorContext cxt) {


        System.out.println("file type = " + file.getOriginalFilename());

        boolean rightType = file.getOriginalFilename().matches(".*\\.(jpe?g|png)");

        Const.sys("type = " + rightType);

        return rightType;
    }

}

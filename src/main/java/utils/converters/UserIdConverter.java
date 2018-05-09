package utils.converters;

import database.entity.User;
import database.hibernate_repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIdConverter extends AbstractTwoWayConverter<String, User> {

    private final UserRepository userRepository;

    @Autowired
    public UserIdConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected User convert(String userId) {
        return userRepository.findOne(Integer.valueOf(userId));
    }

    @Override
    protected String convertBack(User target) {
        return String.valueOf(target.getId());
    }
}

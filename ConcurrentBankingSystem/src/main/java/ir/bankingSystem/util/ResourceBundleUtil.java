package ir.bankingSystem.util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AllArgsConstructor
@Component
public class ResourceBundleUtil {

    private static MessageSource messageSourceStatic;

    private MessageSource messageSource;

    @PostConstruct
    public void init() {
        messageSourceStatic = messageSource;
    }

    public static String getMessage(String key, Object... args) {
        try {
            return messageSourceStatic.getMessage(key, args,new Locale("fa"));
        } catch (Exception e) {
            return key;
        }
    }

}



package wikipedia;

import org.wikipedia.Wiki;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Hello {

    Wiki bot;

    Wiki login(String user, String password) throws IOException, FailedLoginException {
        bot = new Wiki("uk.wikipedia.org");
        bot.login(user, password);
        return bot;
    }

    void fixDot(String page) throws IOException, LoginException {
        String article = "Животівка";

        String before = "." + page;
        String after = "с. " + page;

        String text = bot.getPageText(article);
        String modified = text.replace(before, after);

        String summary = before + " -> " + after;
        bot.edit(article, modified, summary);
    }

    public static void main(String[] args) throws IOException, LoginException {

        Hello hello = new Hello();
        String user = args[0];
        String password = args[1];
        Wiki bot = hello.login(user, password);


        String[] members = bot.getCategoryMembers("Села Вінницької області", false, 0);

        System.out.println(members.length);

        for (String page : members) {
            System.out.println(page);

            hello.fixDot(page);
        }

    }


}

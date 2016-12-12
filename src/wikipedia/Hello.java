package wikipedia;

import org.wikipedia.Wiki;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hello {

    Wiki bot;

    public static void main(String[] args) throws IOException, LoginException {

        Hello hello = new Hello();
        String user = args[0];
        String password = args[1];
        Wiki bot = hello.login(user, password);

        String[] members = bot.getCategoryMembers("Села Вінницької області", false, 0);

        System.out.println(members.length);

        for (String page : members) {
            System.out.println(page);

            hello.replaceRegex(page, "(вул\\.)(\\S)", "вул. $2");
        }
    }

    Wiki login(String user, String password) throws IOException, FailedLoginException {
        bot = new Wiki("uk.wikipedia.org");
        bot.login(user, password);
        bot.setMarkBot(true);
        return bot;
    }

    void replaceRegex(String page, String regex, String replacement) throws IOException, LoginException {

        String text = bot.getPageText(page);

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        if (m.find()) {
            System.out.println("Заміна в статті  " + page );
            String output = m.replaceAll(replacement);

            printDiff(text, p, output);

            bot.edit(page, output, "вул. + пробіл");
        }
    }

    private void printDiff(String text, Pattern p, String output) {
        String[] oldlines = text.split("\n");
        String[] newlines = output.split("\n");

        for (int i = 0; i < oldlines.length; i++) {
            String old = oldlines[i];
            Matcher m2 = p.matcher(old);
            if (m2.find()) {
                String newLine = newlines[i];

                System.out.println(" == Старий рядок == \n" + old);
                System.out.println(" == Новий рядок == \n" + newLine);
            }
        }
    }

}
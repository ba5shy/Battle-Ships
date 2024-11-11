
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {

    private static Locale locale = new Locale("en");

    public static Stage stage;

    @Override
    public void start(Stage stage) {

        setStage(stage);

        MainController main_controller = new MainController();
        main_controller.switchToMainMenu();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    static void setLocale(Locale newLocale){
        locale = newLocale;
    }

    static Locale getLocale(){
        return locale;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package gui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class RecordLoader {
    Stage stage;
    private DataInputStream in;
    private FileChooser fileChooser;
    File file;

    RecordLoader(Stage stage) {
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("OUT","*.out"));
    }

    boolean openFile() {
        file = fileChooser.showOpenDialog(stage);
        return file == null;
    }

    boolean replayPrepare(){
        try {
            if (file == null)
                return false;
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
//            in = new ObjectInputStream(new FileInputStream(file));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    DataInputStream getInputStream() {
        return in;
    }
}

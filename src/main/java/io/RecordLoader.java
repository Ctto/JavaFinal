package io;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class RecordLoader {
    Stage stage;
    private DataInputStream in;
    private FileChooser fileChooser;
    File file;

    public RecordLoader(Stage stage) {
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("OUT","*.out"));
    }

    public boolean openFile() {
        file = fileChooser.showOpenDialog(stage);
        return file == null;
    }

    public boolean replayPrepare(){
        try {
            if (file == null)
                return false;
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public DataInputStream getInputStream() {
        return in;
    }

    public boolean isClosed() {
        return in == null;
    }
}

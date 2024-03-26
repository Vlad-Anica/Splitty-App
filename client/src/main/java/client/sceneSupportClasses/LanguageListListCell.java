package client.sceneSupportClasses;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LanguageListListCell extends ListCell<String> {
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        if(item!=null){
            String[] tokens = item.split(";");
            String text = tokens[0];
            System.out.println("length: " + item);
            if (tokens.length == 2) {
                String path = tokens[1];
                ImageView imageView = new ImageView(new Image(path));
                imageView.setFitWidth(20);
                imageView.setFitHeight(15);
                setGraphic(imageView);
                if (path != null)
                    System.out.println(path);
            }

            setText(text);
        }
    }

}
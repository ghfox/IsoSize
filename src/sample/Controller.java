package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Controller {
    public Slider Slider_X;
    public Slider Slider_Y;
    public Slider Slider_Z;
    public javafx.scene.shape.Box Box;
    public ImageView ImageBox;
    public Slider Y_Off;
    public Slider X_Off;
    public Image img;
    public TextField Field_Width;
    public TextField Field_Depth;
    public TextField Field_Height;
    public Circle OriginSphere;
    public Pane ImgPane;
    public WritableImage copyImg;
    public FileChooser fc;


    public void initialize(){
        Slider_X.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                updateBox();
            }
        });
        Slider_Y.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                updateBox();
            }
        });
        Slider_Z.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                updateBox();
            }
        });
        X_Off.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                updateImagePosition();
            }
        });
        Y_Off.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                updateImagePosition();
            }
        });
        updateBox();
    }

    public void updateBox() {
        Box.setHeight(Slider_Y.getValue());
        Box.setDepth(Slider_Z.getValue());
        Box.setWidth(Slider_X.getValue());
        Field_Depth.setText("" + Slider_Z.getValue());
        Field_Height.setText(""+ Slider_Y.getValue());
        Field_Width.setText("" + Slider_X.getValue());
        OriginSphere.setTranslateY((Slider_Y.getValue() / 2));
        fc = new FileChooser();
        fc.setInitialFileName("NewSpriteSize");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
    }

    public void updateImagePosition()
    {
        ImageBox.setX(200 - (img.getWidth()/2) + X_Off.getValue());
        ImageBox.setY(225 - (img.getHeight()/2) + Y_Off.getValue());
    }

    public void updateImage(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean pass = false;
        if(db.hasFiles())
        {
            img = new Image("file:" + db.getFiles().get(0).toString());
            ImageBox.setImage(img);
            ImageBox.setFitHeight(img.getHeight());
            ImageBox.setFitWidth(img.getWidth());
            Y_Off.setValue(0.0);
            X_Off.setValue(0.0);
            ImageBox.setX(200 - (img.getWidth()/2));
            ImageBox.setY(225 - (img.getHeight()/2));
            pass = true;
        }
        dragEvent.setDropCompleted(pass);
        dragEvent.consume();
    }

    public void setTransfer(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasFiles())
        {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public void manualValue(ActionEvent actionEvent) {
        double z = Double.valueOf(Field_Depth.getText());
        double x = Double.valueOf(Field_Width.getText());
        double y = Double.valueOf(Field_Height.getText());
        Slider_Z.setValue(z);
        Slider_X.setValue(x);
        Slider_Y.setValue(y);
    }

    public void copyImg(ActionEvent actionEvent) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        copyImg = ImgPane.snapshot(params, null);
        ClipboardContent content = new ClipboardContent();
        content.putImage(copyImg);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public void saveImg(ActionEvent actionEvent) {
        copyImg(null);
        File outputFile = fc.showSaveDialog(ImgPane.getScene().getWindow());
        BufferedImage bImage = SwingFXUtils.fromFXImage(copyImg, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

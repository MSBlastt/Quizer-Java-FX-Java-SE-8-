//package com.quizbuilder.events;
//
//import javafx.embed.swing.SwingFXUtils;
//import javafx.event.Event;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.FileChooser;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//
///**
// * Created by Mikhail Kholodkov
// *         on 24.06.2015
// */
//public class ValidateTextInputEventListener implements javafx.event.EventHandler {
//
//    private int length;
//
//    private String contentMatches;
//
////   // public ValidateTextInputEventListener(int length, ) {
////        this.imageView = imageView;
////    }
//
//    @Override
//    public void handle(Event event) {
//        FileChooser fileChooser = new FileChooser();
//
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
//        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
//        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
//
//        //Show open file dialog
//        File file = fileChooser.showOpenDialog(null);
//
//        try {
//            BufferedImage bufferedImage = ImageIO.read(file);
//            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//            imageView.setImage(image);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}

package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import layout.DlgMain;
import layout.DlgStatusPrompt;
import util.WzFXWindowUtil;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			DlgMain root = new DlgMain(primaryStage);
			
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.initStyle(StageStyle.TRANSPARENT); //去除标题栏
			primaryStage.setTitle("伟仔WiFi助手"); //设置窗口标题
			//primaryStage.setAlwaysOnTop(true); //顶层窗口
			primaryStage.getIcons().add(new Image("images/icon.png")); //设置窗口图标
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}

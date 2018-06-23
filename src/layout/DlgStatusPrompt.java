package layout;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.WzFXWindowUtil;

public class DlgStatusPrompt extends GridPane
{
	@FXML Button btnOK;
	@FXML Label statusText;
	
	public DlgStatusPrompt(Stage stage) throws Exception
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/DlgStatusPrompt.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();	
		
		//支持手机移动
		WzFXWindowUtil.supportMouseMove(stage, this);
		
		btnOK.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				btnOK.setStyle("-fx-background-color:pink;-fx-background-radius:10;");
			}
		});
		
		btnOK.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				btnOK.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
			}
		});
		
		btnOK.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				stage.close(); //关闭窗口
			}
		});
	}

	
	public void setStatusText(String text)
	{
		this.statusText.setText(text);
	}
}

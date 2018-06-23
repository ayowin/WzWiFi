package layout;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DlgMain extends StackPane
{
	@FXML ImageView minImageView;
	@FXML ImageView closeImageView;
	@FXML TextField nameTextField;
	@FXML TextField passwordTextField;
	@FXML Button openButton;
	@FXML Button closeButton;
	
	double xOffset = 0;
	double yOffset = 0;
	boolean isOpened = false;
	
	public DlgMain(Stage primaryStage) throws Exception
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/DlgMain.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();	
		
		//加载配置
		loadConfig();
		
		//初始化
		init(primaryStage);
	}
	
	private void init(Stage primaryStage)
	{
		//支持鼠标移动，重写onMousePressed和onMouseDragged
		this.setOnMousePressed(new EventHandler<MouseEvent>() // 鼠标安装下，获取x,y偏移量
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				event.consume();
				xOffset = event.getScreenX() - primaryStage.getX();
				yOffset = event.getScreenY() - primaryStage.getY();
				
				nameTextField.setEditable(false);
				passwordTextField.setEditable(false);
				
				//支持打开状态下修改wifi名、wifi密码
				try
				{
					if(isOpened && checkConfigChanged())
					{	
						if(nameTextField.getText().length() == 0)
						{
							Stage stage = new Stage();
							DlgStatusPrompt root = new DlgStatusPrompt(stage);
							root.setStatusText("WIFI名为空！");
							Scene scene = new Scene(root, 300, 150);
							scene.setFill(null);// 场景不填充，实现不规则窗口
							stage.initStyle(StageStyle.TRANSPARENT); // 去除标题栏
							stage.setTitle("伟仔WiFi助手"); // 设置窗口标题
							// stage.setAlwaysOnTop(true); //顶层窗口
							stage.getIcons().add(new Image("images/icon.png")); // 设置窗口图标
							stage.initModality(Modality.APPLICATION_MODAL); // 模态窗口
							stage.setScene(scene);
							stage.show(); //弹出对话框
						}
						else if(passwordTextField.getText().length() < 8)
						{
							Stage stage = new Stage();
							DlgStatusPrompt root = new DlgStatusPrompt(stage);
							root.setStatusText("密码不足8位！");
							Scene scene = new Scene(root, 300, 150);
							scene.setFill(null);// 场景不填充，实现不规则窗口
							stage.initStyle(StageStyle.TRANSPARENT); // 去除标题栏
							stage.setTitle("伟仔WiFi助手"); // 设置窗口标题
							// stage.setAlwaysOnTop(true); //顶层窗口
							stage.getIcons().add(new Image("images/icon.png")); // 设置窗口图标
							stage.initModality(Modality.APPLICATION_MODAL); // 模态窗口
							stage.setScene(scene);
							stage.show(); //弹出对话框
						}
						if(changedWifi() && startWifi()) saveConfig();
						else
						{
							Stage stage = new Stage();
							DlgStatusPrompt root = new DlgStatusPrompt(stage);
							root.setStatusText("修改失败！");
							Scene scene = new Scene(root, 300, 150);
							scene.setFill(null);// 场景不填充，实现不规则窗口
							stage.initStyle(StageStyle.TRANSPARENT); // 去除标题栏
							stage.setTitle("伟仔WiFi助手"); // 设置窗口标题
							// stage.setAlwaysOnTop(true); //顶层窗口
							stage.getIcons().add(new Image("images/icon.png")); // 设置窗口图标
							stage.initModality(Modality.APPLICATION_MODAL); // 模态窗口
							stage.setScene(scene);
							stage.show(); //弹出对话框
						}
					}
				} catch (Exception e)
				{
					// TODO: handle exception
				}	
			}		
		});
		this.setOnMouseDragged(new EventHandler<MouseEvent>() // 标题栏支持鼠标按下拖动
		{
			@Override
			public void handle(MouseEvent event) // 支持拖动
			{
				// TODO Auto-generated method stub
				event.consume();
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});
		
		// 最小化ImageView
		minImageView.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				minImageView.setImage(new Image("/images/min_mouseover.png"));//响应图标
			}
		});
		minImageView.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				minImageView.setImage(new Image("/images/min_normal.png"));//正常图标
			}
		});
		minImageView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(event.getButton() == MouseButton.PRIMARY)
				primaryStage.setIconified(true);//最小化
			}
		});
		
		//关闭ImageView
		closeImageView.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				closeImageView.setImage(new Image("/images/close_mouseover.png"));//响应图标
			}
		});
		closeImageView.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				closeImageView.setImage(new Image("/images/close_normal.png"));//正常图标
			}
		});
		closeImageView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(event.getButton() == MouseButton.PRIMARY)
				//System.exit(0);//必杀技，关闭当前的虚拟机
				primaryStage.close();//退出当前窗口，如果子线程，需先手动回收子线程
			}
		});
		
		//WIFI名称nameTextField
		nameTextField.setFocusTraversable(false);
		nameTextField.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				nameTextField.setEditable(true);
			}
		});
		
		//WIFI密码passwordTextField
		passwordTextField.setFocusTraversable(false);
		passwordTextField.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				passwordTextField.setEditable(true);
			}
		});
		
		//开启按钮
		openButton.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(isOpened)
					openButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
				else
					openButton.setStyle("-fx-background-color:#90E690;-fx-background-radius:10;");
			}
		});
		openButton.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(isOpened)
					openButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
				else
					openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
			}
		});
		openButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if (event.getButton() == MouseButton.PRIMARY)
				{
					if (isOpened)
						return;

					// 执行业务
					try
					{
						// 初始化状态对话框
						Stage stage = new Stage();
						DlgStatusPrompt root = new DlgStatusPrompt(stage);

						if (nameTextField.getText().length() == 0)
						{
							root.setStatusText("WIFI名为空！");
							isOpened = false;
							openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
							closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
						} 
						else if (passwordTextField.getText().length() < 8)
						{
							root.setStatusText("密码不足8位！");
							isOpened = false;
							openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
							closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
						}

						if (checkWifiDrivers())
						{
							if (changedWifi() && startWifi())
							{
								saveConfig();
								isOpened = true;
								openButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
								closeButton.setStyle("-fx-background-color:pink;-fx-background-radius:10;");
							} 
							else
							{
								root.setStatusText("开启WIFI失败！");
								isOpened = false;
								openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
								closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
							}
						} 
						else
						{
							root.setStatusText("您的硬件不支持！");
							isOpened = false;
							openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
							closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
						}

						Scene scene = new Scene(root, 300, 150);
						scene.setFill(null);// 场景不填充，实现不规则窗口
						stage.initStyle(StageStyle.TRANSPARENT); // 去除标题栏
						stage.setTitle("伟仔WiFi助手"); // 设置窗口标题
						// stage.setAlwaysOnTop(true); //顶层窗口
						stage.getIcons().add(new Image("images/icon.png")); // 设置窗口图标
						stage.initModality(Modality.APPLICATION_MODAL); // 模态窗口
						stage.setScene(scene);
						stage.show(); //弹出对话框
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		
		//关闭按钮
		closeButton.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(isOpened)
					closeButton.setStyle("-fx-background-color:#F7C0CB;-fx-background-radius:10;");
				else
					closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
			}
		});
		closeButton.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(isOpened)
					closeButton.setStyle("-fx-background-color:pink;-fx-background-radius:10;");
				else
					closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
			}
		});
		closeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				if(event.getButton() == MouseButton.PRIMARY)
				{
					if(!isOpened) return;
					try
					{
						closeWifi();
					} 
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isOpened = false;
					openButton.setStyle("-fx-background-color:lightgreen;-fx-background-radius:10;");
					closeButton.setStyle("-fx-background-color:lightgray;-fx-background-radius:10;");
				}
			}
		});
	}

	
	//加载配置
	private void loadConfig() throws Exception
	{
		//先关闭wifi
		closeWifi();
		
		SAXReader reader = new SAXReader();
        //Document document = reader.read(DlgMain.class.getResourceAsStream("/config.xml"));
		Document document = reader.read(System.getProperty("user.dir") + "/config.xml");
        Element root = document.getRootElement();
        nameTextField.setText(root.elementText("name"));
        passwordTextField.setText(root.elementText("password"));
	}
	
	//保存配置
	private void saveConfig() throws Exception
	{
		//如果未发生更改则不需要保存
		if(!checkConfigChanged())return;
		
		//保存配置
		SAXReader reader = new SAXReader();
        //Document document = reader.read(DlgMain.class.getResourceAsStream("/config.xml"));
		Document document = reader.read(System.getProperty("user.dir") + "/config.xml");
		Element root = document.getRootElement();
        root.element("name").setText(nameTextField.getText());
        root.element("password").setText(passwordTextField.getText());

		OutputFormat format = OutputFormat.createPrettyPrint(); // 格式化为缩进方式
		format.setEncoding("UTF-8"); // 设置写入流编码
		//XMLWriter writer = new XMLWriter(new FileWriter(DlgMain.class.getResource("/config.xml").getFile()), format);
		XMLWriter writer = new XMLWriter(new FileWriter(System.getProperty("user.dir") + "/config.xml"), format);
		writer.write(document); // 向流写入数据
		writer.close(); // 关闭流
	}

	//检查配置是否发生了更改
	private boolean checkConfigChanged() throws Exception
	{
		SAXReader reader = new SAXReader();
        //Document document = reader.read(DlgMain.class.getResourceAsStream("/config.xml"));
		Document document = reader.read(System.getProperty("user.dir") + "/config.xml");
		Element root = document.getRootElement();
        
        return (!nameTextField.getText().equals(root.elementText("name"))  || 
        		!passwordTextField.getText().equals(root.elementText("password")));
	}
	
	//检查硬件是否支持wifi
	private boolean checkWifiDrivers() throws Exception
	{
		String command = "netsh wlan show drivers";
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		
		if(process.exitValue() == 0) return true;
		else return false;
	}
	
	//开启wifi
	private boolean startWifi() throws Exception
	{
		String command = "netsh wlan start hostednetwork";
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		
		if(process.exitValue() == 0) return true;
		else return false;
	}
	
	//关闭wifi
	private boolean closeWifi() throws Exception
	{
		String command = "netsh wlan stop hostednetwork";
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		
		if(process.exitValue() == 0) return true;
		else return false;
	}
	
	//更改wifi
	private boolean changedWifi() throws Exception
	{
		String command = "netsh wlan set hostednetwork mode=allow ssid=" + 
				  nameTextField.getText() + 
				  " key=" + 
				  passwordTextField.getText();
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		
		if(process.exitValue() == 0) return true;
		else return false;
	}
}

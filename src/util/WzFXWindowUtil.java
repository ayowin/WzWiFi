package util;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

/*
 * 作者：欧阳伟
 * 日期：2018-4-12
 * 内容：
 * 		WzFXWindowUtil
 * 作用：
 * 		JavaFX窗口常用功能工具类
 * 		当前功能：边框拉伸、鼠标拖动、窗口居中，更多功能请待后续。
 */

public class WzFXWindowUtil
{
	//鼠标位置
	private static boolean isLeft; //左
	private static boolean isTop; //上
	private static boolean isRight; //右
	private static boolean isBottom; //下
	
	private static double oldScreenX; //修改前鼠标x坐标
	private static double oldScreenY; //修改前鼠标y坐标
	private static double oldStageWidth; //修改前窗口宽度
	private static double oldStageHeight; //修改前窗口高度
	private static double xOffset; //x偏移量
	private static double yOffset; //y偏移量
	
	//边框拉伸相关属性，可在调用supportBorderStretch函数前，显式设置；也可以直接修改此处的值
	public static int resizeWidth = 2; //识别宽度，建议2或3
	public static int minWidth = 0; //窗口最小宽度
	public static int minHeight = 0; //窗口最小高度
	
	//将所有鼠标的位置状态复位为false
	private static void resetFalseToAll()
	{
		isLeft = false;
		isTop = false;
		isRight = false;
		isBottom = false;
	}

	//支持region边框拉伸窗口
	public static void supportBorderStretch(Stage stage, Region region)
	{
		//鼠标移动事件
		region.setOnMouseMoved(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				event.consume();
				
				//获取鼠标相对窗口的位置
				double x = event.getSceneX(); //x
				double y = event.getSceneY(); //y
				double width = stage.getWidth(); //获取当前窗口宽度
				double height = stage.getHeight(); //获取当前窗口高度
				
				resetFalseToAll();
				
				region.setCursor(Cursor.DEFAULT);
				
				if(x > 0 && y > 0)
				{
					//左
					if(x <= resizeWidth)
					{
						region.setCursor(Cursor.W_RESIZE);
						isLeft = true;
					}
					//上
					if(y <= resizeWidth)
					{
						region.setCursor(Cursor.N_RESIZE);
						isTop = true;
					}
					//右
					if(x >= width - resizeWidth && x <= width)
					{
						region.setCursor(Cursor.E_RESIZE);
						isRight = true;
					}
					//下
					if(y >= height - resizeWidth && y <= height)
					{
						region.setCursor(Cursor.S_RESIZE);
						isBottom = true;
					}
					//左上角
					if(isLeft && isTop) region.setCursor(Cursor.NW_RESIZE);
					//右上角
					if(isRight && isTop) region.setCursor(Cursor.NE_RESIZE);
					//左下角
					if(isLeft && isBottom) region.setCursor(Cursor.SW_RESIZE);
					//右下角
					if(isRight && isBottom) region.setCursor(Cursor.SE_RESIZE);
				}	
			}
		});
		
		//鼠标按下事件
		region.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				event.consume();
				oldScreenX = event.getScreenX();
				oldScreenY = event.getScreenY();
				oldStageWidth = stage.getWidth();
				oldStageHeight = stage.getHeight();
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		//鼠标拖动事件
		region.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				event.consume();
				double screenX = event.getScreenX();
				double screenY = event.getScreenY();

				if (isRight)
				{
					if ((screenX - oldScreenX + oldStageWidth) > minWidth)
						stage.setWidth(screenX - oldScreenX + oldStageWidth);
				}
				if (isBottom)
				{
					if ((screenY - oldScreenY + oldStageHeight) > minHeight)
						stage.setHeight(screenY - oldScreenY + oldStageHeight);
				}
				if (isLeft)
				{
					if((oldStageWidth + oldScreenX - event.getScreenX()) > minWidth)
					{
						stage.setX(event.getScreenX() - xOffset);
						stage.setWidth(oldStageWidth + oldScreenX - event.getScreenX());
					}
				}
				if (isTop)
				{
					if((oldStageHeight + oldScreenY - event.getScreenY()) > minHeight)
					{
						stage.setY(event.getScreenY() - yOffset);
						stage.setHeight(oldStageHeight + oldScreenY - event.getScreenY());
					}
				}
			}
		});	
	}

	//支持node鼠标点击移动窗口
	public static void supportMouseMove(Stage stage,Node node)
	{
		node.setOnMousePressed(new EventHandler<MouseEvent>() // 鼠标安装下，获取x,y偏移量
		{
			@Override
			public void handle(MouseEvent event)
			{
				// TODO Auto-generated method stub
				event.consume();
				xOffset = event.getScreenX() - stage.getX();
				yOffset = event.getScreenY() - stage.getY();
			}
		});
		node.setOnMouseDragged(new EventHandler<MouseEvent>() // 标题栏支持鼠标按下拖动
		{
			@Override
			public void handle(MouseEvent event) // 支持拖动
			{
				// TODO Auto-generated method stub
				event.consume();
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
	}

	//窗口居中
	public static void centerWindow(Stage stage, Scene scene)
	{
		Rectangle2D rectScreen = Screen.getPrimary().getBounds(); //获得屏幕物理边界
		double xRemainSpace = rectScreen.getMaxX() - scene.getWidth(); //x剩余空间
		if (xRemainSpace > 0) stage.setX(xRemainSpace / 2); //如果x有剩余空间则移动
		else stage.setX(0); //如果x没有剩余空间则设置为0
		double yRemainSpace = rectScreen.getMaxY() - scene.getHeight(); //y剩余空间
		if (yRemainSpace > 0) stage.setY(yRemainSpace / 2); //如果y有剩余空间则移动
		else stage.setY(0); //如果y没有剩余空间则设置为0
	}
}

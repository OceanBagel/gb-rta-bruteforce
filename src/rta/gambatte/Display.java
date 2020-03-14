package rta.gambatte;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

public class Display extends Canvas {

	private static final long serialVersionUID = -794520137457884047L;
	
	private JFrame frame;
	private RenderContext renderContext;
	private BufferedImage displayImage;
	private byte displayComponents[];
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	
	public Display(int width, int height, int scale, String title) {
		Dimension size = new Dimension(width * scale, height * scale);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		
		renderContext = new RenderContext(width, height);
		displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		displayComponents = ((DataBufferByte) displayImage.getRaster().getDataBuffer()).getData();
		
		frame = new JFrame();
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle(title);
		frame.setVisible(true);
		
		createBufferStrategy(1);
		bufferStrategy = getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public void swapBuffers() {
		renderContext.copyToByteArray(displayComponents);
		graphics.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null);
	}

	public RenderContext getRenderContext() {
		return renderContext;
	}

}
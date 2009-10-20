package tracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Sampler2D {
	
	int[] source;
	int sourceWidth;
	int sourceHeight;
	
	public abstract Vec3 sample( float u, float v );
	
	protected int[] rawRead( int x, int y ) {
		x = x % sourceWidth;
		y = y % sourceHeight;
		y = sourceHeight - y - 1; // flip Y so 0 is bottom.
		int index = y*sourceWidth*3 + x*3;
		int[] result = new int[3];
		result[0] = source[index];
		result[1] = source[index+1];
		result[2] = source[index+2];
		return result;
	}
	
	protected float signalMin, signalRange;
	public void setOutputRange( float min, float max ) {
		signalMin = min;
		signalRange = max-min;
	}
	protected float scaleOutput( int b ) {
		return signalMin + (b/255.0f)*signalRange;
	}
	
	// scales and wraps to float [0..sourceWidth)
	protected float scaleU( float u ) {
	    float r = u % 1;
		return (r * this.sourceWidth);
	}

	// scales and wraps to float [0..sourceHeight)
	protected float scaleV( float v ) {
	    float r = v % 1;
		return (r * this.sourceHeight);
	}
	
	
	Sampler2D() {
		source = null;
	}
	Sampler2D( String filename ) {
		File f = new File(filename);
		try {
			BufferedImage i = ImageIO.read(f);
			sourceWidth = i.getWidth();
			sourceHeight = i.getHeight();
			source = i.getData().getPixels( 0, 0, sourceWidth, sourceHeight, (int[])null );
			setOutputRange( 0, 1 );
		} catch (IOException e) {
			source = null;
		}
	}
	
	

}

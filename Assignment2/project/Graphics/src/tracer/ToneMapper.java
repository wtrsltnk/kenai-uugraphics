package tracer;

/**
 * Used for mapping floating point representations of colors into packed-int
 * colors as used by Java. This implementation also supports gamma correction.
 */
public class ToneMapper {
	
	public ToneMapper( float gamma ) {
		invGamma = 1.0f/gamma;	
	}

	public float invGamma;
	
	public int map( float r, float g, float b ) {
                // Apply gamma correction
                r = (float)Math.pow(r, invGamma);
                g = (float)Math.pow(g, invGamma);
                b = (float)Math.pow(b, invGamma);

                // Clamp values lower than 0 to 0
                if (r < 0) r = 0;
                if (g < 0) g = 0;
                if (b < 0) b = 0;

                // Clamp values higher the 1 to 1
                if (r > 1) r = 1;
                if (g > 1) g = 1;
                if (b > 1) b = 1;

                // convert to int
                int intR = (int)( r*255.0f );
                int intG = (int)( g*255.0f );
                int intB = (int)( b*255.0f );
                int intA = 255;

                // pack the colours into an int
                return (intA<<24) | (intR<<16) | (intG<<8) | intB;

	}	
	
}
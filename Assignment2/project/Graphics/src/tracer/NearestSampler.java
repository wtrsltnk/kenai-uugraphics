/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tracer;

/**
 *
 * @author Administrator
 */
public class NearestSampler extends Sampler2D {
	
	public NearestSampler(String filename) {
		super(filename);
	}

	public Vec3 sample( float u, float v ) {
		int [] result = new int[3];
		result = this.rawRead((int)scaleU(u), (int)scaleV(v));
		return new Vec3(scaleOutput(result[0]), scaleOutput(result[1]), scaleOutput(result[2]));
	}

}

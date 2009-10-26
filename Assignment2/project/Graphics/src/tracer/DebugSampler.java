/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tracer;

/**
 *
 * @author Administrator
 */
public class DebugSampler extends Sampler2D {

	public Vec3 sample( float u, float v ) {
		return new Vec3(u, v, 0);
	}

}

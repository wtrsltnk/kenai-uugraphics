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

	public DebugSampler() {
		
	}

	public Vec3 sample( float u, float v ) {
		return new Vec3(this.scaleU(u), this.scaleV(v), 0);
	}

}

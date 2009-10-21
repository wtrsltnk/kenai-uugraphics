/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tracer;

/**
 *
 * @author Administrator
 */
public class BilinearSampler extends Sampler2D {

	public BilinearSampler(String filename) {
		super(filename);
	}

	public Vec3 sample( float u, float v ) {
//		int color[] = this.rawRead((int)this.scaleU(u), (int)this.scaleV(v));
//		return new Vec3(color[0]/255.0f, color[1]/255.0f, color[2]/255.0f);
		return new Vec3();
	}

}

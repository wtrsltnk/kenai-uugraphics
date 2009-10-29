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
		float uDash = (float)(this.sourceWidth*u - Math.floor(this.sourceWidth*u));
		float vDash = (float)(this.sourceHeight*v - Math.floor(this.sourceHeight*v));

		Vec3 colorIJ	= new Vec3(this.rawRead((int)this.scaleU(u),		(int)this.scaleV(v)));
		Vec3 colorIIJ	= new Vec3(this.rawRead((int)this.scaleU(u) + 1,	(int)this.scaleV(v)));
		Vec3 colorIIJJ	= new Vec3(this.rawRead((int)this.scaleU(u) + 1,	(int)this.scaleV(v) + 1));
		Vec3 colorIJJ	= new Vec3(this.rawRead((int)this.scaleU(u),		(int)this.scaleV(v) + 1));

		float w1 = (1 - uDash) * (1 - vDash);
		float w2 = uDash * (1 - vDash);
		float w3 = (1 - uDash) * vDash;
		float w4 = uDash * vDash;

		colorIJ = colorIJ.times(w1);
		colorIIJ = colorIIJ.times(w2);
		colorIJJ = colorIJJ.times(w3);
		colorIIJJ = colorIIJJ.times(w4);

		return colorIJ.add(colorIIJ).add(colorIJJ).add(colorIIJJ);
	}

}

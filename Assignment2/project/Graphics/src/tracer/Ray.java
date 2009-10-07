package tracer;

import java.util.Iterator;

/**
 * Represents a ray: a ray can `trace' itself, calculating a color.
 */
public class Ray {

	public Vec3 origin;
	public Vec3 direction;
	
	public Ray( Vec3 o, Vec3 d ) {
		origin = new Vec3( o );
		direction = new Vec3( d );	
	}
	
	/**
	 * Calculates the local light term, given an IntersectionInfo and a Light.
	 * This method does <b>not</b> do an occlusion test; do this yourself (e.g.
	 * using a shadow feeler).
	 */
	public Vec3 localLight( IntersectionInfo info, Light light ) {
            // diffuse component
                // Calculate the Light direction vector
                Vec3 l = light.location.minus(info.location);
                l.normalize();

                // Calculate the dotproduct of the intersectino normal and teh light
                float ndotl = Math.max(0, info.normal.dot(l));
                // Calculate the intensity of the material color as diffuse light
                Vec3 cr = info.object.material.color.times(info.object.material.diffuse);
                // Calculate the intensity of the light color
                Vec3 cl = light.color.times(light.intensity);

                // Final diffuse color calculation
                Vec3 diffuse = cr.times(cl).times(ndotl);

            // specular component
                // Calulate and normalize the direction in which the camera looks at the intersection
                Vec3 e = this.direction.times(-1);
                e.normalize();

                // Calulate the vecor between e and l and normalize it
                Vec3 h = e.add(l);
                h.normalize();

                // Calculate the dotproduct to determine how far h is from the normal
                float hdotn = h.dot(info.normal);

                // Use specular power to set the intensity of specular effect
                float hdotnpow = (float)Math.pow(hdotn, info.object.material.specularPower);
                // Final specular color calculation
                Vec3 specular = cl.times(hdotnpow).times(info.object.material.specular);

                // Add the specular to the diffuse color
		return diffuse.add( specular );
	}
	
	/**
	 * Does the actual `raytracing'. Returns the color this ray `hits.'
	 * @param currentObject This object is ignored in the intersection tests; in
	 *    effect, this object is `invisible' to the ray. This is useful for
	 *    reflection rays and shadow feelers: it avoids precision-errors by just
	 *    ignoring the object you've just bounced off of. If all objects are
	 *    convex (which they are in this tracer) this is actually not a hack but
	 *    completely correct.
	 * @param maxReflectionsLeft Maximum recursion depth for reflection
	 *    calculations .
	 */
	public Vec3 trace( Traceable currentObject, int maxReflectionsLeft ) {
		// test all Traceable object in the scene for collision.
		// store the nearest one.
		IntersectionInfo nearestHit = null;
		Iterator i = Tracer.scene.iterator();
		while( i.hasNext() ) {
			
			Traceable t = (Traceable)i.next();
			if( t != currentObject ) {
				IntersectionInfo info = t.intersect( this );
				if(  info.hit  &&  (nearestHit==null || info.distance<nearestHit.distance)  ) {
					nearestHit = info;
				}
			}
			
		}
		if( nearestHit != null ) {
			// actually hit something
			Material material = nearestHit.object.material;
			Vec3 color = material.color.times( material.ambient ); // ambient
			
			// local contribution of light
			Iterator lightIter = Tracer.lights.iterator();
			while( lightIter.hasNext() ) {
				Light light = (Light)lightIter.next();
				Vec3 shadowFeelerDirection = light.location.minus( nearestHit.location );
				Ray shadowFeeler = new Ray( nearestHit.location, shadowFeelerDirection );
				if( !shadowFeeler.hit( nearestHit.object ) ) {
					color = color.add(  localLight( nearestHit, light )  );
				}
			}
			
			// global illumination
                        if (maxReflectionsLeft > 0) {
                                // Direction in which the current trace is tracing
                                Vec3 e = this.direction;
                                e.normalize();
                                // The normal of the nearest hit of the current trace
                                Vec3 n = nearestHit.normal;
                                n.normalize();

                                // Calulate the ray for tracing the reflectance
                                Ray ray = new Ray(nearestHit.location, e.add(n.times(e.times(-1).dot(n)).times(2)));

                                // Trace the reflection ray were for every reflectance the number of maxReflectionsLeft is lowered with 1
                                Vec3 reflectionColor = ray.trace(nearestHit.object, maxReflectionsLeft - 1);

                                // Add the color from tracing the reflection ray and multiply it with the materials reflectance setting
                                return color.add(reflectionColor.times(nearestHit.object.material.reflectance));
                        }
			
			
			return color;
			
			
		} else {
			// hit nothing; return background color
			return new Vec3( 0.0f, 0.0f, 0.0f );
		}
		
	}
	
	/**
	 * Checks if the ray (origin + t*direction) hits the scene with 0 <= t <= 1.
	 * @param ignoreObject Similar to trace's currentObject parameter.
	 *    @see trace.
	 */
	public boolean hit( Traceable ignoreObject ) {
		Iterator i = Tracer.scene.iterator();
		while( i.hasNext() ) {
			Traceable t = (Traceable)i.next();
			if( t != ignoreObject ) {
				if (t.hit(this)) {
                                    return true;
                                }
			}
                }
		// replace the line below by meaningful code
		return false;
	}
	
}
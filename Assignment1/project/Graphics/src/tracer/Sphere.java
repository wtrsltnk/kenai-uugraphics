package tracer;

import java.io.IOException;

/**
 * A 3D roundish traceable thingy with a center and a radius
 */
public class Sphere extends Traceable {

	Vec3 origin;
	float radius;
	
	public Sphere( Vec3 o, float r ) {
		origin = new Vec3(o);
		radius = r;	
		material = new Material();
	}
	public Sphere() {
		origin = new Vec3();
		material = new Material();	
	}
	
	public void parse( Parser p ) throws IOException {
		p.parseKeyword( "{" );
		while( !p.tryKeyword("}") && !p.endOfFile() ) {
			
			if( p.tryKeyword("origin") ) {
				origin.parse( p );
			} else if( p.tryKeyword("radius") ) {
				radius = p.parseFloat();
			} else if( p.tryKeyword("material") ) {
				material.parse( p );
			} else {
				System.out.println( p.tokenWasUnexpected() );	
			}
			
		}
	}
	
	public IntersectionInfo intersect( Ray r ) {
		// delete the line of code below, and properly compute if the Ray r hits the Spere, and if so, 
		// record the nearest intersection point in the IntersectionInfo record, as well as the distance
		// of this intersection point to the origin of the ray. Initially, you may put a zero normal vector
		// in the IntersectionInfo, but as soon as we compute the local lighting model, you have to compute
		// a proper normal vector of the sphere at the intersection point. 

            float A = r.direction.dot(r.direction);
            float B = r.direction.times(2).dot(r.origin.minus(this.origin));
            float C = r.origin.minus(this.origin).dot(r.origin.minus(this.origin)) - (this.radius * this.radius);

            float D = (B * B) - 4 * A * C;

            if (D < 0) {
                // No solution
		
		// For now, simply return "no hit". Replace the line below by meaningful code
		return new IntersectionInfo(false);
            }
            else if (D == 0) {
                // One solution
                Vec3 location = new Vec3();
                Vec3 normal = new Vec3();

                float distance = (float)((-B + Math.sqrt(D)) / (2 * A));

                location = r.origin.add(r.direction.times(distance));
                normal = location.minus(this.origin);
                normal.normalize();

		return new IntersectionInfo(location, normal, distance, this);
            }
            else {
                // Two solutions
                Vec3 location = new Vec3();
                Vec3 normal = new Vec3();

                float tPlus = (float)((-B + Math.sqrt(D)) / (2 * A));
                float tMin = (float)((-B - Math.sqrt(D)) / (2 * A));

                float distance = Math.min(tPlus, tMin);

                location = r.origin.add(r.direction.times(distance));
                normal = location.minus(this.origin);
                normal.normalize();

		return new IntersectionInfo(location, normal, distance, this);
            }
	}
	
	public boolean hit( Ray r ) {
            float A = r.direction.dot(r.direction);
            float B = r.direction.times(2).dot(r.origin.minus(this.origin));
            float C = r.origin.minus(this.origin).dot(r.origin.minus(this.origin)) - (this.radius * this.radius);

            float D = (B * B) - 4 * A * C;

            float distance = (float)((-B + Math.sqrt(D)) / (2 * A));
            return (D > 0 && distance >= 0 && distance <= 1);
	}
	
}
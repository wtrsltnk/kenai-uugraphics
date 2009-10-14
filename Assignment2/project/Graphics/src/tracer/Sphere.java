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
                // Impementation of the ABC formula
                float A = r.direction.dot(r.direction);
                float B = r.direction.times(2).dot(r.origin.minus(this.origin));
                float C = r.origin.minus(this.origin).dot(r.origin.minus(this.origin)) - (this.radius * this.radius);

                float D = (B * B) - 4 * A * C;

                // If the discriminant < 0 there is no intersection
                if (D < 0) {
                        return new IntersectionInfo(false);
                }
                // If the discrimant = 0 there is one solotion
                else if (D == 0) {
                        Vec3 location = new Vec3();
                        Vec3 normal = new Vec3();

                        // Calculate the nearest hit
                        float distance = (float)((-B - Math.sqrt(D)) / (2 * A));

                        // Calulate the location and normal from the distance
                        location = r.origin.add(r.direction.times(distance));
                        normal = location.minus(this.origin);
                        normal.normalize();

                        // Make sure we only look in the positive direction of the ray, otherwise there will be weird artifacts visible
                        if (distance >= 0) {
							return new IntersectionInfo(location, normal, distance, this);
                        }
                        else{
							return new IntersectionInfo(false);
                        }
                }
                // If the discrimant > 0 there are two solotion
                else {
                        Vec3 location = new Vec3();
                        Vec3 normal = new Vec3();

                        // Calulate both hits
                        float tPlus = (float)((-B + Math.sqrt(D)) / (2 * A));
                        float tMin = (float)((-B - Math.sqrt(D)) / (2 * A));

                        // Use the nearest hit
                        float distance = Math.min(tPlus, tMin);

                        // Calulate the location and normal from the distance
                        location = r.origin.add(r.direction.times(distance));
                        normal = location.minus(this.origin);
                        normal.normalize();

                        // Make sure we only look in the positive direction of the ray, otherwise there will be weird artifacts visible
                        if (distance >= 0) {
                                return new IntersectionInfo(location, normal, distance, this);
                        }
                        else{
                                return new IntersectionInfo(false);
                        }
                }
	}
	
	public boolean hit( Ray r ) {
                // Impementation of the ABC formula
                float A = r.direction.dot(r.direction);
                float B = r.direction.times(2).dot(r.origin.minus(this.origin));
                float C = r.origin.minus(this.origin).dot(r.origin.minus(this.origin)) - (this.radius * this.radius);

                float D = (B * B) - 4 * A * C;

                float distance = (float)((-B + Math.sqrt(D)) / (2 * A));

                // When there is an intersection, and the distance of this intersection is between 1 and 0, there is a hit
                return (D > 0 && distance >= 0 && distance <= 1);
	}
	
}
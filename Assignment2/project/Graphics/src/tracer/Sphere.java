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

		Vec3 location = new Vec3();
		Vec3 normal = new Vec3();
		float distance = 0;

		// Impementation of the ABC formula
		float A = r.direction.dot(r.direction);
		float B = r.direction.times(2).dot(r.origin.minus(this.origin));
		float C = r.origin.minus(this.origin).dot(r.origin.minus(this.origin)) - (this.radius * this.radius);
		float D = (B * B) - 4 * A * C;

		// Calulate both hits
		float tPlus = (float)((-B + Math.sqrt(D)) / (2 * A));
		float tMin = (float)((-B - Math.sqrt(D)) / (2 * A));

		if (D < 0) {													// If the discriminant < 0 there is no intersection
			return new IntersectionInfo(false);
		}
		else if (D == 0) {												// If the discrimant = 0 there is one solotion
			// Calculate the nearest hit
			distance = tMin;
		}
		else {															// If the discrimant > 0 there are two solotion
			// Use the nearest hit
			distance = Math.min(tPlus, tMin);
		}

		// Calulate the location and normal from the distance
		location = r.origin.add(r.direction.times(distance));
		normal = location.minus(this.origin);
		normal.normalize();

		// Make sure we only look in the positive direction of the ray, otherwise there will be weird artifacts visible
		if (distance >= 0) {
			if (this.material.texture != null) {

				// TODO: Calculate u & v
				float theta = (float)Math.acos((location.y - origin.y) / radius);
				float phi = (float)Math.atan2((location.x - origin.x), (location.z - origin.z));
				phi += Math.PI;

				if (phi < 0)
					phi += (Math.PI * 2);
				float PI2 = (float)Math.PI * 2;
				float u = phi / PI2;
				float v = (float)((Math.PI - theta) / Math.PI);

				return new IntersectionInfo(location, normal, distance, this, u, v);
			}
			return new IntersectionInfo(location, normal, distance, this);
		}
		else{
			return new IntersectionInfo(false);
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
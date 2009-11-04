package tracer;

import java.io.IOException;

/**
 * 3D flat, onbounded, traceable thingy, represented by normal vector and distance from the origin
 */
public class Plane extends Traceable {
	
	Vec3 normal;
	float offset;
	Vec3 uDirection;
	Vec3 vDirection;
	
	public Plane( Vec3 n, float o ) {
		normal = new Vec3(n);
		normal.normalize();
		offset = o;
		material = new Material();
	}
	public Plane() {
		normal = new Vec3(0,1,0);
		offset = 0.0f;
		material = new Material();
	}
	
	public void parse( Parser p ) throws IOException {
		p.parseKeyword( "{" );
		while( !p.tryKeyword("}") && !p.endOfFile() ) {
			
			if( p.tryKeyword("normal") ) {
				normal.parse( p );
			} else if( p.tryKeyword("offset") ) {
				offset = p.parseFloat();
			} else if( p.tryKeyword("material") ) {
				material.parse( p );
			} else if( p.tryKeyword("udirection") ) {
				uDirection = new Vec3();
				uDirection.parse( p );
			} else if( p.tryKeyword("vdirection") ) {
				vDirection = new Vec3();
				vDirection.parse( p );
			} else {
				System.out.println( p.tokenWasUnexpected() );	
			}
		}
		if (this.uDirection == null) 
			this.uDirection = this.normal.cross(this.normal);
		if (this.vDirection == null)
			this.vDirection = this.normal.cross(this.uDirection);
	}
	
	public IntersectionInfo intersect( Ray r ) {
		Vec3 p1 = this.normal.times(-this.offset);
		if (r.direction.dot(this.normal) != 0) {
			float t = (p1.minus(r.origin)).dot(this.normal) / r.direction.dot(this.normal);

			// Make sure we only look in the positive direction of the ray, otherwise there will be weird artifacts visible
			if (t >= 0)
			{
				Vec3 hitLocation = r.origin.add(r.direction.times(t));
				if (this.material.texture != null) {
					return new IntersectionInfo(hitLocation, normal, t, this, hitLocation.dot(uDirection), hitLocation.dot(vDirection));
				}
				return new IntersectionInfo(hitLocation, normal, t, this);
			}
		}
		return new IntersectionInfo(false);
	}
	
	public boolean hit( Ray r ) {
		Vec3 p1 = this.normal.times(-this.offset);
		float t = (p1.minus(r.origin)).dot(this.normal) / r.direction.dot(this.normal);

		// Make sure the distance is between 0 and 1
		return (t >= 0 && t <= 1);
	}
	
}
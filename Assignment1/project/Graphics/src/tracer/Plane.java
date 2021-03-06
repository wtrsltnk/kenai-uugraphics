package tracer;

import java.io.IOException;

/**
 * 3D flat, onbounded, traceable thingy, represented by normal vector and distance from the origin
 */
public class Plane extends Traceable {

	Vec3 normal;
	float offset;
	
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
			} else {
				System.out.println( p.tokenWasUnexpected() );	
			}
			
		}
	}
	
	public IntersectionInfo intersect( Ray r ) {
                Vec3 p1 = this.normal.times(-this.offset);
                float t = (p1.minus(r.origin)).dot(this.normal) / r.direction.dot(this.normal);

                // Make sure we only look in the positive direction of the ray, otherwise there will be weird artifacts visible
                if (t >= 0)
                {
                    return new IntersectionInfo(r.origin.add(r.direction.times(t)), normal, t, this);
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
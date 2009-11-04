package tracer;
import java.io.IOException;

public class Triangle extends Traceable {

	Vertex vertices[];

	public Triangle() {
		this.vertices = new Vertex[3];
		this.material = new Material();
	}
	
	public Triangle(Vertex v0, Vertex v1, Vertex v2) {
		this.vertices = new Vertex[3];
		this.vertices[0] = v0;
		this.vertices[1] = v1;
		this.vertices[2] = v2;
		this.material = new Material();
	}
	
	public void parse( Parser p ) throws IOException {
		p.parseKeyword( "{" );
		while( !p.tryKeyword("}") && !p.endOfFile() ) {
			if( p.tryKeyword("p1") ) {
				this.vertices[0] = new Vertex();
				this.vertices[0].point.parse( p );
			} else if( p.tryKeyword("u1") ) {
				this.vertices[0].u = p.parseFloat();
			} else if( p.tryKeyword("v1") ) {
				this.vertices[0].v = p.parseFloat();
			}

			else if( p.tryKeyword("p2") ) {
				this.vertices[1] = new Vertex();
				this.vertices[1].point.parse( p );
			} else if( p.tryKeyword("u2") ) {
				this.vertices[1].u = p.parseFloat();
			} else if( p.tryKeyword("v2") ) {
				this.vertices[1].v = p.parseFloat();
			}

			else if( p.tryKeyword("p3") ) {
				this.vertices[2] = new Vertex();
				this.vertices[2].point.parse( p );
			} else if( p.tryKeyword("u3") ) {
				this.vertices[2].u = p.parseFloat();
			} else if( p.tryKeyword("v3") ) {
				this.vertices[2].v = p.parseFloat();
			}

			else if( p.tryKeyword("material") ) {
				material.parse( p );
			} else {
				System.out.println( p.tokenWasUnexpected() );
			}
		}
	}
	
	private Vec3 getBYt(Ray r) {
		float a = this.vertices[0].point.x - this.vertices[1].point.x;
		float b = this.vertices[0].point.y - this.vertices[1].point.y;
		float c = this.vertices[0].point.z - this.vertices[1].point.z;

		float d = this.vertices[0].point.x - this.vertices[2].point.x;
		float e = this.vertices[0].point.y - this.vertices[2].point.y;
		float f = this.vertices[0].point.z - this.vertices[2].point.z;

		float g = r.direction.x;
		float h = r.direction.y;
		float i = r.direction.z;

		float j = this.vertices[0].point.x - r.origin.x;
		float k = this.vertices[0].point.y - r.origin.y;
		float l = this.vertices[0].point.z - r.origin.z;

		float ei_min_hf = ((e * i) - (h * f));
		float gf_min_di = ((g * f) - (d * i));
		float dh_min_eg = ((d * h) - (e * g));

		float ak_min_jb = ((a * k) - (j * b));
		float jc_min_al = ((j * c) - (a * l));
		float bl_min_kc = ((b * l) - (k * c));

		float M = (a * ei_min_hf) + (b * gf_min_di) + (c * dh_min_eg);

		float B = ( (j * ei_min_hf) + (k * gf_min_di) + (l * dh_min_eg) ) / M;
		float Y = ( (i * ak_min_jb) + (h * jc_min_al) + (g * bl_min_kc) ) / M;
		float t = -( (f * ak_min_jb) + (e * jc_min_al) + (d * bl_min_kc) ) / M;

		return new Vec3(B, Y, t);
	}

	// TODO: Make performance better by calculating B, Y and t only when necessary
	public IntersectionInfo intersect( Ray r ) {
		Vec3 BYt = getBYt(r);

		if (BYt.z < 0) return new IntersectionInfo(false);
		if ((BYt.y < 0) || (BYt.y > 1)) return new IntersectionInfo(false);
		if ((BYt.x < 0) || (BYt.x > 1 - BYt.y)) return new IntersectionInfo(false);

		// Calculate The position of the hit
		Vec3 hitpoint = r.origin.add(r.direction.times(BYt.z));

		// Calculate normal
		Vec3 v1 = this.vertices[1].point.minus(this.vertices[0].point);
		Vec3 v2 = this.vertices[2].point.minus(this.vertices[0].point);
		Vec3 normal = v2.cross(v1);
		normal.normalize();

		if (this.material.texture != null) {
			//Calculate u & v
			float u = this.vertices[0].u + BYt.x * (this.vertices[1].u - this.vertices[0].u) + BYt.y * (this.vertices[2].u - this.vertices[0].u);
			float v = this.vertices[0].v + BYt.x * (this.vertices[1].v - this.vertices[0].v) + BYt.y * (this.vertices[2].v - this.vertices[0].v);

			return new IntersectionInfo(hitpoint, normal, BYt.z, this, u, v);
		}

		return new IntersectionInfo(hitpoint, normal, BYt.z, this);
	}

	public boolean hit( Ray r ) {
		Vec3 BYt = getBYt(r);

		if (BYt.z < 0 || BYt.z > 1) return false;
		if ((BYt.y < 0) || (BYt.y > 1)) return false;
		if ((BYt.x < 0) || (BYt.x > 1 - BYt.y)) return false;

		return true;
	}
}

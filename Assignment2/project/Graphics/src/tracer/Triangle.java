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

	public IntersectionInfo intersect( Ray r ) {
		return new IntersectionInfo(false);
	}

	public boolean hit( Ray r ) {

		return false;
	}
}

package tracer;
import java.io.IOException;

/**
 * Represents a 3-dimensional vector.
 * Note that all the `operator' methods return a <i>new</i> Vec3 object.
 */
public class Vec3 {
	
	public float x, y, z;
	
	/**
	 * Returns a Vec3 of specified length with a random direction.
	 */
	public static Vec3 random( float length ) {
		Vec3 v = new Vec3( (float)Math.random(), (float)Math.random(), (float)Math.random() );
		v.normalize();
		v = v.times( length );
		return v;
	}
	
	/**
	 * Reads a vector in the form "&lt;number, number, number&gt;" from the Parser p.
	 * Changes this vector.
	 * @see toString
	 */
	public void parse( Parser p ) throws IOException {
		p.parseKeyword( "<" );
		x = p.parseFloat();
		p.parseKeyword( "," );
		y = p.parseFloat();
		p.parseKeyword( "," );
		z = p.parseFloat();
		p.parseKeyword( ">" );
	}
	
	/**
	 * Vector subtraction. Returns a new Vec3 equal to this - that.
	 */
	public Vec3 minus( Vec3 that ) {
		return new Vec3( x - that.x
		               , y - that.y
		               , z - that.z
		               );	
	}
	/**
	 * Vector addition. Returns a new Vec3 equal to this + that.
	 */
	public Vec3 add( Vec3 that ) {
		return new Vec3( x + that.x
		               , y + that.y
		               , z + that.z
		               );
	}
	/**
	 * Multiplication by a scalar. Returns a new Vec3 equals to f * this.
	 */
	public Vec3 times( float f ) {
		return new Vec3( f*x
		               , f*y
		               , f*z
		               );
	}
	
	/**
	 * Component-wise multiply
	 */
	 public Vec3 times( Vec3 that ) {
	 	return new Vec3( x*that.x
	 	               , y*that.y
	 	               , z*that.z
	 	               );
	 	                 	
	 }
	
	/**
	 * Returns the length of this vector.
	 * Use lengthSquared() instead if you're going to square the result anyway:
	 * lengthSquared() is more efficient.
	 * @see lengthSquared
	 */
	public float length() {
		return (float)Math.sqrt( lengthSquared() );
	}
	/**
	 * Return the square of the length of this vector.
	 * @see length
	 **/
	public float lengthSquared() {
//                float lengthS;
//                lengthS = (float)Math.pow(x, 2) + (float)Math.pow(y, 2) + (float)Math.pow(z, 2);
//		return lengthS;
            return (x * x) + (y * y) + (z * z);
	}
	
	/**
	 * Vector dot-product. Returns a new Vec3 equal to this dot that.
	 */
	public float dot( Vec3 that ) {
                return (this.x * that.x) + (this.y * that.y) + (this.z * that.z);
	}
	/**
	 * Vector cross-product. Returns a new Vec3 equal to this cross that.
	 */
	public Vec3 cross( Vec3 b ) {
		return new Vec3(
                        (this.y * b.z) - (this.z * b.y),
                        (this.z * b.x) - (this.x * b.z),
                        (this.x * b.y) - (this.y + b.x)
                        );
	}
	
	/**
	 * Normalizes this vector.
	 * Warning: this actually changes this instance, contrary to most methods.
	 */
	public void normalize() {
		float invLength = 1.0f / length();
		x *= invLength;
		y *= invLength;
		z *= invLength;
	}
	
	/**
	 * Copy constructor
	 */
	public Vec3( Vec3 that ) {
		x = that.x;
		y = that.y;
		z = that.z;
	}
	/**
	 * Constructor from floats.
	 */
	public Vec3( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Default constructor. Makes the vector (0,0,0)
	 */
	public Vec3() {
		x = 0;	
		y = 0;
		z = 0;
	}
	
	/**
	 * Returns a string representation of this vector, of the form
	 * "&lt;number, number, number&gt;" as can be parsed by the parse(Parser) method.
	 * @see parse
	 */
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";	
	}
	
}
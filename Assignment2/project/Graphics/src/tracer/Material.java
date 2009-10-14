package tracer;

import java.io.IOException;

/**
 * Holds shading parameters.
 */
public class Material {
	
	public Material() {
		color = new Vec3( 1, 1, 1 );	
		ambient = 0.2f;
		diffuse	= 0.8f;
		specular = 0.0f;
		specularPower = 16.0f;
		reflectance = 0.0f;
		perlin = new Vec3( 1, 1, 1 );
		usePerlin = false;
	}
	
	public Vec3 color;	
	public float ambient;
	public float diffuse;
	public float specular;
	public float specularPower;
	public float reflectance;
	public Vec3 perlin;
	public boolean usePerlin;
	public TextureSamping textureSampling;
	public String textureFile;
	
	public void parse( Parser p ) throws IOException {
		p.parseKeyword( "{" );
		while( !p.tryKeyword("}") && !p.endOfFile() ) {
			
			if( p.tryKeyword("color") ) {
				color.parse( p );
				//perlin = new Vec3(color.x, color.y, color.z);
			} else if( p.tryKeyword("ambient") ) {
				ambient = p.parseFloat();
			} else if( p.tryKeyword("diffuse") ) {
				diffuse = p.parseFloat();
			} else if( p.tryKeyword("specular") ) {
				specular = p.parseFloat();
			} else if( p.tryKeyword("specularpower") ) {
				specularPower = p.parseFloat();
			} else if( p.tryKeyword("reflectance") ) {
				reflectance = p.parseFloat();
			} else if( p.tryKeyword("perlin") ) {
				color.parse(p);
				perlin.parse(p);
				usePerlin = true;
			} else if( p.tryKeyword("texture") ) {
				// Parse the Samping mode
				if (p.tryKeyword("bilinear")) {
					this.textureSampling = TextureSamping.Bilinear;
				} else if (p.tryKeyword("nearest")) {
					this.textureSampling = TextureSamping.Nearest;
				} else if (p.tryKeyword("debug")) {
					this.textureSampling = TextureSamping.Debug;
				}

				// Only parse texture file when samping mode is not Debug
				if (this.textureSampling != TextureSamping.Debug) {
					this.textureFile = p.parseString();
				}
			} else {
				System.out.println( p.tokenWasUnexpected() );	
			}
			
		}
	}
	
}
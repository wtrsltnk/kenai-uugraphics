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
		texture = null;
	}
	
	public Vec3 color;	
	public float ambient;
	public float diffuse;
	public float specular;
	public float specularPower;
	public float reflectance;
	public Vec3 perlin;
	public boolean usePerlin;
	public Sampler2D texture;

	public Vec3[] G;
	public int[] P;
	public static final int N = 256;
	
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
				generateRandomArrays();
			} else if( p.tryKeyword("texture") ) {
				String textureMode = p.parseString();
				// Parse the Samping mode
				if (textureMode.equals("bilinear")) {
					this.texture = new BilinearSampler(p.parseString());
				} else if (textureMode.equals("nearest")) {
					this.texture = new NearestSampler(p.parseString());
				} else if (textureMode.equals("debug")) {
					this.texture = new DebugSampler();
				}
			} else {
				System.out.println( p.tokenWasUnexpected() );	
			}
			
		}
	}
	
	public float calculatePerlinColor(Vec3 point) {
		float result = 0;

		for (int i = (int)Math.floor(point.x); i <= (int)(Math.floor(point.x) + 1); i++) {
			for (int j = (int)Math.floor(point.y); j <= (int)(Math.floor(point.y) + 1); j++) {
				for (int k = (int)Math.floor(point.z); k <= (int)(Math.floor(point.z) + 1); k++) {
					//result += calculatePerlinValue(point.x - i, point.y - j, point.z - k);
					Vec3 uvw = new Vec3(point.x - i, point.y - j, point.z - k);
					result += calculateCubicWeight(point.x - i) * calculateCubicWeight(point.y - j) * calculateCubicWeight(point.z - k) * (getRandomUnitVector(i, j, k).dot(uvw));
				}
			}
		}
		return result;
	}

	private float calculateCubicWeight(float t) {
		if (t < 1) {
			t = Math.abs(t);
			return (float)(2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1);
		}
		return 0;
	}

	private Vec3 getRandomUnitVector(float i, float j, float k) {
		int index = getRandomIndex(i + getRandomIndex(j + getRandomIndex(k)));
		return this.G[index];
	}

	private int getRandomIndex(float p) {
		p = Math.abs(p);
		return this.P[(int)(p % Material.N)];
	}

	private void generateRandomArrays() {
		// Create a vector array of size N
		this.G = new Vec3[Material.N];
		// Generate N unit vectors
		for (int i = 0; i < Material.N; i++) {
			this.G[i] = new Vec3(2, 2, 2);
			while (this.G[i].length() > 1) {
				this.G[i].x = (float)(2 * Math.random()) - 1;
				this.G[i].y = (float)(2 * Math.random()) - 1;
				this.G[i].z = (float)(2 * Math.random()) - 1;
			}
			this.G[i].normalize();
		}
		
		// Create a integer array of size N
		this.P = new int[Material.N];
		// Generate numbers ranging from 0 to N-1
		for (int i = 0; i < Material.N; i++) {
			this.P[i] = (int)(Math.random() * (Material.N-1));
		}
	}
	
}
package tracer;

public class Vertex {

	Vec3 point;
	float u;
	float v;

	public Vertex() {
		this.point = new Vec3();
		this.u = 0;
		this.v = 0;
	}

	public Vertex(Vec3 p, float u, float v) {
		this.point = p;
		this.u = u;
		this.v = v;
	}

}
